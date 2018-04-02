package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.shotgunsandglitter.api.GrenadeEffect;
import com.teamwizardry.shotgunsandglitter.api.IGrenadeEntity;
import com.teamwizardry.shotgunsandglitter.common.potions.ModPotions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

/**
 * @author WireSegal
 * Created at 9:41 AM on 4/2/18.
 */
public class GrenadeEffectDaylight implements GrenadeEffect {
	@Override
	public String getID() {
		return "daylight";
	}

	@Override
	public float range(@NotNull World world, @NotNull IGrenadeEntity grenade) {
		return 30;
	}

	@Override
	public float damage(@NotNull World world, @NotNull IGrenadeEntity grenade, float intensity) {
		return intensity * 2f;
	}

	@Override
	public boolean doExplosionParticles(@NotNull World world, @NotNull IGrenadeEntity grenade) {
		return false;
	}

	@Override
	public void onImpact(@NotNull World world, @NotNull IGrenadeEntity grenade) {
		GrenadeEffect.super.onImpact(world, grenade);

		if (!world.isRemote) {
			float radius = range(world, grenade);
			for (EntityLivingBase target : world.getEntitiesWithinAABB(EntityLivingBase.class,
					new AxisAlignedBB(grenade.posX() - radius, grenade.posY() - radius, grenade.posZ() - radius,
							grenade.posX() + radius, grenade.posY() + radius, grenade.posZ() + radius),
					(entity) -> {
						if (entity == null || !entity.isEntityAlive()) return false;
						Vec3d look = entity.getLook(0f);
						Vec3d differenceVec = grenade.getPositionAsVector().subtract(entity.getPositionVector());
						double dot = look.dotProduct(differenceVec.normalize());

						return differenceVec.lengthSquared() <= radius * radius && dot >= 0;
					})) {

				Vec3d look = target.getLook(0f);
				Vec3d differenceVec = grenade.getPositionAsVector().subtract(target.getPositionVector());
				double dot = look.dotProduct(differenceVec.normalize());
				double lengthIntensity = Math.min(1 / differenceVec.lengthVector(), 1 / 10.0) * dot;
				int amp = (int) (100 * lengthIntensity / 3 + 1.0 / 6);
				int duration = (int) (400.0 / 9 + lengthIntensity * 50000 / 9);

				target.addPotionEffect(new PotionEffect(ModPotions.FLASH, duration, amp));
			}

			world.newExplosion(grenade.getAsEntity(), grenade.posX(), grenade.posY(), grenade.posZ(), 3, true, false);
		}

	}
}
