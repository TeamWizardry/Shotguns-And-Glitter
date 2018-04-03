package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.features.math.interpolate.position.InterpBezier3D;
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner;
import com.teamwizardry.librarianlib.features.particle.functions.InterpColorHSV;
import com.teamwizardry.librarianlib.features.particle.functions.InterpFadeInOut;
import com.teamwizardry.shotgunsandglitter.api.GrenadeEffect;
import com.teamwizardry.shotgunsandglitter.api.IGrenadeEntity;
import com.teamwizardry.shotgunsandglitter.api.LingerObject;
import com.teamwizardry.shotgunsandglitter.api.util.InterpScale;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.client.core.ClientEventHandler;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import com.teamwizardry.shotgunsandglitter.common.potions.ModPotions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
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
public class GrenadeEffectDisco implements GrenadeEffect {

	private float hue = 0;

	@Override
	public String getID() {
		return "disco";
	}

	@Override
	public void onImpact(@NotNull World world, @NotNull IGrenadeEntity grenade) {
		GrenadeEffect.super.onImpact(world, grenade);

		if (world.isRemote)
			world.playSound(grenade.posX(), grenade.posY(), grenade.posZ(), ModSounds.DISCO, SoundCategory.PLAYERS, 4f, 1f, false);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderImpact(@NotNull World world, @NotNull IGrenadeEntity grenade) {

		ClientEventHandler.lingerObjects.add(new LingerObject(world, grenade.getPositionAsVector(), 200, lingering -> {

			ParticleBuilder glitter = new ParticleBuilder(10);
			glitter.setRender(ClientEventHandler.SPARKLE);
			glitter.setCollision(true);
			glitter.setCanBounce(true);

			ParticleSpawner.spawn(glitter, lingering.world, new StaticInterp<>(lingering.pos), 3, 0, (i, build) -> {
				if (hue >= 1) hue = 0;
				else hue += 0.1;
				build.setLifetime(RandUtil.nextInt(20, 50));
				build.setColorFunction(new InterpColorHSV(Color.getHSBColor(RandUtil.nextFloat(), 1, 1), Color.getHSBColor(hue, 1, 1)));
				build.setAlphaFunction(new InterpFadeInOut(0f, 1f));
				build.setScaleFunction(new InterpScale(RandUtil.nextFloat(0.2f, 0.6f), 0));
				build.setAcceleration(new Vec3d(0, -0.1, 0));

				double radius = 1 * RandUtil.nextFloat();
				double theta = 2.0f * (float) Math.PI * RandUtil.nextFloat();
				double x = radius * MathHelper.cos((float) theta);
				double z = radius * MathHelper.sin((float) theta);
				build.setPositionOffset(new Vec3d(x, RandUtil.nextDouble(-1, 1), z));

				build.setMotion(new Vec3d(x, RandUtil.nextDouble(0, 1), z));
			});

			ParticleSpawner.spawn(glitter, lingering.world, new StaticInterp<>(lingering.pos), 1, 0, (i, build) -> {
				if (hue >= 1) hue = 0;
				else hue += 0.1;
				build.setLifetime(RandUtil.nextInt(50, 100));
				build.setColorFunction(new InterpColorHSV(Color.getHSBColor(RandUtil.nextFloat(), 1, 1), Color.getHSBColor(hue, 1, 1)));
				build.setAlphaFunction(new InterpFadeInOut(1f, 1f));
				build.setScaleFunction(new InterpScale(RandUtil.nextFloat(1f, 3f), 0));
				build.setAcceleration(new Vec3d(0, -0.1, 0));

				double radius = 8 * RandUtil.nextFloat();
				double theta = 2.0f * (float) Math.PI * RandUtil.nextFloat();
				double x = radius * MathHelper.cos((float) theta);
				double z = radius * MathHelper.sin((float) theta);
				build.setPositionOffset(new Vec3d(x, RandUtil.nextDouble(3, 20), z));

				build.setMotion(new Vec3d(x / 6.0, 0, z / 6.0));
			});
		}));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderUpdate(@NotNull World world, @NotNull IGrenadeEntity grenade) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setCollision(true);

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(grenade.getPositionAsVector()), RandUtil.nextInt(1, 5), 0, (i, build) -> {
			if (hue >= 1) hue = 0;
			else hue += 0.1;
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(0.5f, 1f), 0));
			build.setLifetime(RandUtil.nextInt(50, 100));
			build.setColorFunction(new InterpColorHSV(Color.getHSBColor(RandUtil.nextFloat(), 1, 1), Color.getHSBColor(hue, 1, 1)));
		});

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(grenade.getPositionAsVector()), 5, 0, (i, build) -> {
			if (hue >= 1) hue = 0;
			else hue += 0.1;
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(0.2f, 0.5f), 0));
			build.setLifetime(RandUtil.nextInt(40, 80));
			build.setColorFunction(new InterpColorHSV(Color.getHSBColor(RandUtil.nextFloat(), 1, 1), Color.getHSBColor(hue, 1, 1)));
			build.setPositionFunction(new InterpBezier3D(Vec3d.ZERO,
					new Vec3d(
							RandUtil.nextDouble(-5, 5),
							RandUtil.nextDouble(-5, 5),
							RandUtil.nextDouble(-5, 5)
					),
					new Vec3d(
							RandUtil.nextDouble(-5, 5),
							RandUtil.nextDouble(-5, 5),
							RandUtil.nextDouble(-5, 5)
					),
					new Vec3d(
							RandUtil.nextDouble(-5, 5),
							RandUtil.nextDouble(-5, 5),
							RandUtil.nextDouble(-5, 5)
					)));
		});
	}

	@Override
	public void hitEntity(@NotNull World world, @NotNull IGrenadeEntity grenade, @NotNull Entity entity, float intensity) {
		if (entity instanceof EntityLivingBase) {
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(ModPotions.DISCO, 200));
		}
	}

	@Override
	public float range(@NotNull World world, @NotNull IGrenadeEntity grenade) {
		return 16;
	}
}
