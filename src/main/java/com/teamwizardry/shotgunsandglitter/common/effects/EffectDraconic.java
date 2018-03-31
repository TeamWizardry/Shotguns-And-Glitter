package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class EffectDraconic implements Effect {

	@Override
	public String getID() {
		return "draconic";
	}

	@Override
	public void onImpact(@NotNull World world, @NotNull EntityBullet bullet) {
		world.newExplosion(bullet, bullet.posX, bullet.posY, bullet.posZ, bullet.getBulletType().damage, true, true);
	}

	@Override
	public void onUpdate(@NotNull World world, @NotNull EntityBullet bullet) {
		if (!world.isRemote) {
			for (EntityLivingBase target : world.getEntitiesWithinAABB(EntityLivingBase.class,
					new AxisAlignedBB(bullet.posX - 4, bullet.posY - 4, bullet.posZ - 4,
							bullet.posX + 4, bullet.posY + 4, bullet.posZ + 4),
					(entity) -> {
						if (entity == null || !entity.isEntityAlive()) return false;
						Vec3d differenceVec = entity.getPositionVector().subtract(bullet.getPositionVector());
						return differenceVec.lengthSquared() < 4 * 4;
					})) {
				target.setFire(10);
			}
		}
	}
}
