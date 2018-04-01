package com.teamwizardry.shotgunsandglitter.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author WireSegal
 * Created at 8:29 AM on 4/1/18.
 */
public interface IBulletEntity {
	@NotNull
	Entity getAsEntity();

	@Nullable
	EntityLivingBase getThrower();

	@NotNull
	Vec3d getPositionVector();

	@NotNull
	BlockPos getPosition();

	@NotNull
	BulletType getBulletType();

	double posX();

	double posY();

	double posZ();

	double motionX();

	double motionY();

	double motionZ();

	void setBulletType(@NotNull BulletType type);

	@NotNull
	Effect getEffect();

	void setEffect(@NotNull Effect effect);

	int getCasterId();

	void setCasterId(int casterId);

	BlockPos getOrigin();

	void setOrigin(BlockPos pos);

	float getPotency();

	void setPotency(float potency);
}
