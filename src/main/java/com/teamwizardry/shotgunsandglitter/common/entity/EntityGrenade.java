package com.teamwizardry.shotgunsandglitter.common.entity;

import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.EffectRegistry;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

// TODO: replace Effect with GrenadeEffect
public class EntityGrenade extends EntityThrowable {

	private static final DataParameter<String> GRENADE_EFFECT = EntityDataManager.createKey(EntityGrenade.class, DataSerializers.STRING);
	private static final DataParameter<Integer> CASTER_ID = EntityDataManager.createKey(EntityGrenade.class, DataSerializers.VARINT);

	public EntityGrenade(@NotNull World world) {
		super(world);
		setSize(0.1F, 0.1F);
	}

	public EntityGrenade(@NotNull World world, Vec3d normal) {
		super(world);
		setSize(0.1F, 0.1F);

		//TODO: setEffect(effect);
		setCasterId(-1);

		shoot(normal.x, normal.y, normal.z, 1f, 1f);
	}

	public EntityGrenade(@NotNull World world, @NotNull EntityLivingBase caster) {
		super(world, caster);
		setSize(0.1F, 0.1F);

		//TODO: setEffect(effect);
		setCasterId(caster.getEntityId());

		shoot(caster, caster.rotationPitch, caster.rotationYaw, 0f, 1f, 1f);
	}

	@Override
	protected void entityInit() {
		dataManager.register(GRENADE_EFFECT, "");
		dataManager.register(CASTER_ID, -1);
	}

	@Override
	protected float getGravityVelocity() {
		return 0.03f;
	}

	@Override
	protected void onImpact(RayTraceResult result) {

	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (isDead) return;

		if (ticksExisted >= 50) {
			// TODO: go boom boom here

			setDead();
			world.removeEntity(this);
		} else {
			if (world.isRemote) {
				world.playSound(posX, posY, posZ, ModSounds.DUST_SPARKLE, SoundCategory.PLAYERS, RandUtil.nextFloat(0.1f, 0.3f), RandUtil.nextFloat(0.95f, 1.1f), false);
			}

			//TODO: PROXY RENDER CODE
			// ShotgunsAndGlitter.PROXY.updategrenadeEntity(world, this, getEffect());
		}
	}

	@Override
	public boolean isPushedByWater() {
		return false;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setString("grenadeEffect", dataManager.get(GRENADE_EFFECT));
		compound.setInteger("grenadeCaster", getCasterId());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		dataManager.set(GRENADE_EFFECT, compound.getString("grenadeEffect"));
		setCasterId(compound.getInteger("grenadeCaster"));
	}

	@NotNull
	public Effect getEffect() {
		return EffectRegistry.getEffectByID(dataManager.get(GRENADE_EFFECT));
	}

	public void setEffect(@NotNull Effect effect) {
		dataManager.set(GRENADE_EFFECT, effect.getID());
	}

	public int getCasterId() {
		return dataManager.get(CASTER_ID);
	}

	public void setCasterId(int casterId) {
		dataManager.set(CASTER_ID, casterId);
	}
}
