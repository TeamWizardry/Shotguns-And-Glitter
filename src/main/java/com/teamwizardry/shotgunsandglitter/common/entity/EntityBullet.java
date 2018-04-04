package com.teamwizardry.shotgunsandglitter.common.entity;

import com.teamwizardry.librarianlib.features.network.PacketHandler;
import com.teamwizardry.shotgunsandglitter.ShotgunsAndGlitter;
import com.teamwizardry.shotgunsandglitter.api.*;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import com.teamwizardry.shotgunsandglitter.common.network.PacketImpactSound;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EntityBullet extends EntityThrowable implements IBulletEntity {

	private static final DataParameter<Byte> BULLET_TYPE = EntityDataManager.createKey(EntityBullet.class, DataSerializers.BYTE);
	private static final DataParameter<String> BULLET_EFFECT = EntityDataManager.createKey(EntityBullet.class, DataSerializers.STRING);
	private static final DataParameter<Integer> CASTER_ID = EntityDataManager.createKey(EntityBullet.class, DataSerializers.VARINT);
	private static final DataParameter<Float> POTENCY = EntityDataManager.createKey(EntityBullet.class, DataSerializers.FLOAT);
	private static final DataParameter<BlockPos> ORIGIN = EntityDataManager.createKey(EntityBullet.class, DataSerializers.BLOCK_POS);

	public EntityBullet(@NotNull World world) {
		super(world);
		setSize(0.1F, 0.1F);
	}

	public EntityBullet(@NotNull World world, Vec3d normal, @NotNull BulletType bulletType, @NotNull BulletEffect bulletEffect, float inaccuracy, float potency) {
		super(world);
		setSize(0.1F, 0.1F);

		setBulletType(bulletType);
		setEffect(bulletEffect);
		setCasterId(-1);
		setPotency(potency);

		shoot(normal.x, normal.y, normal.z, bulletEffect.getVelocity(world, bulletType), inaccuracy);
		setOrigin(getPosition());
	}

	public EntityBullet(@NotNull World world, @NotNull EntityLivingBase caster, @NotNull BulletType bulletType, @NotNull BulletEffect bulletEffect, float inaccuracy, float potency) {
		super(world, caster);
		setSize(0.1F, 0.1F);

		setBulletType(bulletType);
		setEffect(bulletEffect);
		setCasterId(caster.getEntityId());
		setPotency(potency);

		shoot(caster, caster.rotationPitch, caster.rotationYaw, 0f, bulletEffect.getVelocity(world, bulletType), inaccuracy);

		setOrigin(getPosition());
	}

	@NotNull
	@Override
	public Entity getAsEntity() {
		return this;
	}

	@Override
	public @Nullable EntityLivingBase getEntityThrower() {
		return thrower;
	}

	@Override
	public @NotNull Vec3d getPositionAsVector() {
		return getPositionVector();
	}

	@Override
	public @NotNull BlockPos getPositionAsBlockPos() {
		return getPosition();
	}

	@Override
	protected void entityInit() {
		dataManager.register(BULLET_TYPE, (byte) 0);
		dataManager.register(BULLET_EFFECT, "");
		dataManager.register(CASTER_ID, -1);
		dataManager.register(ORIGIN, BlockPos.ORIGIN);
		dataManager.register(POTENCY, 1f);
	}

	@Override
	protected float getGravityVelocity() {
		return 0f;
	}

	@Override
	public void onUpdate() {
		double mX = motionX,
				mY = motionY,
				mZ = motionZ;

		super.onUpdate();
		if (isDead) return;

		if (!inGround) {
			motionX = mX;
			motionY = mY - getFalloff() / 100.0;
			motionZ = mZ;
		}

		if (ticksExisted >= 50.0) {
			setDead();
		} else {
			if (world.isRemote) {
				List<EntityPlayer> entities = world.getEntities(EntityPlayer.class, input -> input != null && !(input.getDistanceSq(posX, posY, posZ) > 5 * 5));

				for (EntityPlayer player : entities) {
					if (player.getEntityId() == getCasterId()) continue;

					SoundSystem.playSoundsQuiet(world, getPositionVector(), ModSounds.BULLET_FLYBY, ModSounds.DUST_SPARKLE);
				}
			}

			ShotgunsAndGlitter.PROXY.updateBulletEntity(world, this, getEffect());
		}
	}

	@Override
	public boolean isPushedByWater() {
		return false;
	}

	@Override
	protected void onImpact(@NotNull RayTraceResult result) {
		if (MinecraftForge.EVENT_BUS.post(new BulletImpactEvent(world, this, result)))
			return;

		if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
			IBlockState state = world.getBlockState(result.getBlockPos());
			if (!world.isRemote && state.getCollisionBoundingBox(world, result.getBlockPos()) != Block.NULL_AABB &&
					state.getBlock().canCollideCheck(state, false) &&
					ShotgunsAndGlitter.PROXY.collideBulletWithBlock(world, this, result.getBlockPos(),
							state, getEffect(), result.hitVec)) {

				PacketHandler.NETWORK.sendToAllAround(new PacketImpactSound(getPositionVector(), getEffect().getID()),
						new NetworkRegistry.TargetPoint(world.provider.getDimension(), posX, posY, posZ, 512));
				setDead();
			}
		} else if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
			if (result.entityHit != null && !world.isRemote)
				if (ShotgunsAndGlitter.PROXY.collideBulletWithEntity(world, this,
						result.entityHit, getEffect(), result.hitVec)) {

					PacketHandler.NETWORK.sendToAllAround(new PacketImpactSound(getPositionVector(), getEffect().getID()),
							new NetworkRegistry.TargetPoint(world.provider.getDimension(), posX, posY, posZ, 512));
					setDead();
				}
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setByte("bulletType", dataManager.get(BULLET_TYPE));
		compound.setString("bulletEffect", dataManager.get(BULLET_EFFECT));
		compound.setFloat("bulletPotency", getPotency());
		compound.setLong("bulletOrigin", getOrigin().toLong());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		dataManager.set(BULLET_TYPE, compound.getByte("bulletType"));
		dataManager.set(BULLET_EFFECT, compound.getString("bulletEffect"));
		setPotency(compound.getFloat("bulletPotency"));
		setOrigin(BlockPos.fromLong(compound.getLong("bulletOrigin")));
	}

	@Override
	public double posX() {
		return posX;
	}

	@Override
	public double posY() {
		return posY;
	}

	@Override
	public double posZ() {
		return posZ;
	}

	@Override
	public double motionX() {
		return motionX;
	}

	@Override
	public double motionY() {
		return motionY;
	}

	@Override
	public double motionZ() {
		return motionZ;
	}

	@NotNull
	@Override
	public BulletType getBulletType() {
		return BulletType.byOrdinal(dataManager.get(BULLET_TYPE));
	}

	@Override
	public void setBulletType(@NotNull BulletType type) {
		dataManager.set(BULLET_TYPE, (byte) type.ordinal());
	}

	@NotNull
	@Override
	public BulletEffect getEffect() {
		return EffectRegistry.getBulletEffectByID(dataManager.get(BULLET_EFFECT));
	}

	@Override
	public void setEffect(@NotNull BulletEffect bulletEffect) {
		dataManager.set(BULLET_EFFECT, bulletEffect.getID());
	}

	@Override
	public int getCasterId() {
		return dataManager.get(CASTER_ID);
	}

	@Override
	public void setCasterId(int casterId) {
		dataManager.set(CASTER_ID, casterId);
	}

	@Override
	public BlockPos getOrigin() {
		return dataManager.get(ORIGIN);
	}

	@Override
	public void setOrigin(BlockPos pos) {
		dataManager.set(ORIGIN, pos);
	}

	@Override
	public float getPotency() {
		return dataManager.get(POTENCY);
	}

	@Override
	public void setPotency(float potency) {
		dataManager.set(POTENCY, potency);
	}
}
