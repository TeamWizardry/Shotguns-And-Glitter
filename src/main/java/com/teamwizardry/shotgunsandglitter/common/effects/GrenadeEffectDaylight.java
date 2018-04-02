package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.features.math.interpolate.position.InterpBezier3D;
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner;
import com.teamwizardry.librarianlib.features.particle.functions.InterpColorHSV;
import com.teamwizardry.librarianlib.features.particle.functions.InterpFadeInOut;
import com.teamwizardry.shotgunsandglitter.api.GrenadeEffect;
import com.teamwizardry.shotgunsandglitter.api.IGrenadeEntity;
import com.teamwizardry.shotgunsandglitter.api.util.InterpScale;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.client.core.ClientEventHandler;
import com.teamwizardry.shotgunsandglitter.common.potions.ModPotions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

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
			float radius = range(world, grenade) * 10 / 3;
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

	@SideOnly(Side.CLIENT)
	@Override
	public void renderUpdate(@NotNull World world, @NotNull IGrenadeEntity grenade) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setCollision(true);

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(grenade.getPositionAsVector()), 1, 0, (i, build) -> {
			build.setLifetime(RandUtil.nextInt(5, 20));
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(1f, 3f), 0));
			build.setColorFunction(new InterpColorHSV(new Color(0xc1ffec), new Color(0xfffac1)));
			build.setMotion(new Vec3d(grenade.motionX(), grenade.motionY(), grenade.motionZ()));
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

	@SideOnly(Side.CLIENT)
	@Override
	public void renderImpact(@NotNull World world, @NotNull IGrenadeEntity grenade) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setCollision(true);
		glitter.setCanBounce(true);

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(grenade.getPositionAsVector()), 100, 0, (i, build) -> {
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
			build.setMotion(new Vec3d(x, RandUtil.nextDouble(0, radius), z));
		});
	}
}
