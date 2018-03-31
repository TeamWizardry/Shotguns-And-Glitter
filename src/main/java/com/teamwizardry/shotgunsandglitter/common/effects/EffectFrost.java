package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import com.teamwizardry.shotgunsandglitter.common.potions.ModPotions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class EffectFrost implements Effect {

	@Override
	public String getID() {
		return "frost";
	}

	@Override
	public boolean onCollideEntity(@NotNull World world, @NotNull EntityBullet bullet, @NotNull Entity hitEntity) {
		Effect.super.onCollideEntity(world, bullet, hitEntity);
		if (hitEntity instanceof EntityLivingBase && !world.isRemote)
			((EntityLivingBase) hitEntity).addPotionEffect(new PotionEffect(ModPotions.FROST, 300, bullet.getBulletType().ordinal() + 1));
		return true;
	}

	@Override
	public void onImpact(@NotNull World world, @NotNull EntityBullet bullet) {
		if (!world.isRemote) {
			for (EntityLivingBase target : world.getEntitiesWithinAABB(EntityLivingBase.class,
					new AxisAlignedBB(bullet.posX - 10, bullet.posY - 10, bullet.posZ - 10,
							bullet.posX + 10, bullet.posY + 10, bullet.posZ + 10),
					(entity) -> {
						if (entity == null || !entity.isEntityAlive()) return false;
						Vec3d differenceVec = entity.getPositionVector().subtract(bullet.getPositionVector());
						return differenceVec.lengthSquared() < 7.5 * 7.5 && differenceVec.lengthSquared() != 0;
					})) {
				Vec3d difference = target.getPositionVector().subtract(bullet.getPositionVector());
				double struckStrength = Math.min(1 / difference.lengthVector(), 1 / 3.0);
				int duration = (int) (1400 - struckStrength * 1000 / 6);
				target.addPotionEffect(new PotionEffect(ModPotions.FROST, duration, bullet.getBulletType().ordinal()));
			}
		}
	}

	@Override
	public void renderImpact(@NotNull World world, @NotNull EntityBullet bullet) {

	}

	@Override
	public void renderUpdate(@NotNull World world, @NotNull EntityBullet bullet) {

	}
}
