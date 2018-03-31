package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EffectPsychic implements Effect {

	@Override
	public String getID() {
		return "psychic";
	}

	@Override
	public void onUpdate(@NotNull World world, @NotNull EntityBullet bullet) {
		List<EntityLivingBase> targets = world.getEntitiesWithinAABB(EntityLivingBase.class,
				new AxisAlignedBB(bullet.posX - 10, bullet.posY - 10, bullet.posZ - 10,
						bullet.posX + 10, bullet.posY + 10, bullet.posZ + 10),
				(entity) -> {
					if (entity == null || !entity.isEntityAlive()) return false;
					Vec3d motionVec = new Vec3d(bullet.motionX, bullet.motionY, bullet.motionZ);
					Vec3d differenceVec = entity.getPositionVector().subtract(bullet.getPositionVector());
					double dot = motionVec.normalize().dotProduct(differenceVec.normalize());

					return differenceVec.lengthSquared() < 7.5 * 7.5 && dot >= 0.75;
				});

		Vec3d acceleration = Vec3d.ZERO;

		for (EntityLivingBase target : targets) {
			Vec3d differenceVec = target.getPositionVector().subtract(bullet.getPositionVector());
			acceleration = acceleration.add(differenceVec.scale(1 / differenceVec.lengthSquared()));
		}

		acceleration = acceleration.normalize().scale(0.05);

		bullet.motionX += acceleration.x;
		bullet.motionY += acceleration.y;
		bullet.motionZ += acceleration.z;
	}
}
