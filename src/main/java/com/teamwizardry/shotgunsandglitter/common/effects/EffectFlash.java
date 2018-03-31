package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import com.teamwizardry.shotgunsandglitter.common.potions.ModPotions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class EffectFlash implements Effect {

	@Override
	public String getID() {
		return "flash";
	}

	@Override
	public void onImpact(@NotNull World world, @NotNull EntityBullet bullet, @NotNull RayTraceResult hit) {
		if (!world.isRemote) {
			int radius = (int) bullet.getBulletType().damage * 50;
			for (EntityLivingBase target : world.getEntitiesWithinAABB(EntityLivingBase.class,
					new AxisAlignedBB(bullet.posX - radius, bullet.posY - radius, bullet.posZ - radius,
							bullet.posX + radius, bullet.posY + radius, bullet.posZ + radius),
					(entity) -> {
						if (entity == null || !entity.isEntityAlive()) return false;
						Vec3d motionVec = new Vec3d(bullet.motionX, bullet.motionY, bullet.motionZ);
						Vec3d differenceVec = entity.getPositionVector().subtract(bullet.getPositionVector());
						double dot = motionVec.normalize().dotProduct(differenceVec.normalize());

						return differenceVec.lengthSquared() <= radius*radius && dot >= 0;
					})) {

				Vec3d motionVec = new Vec3d(bullet.motionX, bullet.motionY, bullet.motionZ);
				Vec3d differenceVec = target.getPositionVector().subtract(bullet.getPositionVector());
				double dot = motionVec.normalize().dotProduct(differenceVec.normalize());
				double lengthIntensity = Math.min(1 / differenceVec.lengthVector(), 1 / 10.0) * dot * 100 / radius;
				int amp = (int) (100.0 / 3 - lengthIntensity / 3);
				int duration = (int) (400.0 / 9 - lengthIntensity * 50000 / 9);

				target.addPotionEffect(new PotionEffect(ModPotions.FLASH, duration, amp));
			}
		}
	}
}