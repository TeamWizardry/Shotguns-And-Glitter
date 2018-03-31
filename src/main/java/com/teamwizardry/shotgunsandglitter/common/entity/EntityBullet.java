package com.teamwizardry.shotgunsandglitter.common.entity;

import com.teamwizardry.shotgunsandglitter.ShotgunsAndGlitter;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.EffectRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class EntityBullet extends EntityThrowable {

	private static final DataParameter<Byte> BULLET_TYPE = EntityDataManager.createKey(EntityBullet.class, DataSerializers.BYTE);
	private static final DataParameter<String> BULLET_EFFECT = EntityDataManager.createKey(EntityBullet.class, DataSerializers.STRING);

	public EntityBullet(@Nonnull World world) {
		super(world);
		setSize(0.1F, 0.1F);
	}

	public EntityBullet(@Nonnull World world, @Nonnull EntityLivingBase caster, @Nonnull BulletType bulletType, @Nonnull Effect effect, float inaccuracy) {
		super(world, caster);
		setSize(0.1F, 0.1F);

		setBulletType(bulletType);
		setEffect(effect);

		shoot(caster, caster.rotationPitch, caster.rotationYaw, 0f, effect.getVelocity(world, caster, bulletType), inaccuracy);
	}

	@Override
	protected void entityInit() {
		dataManager.register(BULLET_TYPE, (byte) 0);
		dataManager.register(BULLET_EFFECT, "");
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (isDead) return;

		if (!world.isRemote && ticksExisted > 1000)
			setDead();
		else
			ShotgunsAndGlitter.PROXY.updateBulletEntity(world, this, getEffect());
	}

	@Override
	public boolean isPushedByWater() {
		return false;
	}

	@Override
	protected void onImpact(@NotNull RayTraceResult result) {
		if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
			if (!ShotgunsAndGlitter.PROXY.collideBulletWithBlock(world, this,
					result, world.getBlockState(result.getBlockPos()), getEffect()))
				setDead();
		} else if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
			if (!ShotgunsAndGlitter.PROXY.collideBulletWithEntity(world, this,
					result.entityHit, result, getEffect()))
				setDead();
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setByte("bulletType", dataManager.get(BULLET_TYPE));
		compound.setString("bulletEffect", dataManager.get(BULLET_EFFECT));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		dataManager.set(BULLET_TYPE, compound.getByte("bulletType"));
		dataManager.set(BULLET_EFFECT, compound.getString("bulletEffect"));
	}

	public BulletType getBulletType() {
		return BulletType.values()[dataManager.get(BULLET_TYPE) % BulletType.values().length];
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
