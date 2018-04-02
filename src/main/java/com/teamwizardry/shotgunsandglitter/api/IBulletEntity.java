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
	EntityLivingBase getEntityThrower();

	@NotNull
	Vec3d getPositionAsVector();

	@NotNull
	BlockPos getPositionAsBlockPos();

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
	BulletEffect getEffect();

	void setEffect(@NotNull BulletEffect bulletEffect);

	int getCasterId();

	void setCasterId(int casterId);

	BlockPos getOrigin();

	void setOrigin(BlockPos pos);

	float getPotency();

	void setPotency(float potency);

	default double getBulletDistanceSq() {
		BlockPos origin = getOrigin();
		Vec3d pos = getPositionAsVector();
		return pos.squareDistanceTo(origin.getX(), origin.getY(), origin.getZ());
	}

	default float getFalloff() {
		//falloff =
		double dist = getBulletDistanceSq();
		float potency = getPotency();
		return dist < potency ? 0 : (float) ((dist - potency * potency) / (64 * 64 - potency * potency));
	}
}
