package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.features.math.interpolate.position.InterpBezier3D;
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner;
import com.teamwizardry.librarianlib.features.particle.functions.InterpColorHSV;
import com.teamwizardry.librarianlib.features.particle.functions.InterpFadeInOut;
import com.teamwizardry.shotgunsandglitter.api.BulletEffect;
import com.teamwizardry.shotgunsandglitter.api.IBulletEntity;
import com.teamwizardry.shotgunsandglitter.api.util.InterpScale;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.client.core.ClientEventHandler;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import com.teamwizardry.shotgunsandglitter.common.potions.ModPotions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class BulletEffectFlash implements BulletEffect {

	@Override
	public String getID() {
		return "flash";
	}

	@Override
	public void onImpact(@NotNull World world, @NotNull IBulletEntity bullet) {
		if (!world.isRemote) {
			int radius = (int) bullet.getBulletType().damage * 50;
			for (EntityLivingBase target : world.getEntitiesWithinAABB(EntityLivingBase.class,
					new AxisAlignedBB(bullet.posX() - radius, bullet.posY() - radius, bullet.posZ() - radius,
							bullet.posX() + radius, bullet.posY() + radius, bullet.posZ() + radius),
					(entity) -> {
						if (entity == null || !entity.isEntityAlive()) return false;
						Vec3d look = entity.getLook(0f);
						Vec3d differenceVec = bullet.getPositionAsVector().subtract(entity.getPositionVector());
						double dot = look.dotProduct(differenceVec.normalize());

						return differenceVec.lengthSquared() <= radius * radius && dot >= 0;
					})) {

				Vec3d look = target.getLook(0f);
				Vec3d differenceVec = bullet.getPositionAsVector().subtract(target.getPositionVector());
				double dot = look.dotProduct(differenceVec.normalize());
				double lengthIntensity = Math.min(1 / differenceVec.lengthVector(), 1 / 10.0) * dot;
				int amp = (int) (100 * lengthIntensity / 3 + 1.0 / 6);
				int duration = (int) (400.0 / 9 + lengthIntensity * 50000 / 9);

				target.addPotionEffect(new PotionEffect(ModPotions.FLASH, duration, amp));
			}
		}
	}

	@Override
	public void renderImpact(@NotNull World world, @NotNull IBulletEntity bullet) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setCollision(true);
		glitter.setCanBounce(true);

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionAsVector()), 100, 0, (i, build) -> {
			build.setLifetime(RandUtil.nextInt(50, 100));
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(1f, 3f), 0));
			build.setColorFunction(new InterpColorHSV(new Color(0xc1ffec), new Color(0xfffac1)));
			build.setAcceleration(new Vec3d(0, RandUtil.nextDouble(-0.05, -0.1), 0));
			build.setAlphaFunction(new InterpFadeInOut(0, 1f));

			build.setDeceleration(new Vec3d(0.9, 0.9, 0.9));

			double radius = RandUtil.nextDouble(3, 5);
			double theta = 2.0f * (float) Math.PI * RandUtil.nextFloat();
			double r = radius * RandUtil.nextFloat();
			double x = r * MathHelper.cos((float) theta);
			double z = r * MathHelper.sin((float) theta);
			build.setMotion(new Vec3d(x, RandUtil.nextDouble(-radius, radius), z));
		});
	}

	@Override
	public void renderUpdate(@NotNull World world, @NotNull IBulletEntity bullet) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setCollision(true);

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionAsVector()), 1, 0, (i, build) -> {
			build.setLifetime(RandUtil.nextInt(5, 20));
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(1f, 3f), 0));
			build.setColorFunction(new InterpColorHSV(new Color(0xc1ffec), new Color(0xfffac1)));
			build.setMotion(new Vec3d(bullet.motionX(), bullet.motionY(), bullet.motionZ()));
			build.setPositionFunction(new InterpBezier3D(Vec3d.ZERO,
					new Vec3d(
							RandUtil.nextDouble(-0.2, 0.2),
							RandUtil.nextDouble(-0.2, 0.2),
							RandUtil.nextDouble(-0.2, 0.2)
					),
					new Vec3d(
							RandUtil.nextDouble(-1, 1),
							RandUtil.nextDouble(-1, 1),
							RandUtil.nextDouble(-1, 1)
					),
					new Vec3d(
							RandUtil.nextDouble(-1, 1),
							RandUtil.nextDouble(-1, 1),
							RandUtil.nextDouble(-1, 1)
					)));
		});
	}

	@Override
	public @Nullable SoundEvent getImpactSound() {
		return ModSounds.SMOKE_BLAST;
	}
}
