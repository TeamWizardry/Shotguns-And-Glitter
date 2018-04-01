package com.teamwizardry.shotgunsandglitter.common.entity;

import com.teamwizardry.librarianlib.features.network.PacketHandler;
import com.teamwizardry.shotgunsandglitter.ShotgunsAndGlitter;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.EffectRegistry;
import com.teamwizardry.shotgunsandglitter.api.IBulletEntity;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import com.teamwizardry.shotgunsandglitter.common.network.PacketImpactSound;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class EntityBullet extends EntityThrowable implements IBulletEntity {

	private static final DataParameter<Byte> BULLET_TYPE = EntityDataManager.createKey(EntityBullet.class, DataSerializers.BYTE);
	private static final DataParameter<String> BULLET_EFFECT = EntityDataManager.createKey(EntityBullet.class, DataSerializers.STRING);
	private static final DataParameter<Integer> CASTER_ID = EntityDataManager.createKey(EntityBullet.class, DataSerializers.VARINT);

	private EntityLivingBase caster = null;

	public EntityBullet(@NotNull World world) {
		super(world);
		setSize(0.1F, 0.1F);
	}

	public EntityBullet(@NotNull World world, Vec3d normal, @NotNull BulletType bulletType, @NotNull Effect effect, float inaccuracy) {
		super(world);
		setSize(0.1F, 0.1F);

		setBulletType(bulletType);
		setEffect(effect);
		setCasterId(-1);

		shoot(normal.x, normal.y, normal.z, effect.getVelocity(world, bulletType), inaccuracy);
	}

	public EntityBullet(@NotNull World world, @NotNull EntityLivingBase caster, @NotNull BulletType bulletType, @NotNull Effect effect, float inaccuracy) {
		super(world, caster);
		setSize(0.1F, 0.1F);

		setBulletType(bulletType);
		setEffect(effect);
		setCasterId(caster.getEntityId());

		this.caster = caster;
		shoot(caster, caster.rotationPitch, caster.rotationYaw, 0f, effect.getVelocity(world, bulletType), inaccuracy);
	}

	@NotNull
	@Override
	public Entity getAsEntity() {
		return this;
	}

	@NotNull
	@Override
	public IProjectile getAsProjectile() {
		return this;
	}

	@Override
	protected void entityInit() {
		dataManager.register(CASTER_ID, -1);
		dataManager.register(BULLET_TYPE, (byte) 0);
		dataManager.register(BULLET_EFFECT, "");
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
			motionY = mY;
			motionZ = mZ;
		}

		if (!world.isRemote && ticksExisted >= 1000) {
			setDead();
		} else {
			if (world.isRemote) {
				List<EntityPlayer> entities = world.getEntities(EntityPlayer.class, input -> input != null && !(input.getDistanceSq(posX, posY, posZ) > 5 * 5));


				for (EntityPlayer player : entities) {
					world.playSound(player, getPosition(), ModSounds.BULLET_FLYBY, SoundCategory.PLAYERS, RandUtil.nextFloat(0.9f, 1.1f), RandUtil.nextFloat(0.95f, 1.1f));
					world.playSound(player, getPosition(), ModSounds.DUST_SPARKLE, SoundCategory.PLAYERS, RandUtil.nextFloat(0.1f, 0.3f), RandUtil.nextFloat(0.95f, 1.1f));
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
		if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
			IBlockState state = world.getBlockState(result.getBlockPos());
			if (state.getCollisionBoundingBox(world, result.getBlockPos()) != Block.NULL_AABB &&
					state.getBlock().canCollideCheck(state, false) &&
					ShotgunsAndGlitter.PROXY.collideBulletWithBlock(world, this, result.getBlockPos(),
							state, getEffect(), result.hitVec)) {

				PacketHandler.NETWORK.sendToAllAround(new PacketImpactSound(getPositionVector(), getEffect().getID()),
						new NetworkRegistry.TargetPoint(world.provider.getDimension(), posX, posY, posZ, 512));
				setDead();
				world.removeEntity(this);

			}
		} else if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
			if (result.entityHit != null && !world.isRemote)
				if (ShotgunsAndGlitter.PROXY.collideBulletWithEntity(world, this,
						result.entityHit, getEffect(), result.hitVec)) {

					PacketHandler.NETWORK.sendToAllAround(new PacketImpactSound(getPositionVector(), getEffect().getID()),
							new NetworkRegistry.TargetPoint(world.provider.getDimension(), posX, posY, posZ, 512));
					setDead();
					world.removeEntity(this);
				}
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setByte("bulletType", dataManager.get(BULLET_TYPE));
		compound.setString("bulletEffect", dataManager.get(BULLET_EFFECT));
		if (caster != null)
			compound.setString("caster", caster.getUniqueID().toString());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		dataManager.set(BULLET_TYPE, compound.getByte("bulletType"));
		dataManager.set(BULLET_EFFECT, compound.getString("bulletEffect"));

		if (compound.hasKey("caster"))
			caster = world.getPlayerEntityByUUID(UUID.fromString(compound.getString("caster")));
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
	public Effect getEffect() {
		return EffectRegistry.getEffectByID(dataManager.get(BULLET_EFFECT));
	}

	@Override
	public void setEffect(@NotNull Effect effect) {
		dataManager.set(BULLET_EFFECT, effect.getID());
	}

	@Override
	public int getCasterId() {
		return dataManager.get(CASTER_ID);
	}

	@Override
	public void setCasterId(int casterId) {
		dataManager.set(CASTER_ID, casterId);
	}
}
