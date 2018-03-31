package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class EffectGraviton implements Effect {

	private final int direction;
	private final String idSuffix;

	public EffectGraviton(boolean outwards) {
		direction = outwards ? 1 : -1;
		idSuffix = outwards ? "out" : "in";
	}

	@Override
	public String getID() {
		return "gravity_" + idSuffix;
	}

	// The visual effect of the Graviton should be random particles rushing into the central point.

	@Override
	public void onImpact(@NotNull World world, @NotNull EntityBullet bullet, @NotNull RayTraceResult hit) {
		for (EntityLivingBase target : world.getEntitiesWithinAABB(EntityLivingBase.class,
				new AxisAlignedBB(bullet.posX - 10, bullet.posY - 10, bullet.posZ - 10,
						bullet.posX + 10, bullet.posY + 10, bullet.posZ + 10),
				(entity) -> {
					if (entity == null || !entity.isEntityAlive()) return false;
					Vec3d differenceVec = entity.getPositionVector().subtract(bullet.getPositionVector());
					return differenceVec.lengthSquared() < 7.5 * 7.5;
				})) {
			Vec3d differenceVec = bullet.getPositionVector().subtract(target.getPositionVector());
			differenceVec = differenceVec.scale(direction / differenceVec.lengthSquared());
			target.motionX = differenceVec.x * 2f;
			target.motionY = Math.max(differenceVec.y, 0) * 2f + 0.5f;
			target.motionZ = differenceVec.z * 2f;
			target.velocityChanged = true;
		}
	}
}
