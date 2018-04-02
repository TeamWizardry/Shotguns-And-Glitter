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
public interface IGrenadeEntity {
	@NotNull
	Entity getAsEntity();

	@Nullable
	EntityLivingBase getThrower();

	@NotNull
	Vec3d getPositionVector();

	@NotNull
	BlockPos getPosition();

	double posX();

	double posY();

	double posZ();

	double motionX();

	double motionY();

	double motionZ();

	@NotNull
	GrenadeEffect getEffect();

	void setEffect(@NotNull GrenadeEffect bulletEffect);

	int getCasterId();

	void setCasterId(int casterId);
}
