package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner;
import com.teamwizardry.librarianlib.features.particle.functions.InterpFadeInOut;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.util.InterpScale;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtilSeed;
import com.teamwizardry.shotgunsandglitter.client.ClientEventHandler;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class EffectFirework implements Effect {

	@Override
	public String getID() {
		return "firework";
	}

	@Override
	public void renderImpact(@NotNull World world, @NotNull EntityBullet bullet, @NotNull RayTraceResult hit) {
		Vec3d position = bullet.getPositionVector();

		ParticleBuilder glitter = new ParticleBuilder(50);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setAlphaFunction(new InterpFadeInOut(0f, 1f));
		glitter.setCollision(true);
		glitter.setCanBounce(true);

		RandUtilSeed seed = new RandUtilSeed(glitter.hashCode());

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(position), 100, 0, (i, build) -> {
			build.setDeceleration(new Vec3d(
					RandUtil.nextDouble(0.8, 1),
					RandUtil.nextDouble(0.8, 1),
					RandUtil.nextDouble(0.8, 1)
			));
			//build.setAcceleration(new Vec3d(0, RandUtil.nextDouble(-0.05, -0.1), 0));
			build.setLifetime(RandUtil.nextInt(50, 100));
			build.setColor(Color.getHSBColor(seed.nextFloat(), 1f, 1f));
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(1, 3), RandUtil.nextFloat(0, 1f)));

			double radius = 1;
			double theta = 2.0f * (float) Math.PI * RandUtil.nextFloat();
			double r = radius * RandUtil.nextFloat();
			double x = r * MathHelper.cos((float) theta);
			double z = r * MathHelper.sin((float) theta);

			build.setMotion(new Vec3d(x, RandUtil.nextDouble(-1, 2), z));
			build.setJitter(10, new Vec3d(
					RandUtil.nextDouble(-0.1, 0.1),
					RandUtil.nextDouble(-0.1, 0.1),
					RandUtil.nextDouble(-0.1, 0.1)
			));
		});

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(position), 200, 0, (i, build) -> {
			build.setDeceleration(new Vec3d(
					RandUtil.nextDouble(0.8, 1),
					RandUtil.nextDouble(0.8, 1),
					RandUtil.nextDouble(0.8, 1)
			));
			//build.setAcceleration(new Vec3d(0, RandUtil.nextDouble(-0.05, -0.1), 0));
			build.setLifetime(RandUtil.nextInt(100, 300));
			build.setColor(Color.getHSBColor(seed.nextFloat(), 1f, 1f));
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(1, 3), RandUtil.nextFloat(0, 1f)));

			double radius = 3;
			double theta = 2.0f * (float) Math.PI * RandUtil.nextFloat();
			double r = radius * RandUtil.nextFloat();
			double x = r * MathHelper.cos((float) theta);
			double z = r * MathHelper.sin((float) theta);

			build.setMotion(new Vec3d(x, RandUtil.nextDouble(-2, 3), z));
			build.setJitter(10, new Vec3d(
					RandUtil.nextDouble(-0.1, 0.1),
					RandUtil.nextDouble(-0.1, 0.1),
					RandUtil.nextDouble(-0.1, 0.1)
			));
		});
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderUpdate(@NotNull World world, @NotNull EntityBullet bullet) {
		Vec3d position = bullet.getPositionVector();

		ParticleBuilder glitter = new ParticleBuilder(50);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setAlphaFunction(new InterpFadeInOut(0.0f, 0.3f));
		glitter.setCollision(true);
		glitter.setCanBounce(true);

		RandUtilSeed seed = new RandUtilSeed(glitter.hashCode());

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(position), 10, 0, (i, build) -> {
			build.setLifetime(RandUtil.nextInt(5, 10));
			build.setColor(Color.getHSBColor(seed.nextFloat(), 1f, 1f));
			build.setScale((float) RandUtil.nextDouble(0.4, 0.7));

			Vec3d motion = new Vec3d(
					RandUtil.nextDouble(-0.05, 0.05),
					RandUtil.nextDouble(-0.05, 0.05),
					RandUtil.nextDouble(-0.05, 0.05));
			build.addMotion(motion);
			build.setJitter(3, new Vec3d(
					RandUtil.nextDouble(-0.05, 0.05),
					RandUtil.nextDouble(-0.05, 0.05),
					RandUtil.nextDouble(-0.05, 0.05)
			));
		});

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(position), 10, 0, (i, build) -> {
			build.setLifetime(RandUtil.nextInt(5, 20));
			build.setColor(Color.getHSBColor(seed.nextFloat(), 1f, 1f));
			build.setScale((float) RandUtil.nextDouble(0.3, 1));

			Vec3d motion = new Vec3d(
					RandUtil.nextDouble(-0.1, 0.1),
					RandUtil.nextDouble(-0.1, 0.1),
					RandUtil.nextDouble(-0.1, 0.1));
			build.addMotion(motion);
			build.addAcceleration(new Vec3d(motion.x * -0.01, 0, motion.z * -0.01));
			build.setJitter(10, new Vec3d(
					RandUtil.nextDouble(-0.1, 0.1),
					RandUtil.nextDouble(-0.1, 0.1),
					RandUtil.nextDouble(-0.1, 0.1)
			));
		});

		if (RandUtil.nextInt(4) == 0)
			ParticleSpawner.spawn(glitter, world, new StaticInterp<>(position), 5, 0, (i, build) -> {
				build.setLifetime(RandUtil.nextInt(50, 80));
				build.setColor(Color.getHSBColor(seed.nextFloat(), 1f, 1f));
				build.setScale((float) RandUtil.nextDouble(1, 2));

				Vec3d motion = new Vec3d(
						RandUtil.nextDouble(-0.4, 0.4),
						RandUtil.nextDouble(-0.4, 0.4),
						RandUtil.nextDouble(-0.4, 0.4));
				build.addMotion(motion);
				build.addAcceleration(new Vec3d(motion.x * -0.01, RandUtil.nextDouble(-0.005, -0.01), motion.z * -0.01));
				build.setJitter(5, new Vec3d(
						RandUtil.nextDouble(-0.2, 0.2),
						RandUtil.nextDouble(-0.2, 0.2),
						RandUtil.nextDouble(-0.2, 0.2)
				));
			});
	}
}
