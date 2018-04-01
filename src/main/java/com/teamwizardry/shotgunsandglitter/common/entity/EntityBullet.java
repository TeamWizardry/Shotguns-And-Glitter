package com.teamwizardry.shotgunsandglitter.common.entity;

import com.teamwizardry.librarianlib.features.network.PacketHandler;
import com.teamwizardry.shotgunsandglitter.ShotgunsAndGlitter;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.EffectRegistry;
import com.teamwizardry.shotgunsandglitter.common.network.PacketImpactSound;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class EntityBullet extends EntityThrowable {

	private static final DataParameter<Byte> BULLET_TYPE = EntityDataManager.createKey(EntityBullet.class, DataSerializers.BYTE);
	private static final DataParameter<String> BULLET_EFFECT = EntityDataManager.createKey(EntityBullet.class, DataSerializers.STRING);

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

		shoot(normal.x, normal.y, normal.z, effect.getVelocity(world, bulletType), inaccuracy);
	}

	public EntityBullet(@NotNull World world, @NotNull EntityLivingBase caster, @NotNull BulletType bulletType, @NotNull Effect effect, float inaccuracy) {
		super(world, caster);
		setSize(0.1F, 0.1F);

		setBulletType(bulletType);
		setEffect(effect);

		this.caster = caster;
		shoot(caster, caster.rotationPitch, caster.rotationYaw, 0f, effect.getVelocity(world, bulletType), inaccuracy);
	}

	@Override
	protected void entityInit() {
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

		if (!world.isRemote && ticksExisted > 1000) {
			setDead();
			world.removeEntity(this);
		} else
			ShotgunsAndGlitter.PROXY.updateBulletEntity(world, this, getEffect());
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

	public BulletType getBulletType() {
		return BulletType.byOrdinal(dataManager.get(BULLET_TYPE));
	}

	public void setBulletType(BulletType type) {
		dataManager.set(BULLET_TYPE, (byte) type.ordinal());
	}

	public Effect getEffect() {
		return EffectRegistry.getEffectByID(dataManager.get(BULLET_EFFECT));
	}

	public void setEffect(Effect effect) {
		dataManager.set(BULLET_EFFECT, effect.getID());
	}
}
