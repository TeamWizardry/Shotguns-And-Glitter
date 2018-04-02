package com.teamwizardry.shotgunsandglitter.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

public interface GrenadeEffect {

	// Effect ID

	String getID();

	// Logic Methods

	default void onImpact(@NotNull World world, @NotNull IGrenadeEntity grenade) {
		if (!world.isRemote) {
			float range = range(world, grenade);
			for (EntityLivingBase target : world.getEntitiesWithinAABB(EntityLivingBase.class,
					new AxisAlignedBB(grenade.posX() - range, grenade.posY() - range, grenade.posZ() - range,
							grenade.posX() + range, grenade.posY() + range, grenade.posZ() + range),
					(entity) -> {
						if (entity == null || !entity.isEntityAlive()) return false;
						Vec3d differenceVec = entity.getPositionVector().subtract(grenade.getPositionAsVector());
						return differenceVec.lengthSquared() < range * range;
					})) {
				Vec3d difference = target.getPositionVector().subtract(grenade.getPositionAsVector());
				hitEntity(world, grenade, target, intensity(world, grenade, (float) difference.lengthVector()));
			}

			if (doExplosionParticles(world, grenade)) {
				Explosion explosion = new Explosion(world, grenade.getAsEntity(),
						grenade.posX(), grenade.posY(), grenade.posZ(),
						3f, false, false);
				explosion.doExplosionB(true);
			}
		}
	}

	default boolean doExplosionParticles(@NotNull World world, @NotNull IGrenadeEntity grenade) {
		return true;
	}

	default void hitEntity(@NotNull World world, @NotNull IGrenadeEntity grenade, @NotNull Entity entity, float intensity) {
		entity.attackEntityFrom(DamageSource.causeThrownDamage(grenade.getAsEntity(), grenade.getEntityThrower()),
				damage(world, grenade, intensity));
	}

	default void onUpdate(@NotNull World world, @NotNull IGrenadeEntity grenade) {
		// NO-OP
	}

	default float lowerIntensityBound(@NotNull World world, @NotNull IGrenadeEntity grenade) {
		return 0.1f;
	}

	default float intensity(@NotNull World world, @NotNull IGrenadeEntity grenade, float distance) {
		float lower = lowerIntensityBound(world, grenade);
		if (lower == 1)
			return 1f;

		float cf = -lower / (lower - 1);
		float d = distance / range(world, grenade);
		return 1 - (d / (d + cf));
	}

	default float range(@NotNull World world, @NotNull IGrenadeEntity grenade) {
		return 7.5f;
	}

	default float damage(@NotNull World world, @NotNull IGrenadeEntity grenade, float intensity) {
		return 5f * intensity;
	}

	// Render Methods

	@SideOnly(Side.CLIENT)
	default void renderImpact(@NotNull World world, @NotNull IGrenadeEntity grenade) {
		// NO-OP
	}

	@SideOnly(Side.CLIENT)
	default void renderUpdate(@NotNull World world, @NotNull IGrenadeEntity grenade) {
		// NO-OP
	}
}
