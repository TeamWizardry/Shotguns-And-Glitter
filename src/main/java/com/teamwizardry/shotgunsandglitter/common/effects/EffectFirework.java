package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner;
import com.teamwizardry.librarianlib.features.particle.functions.InterpFadeInOut;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtilSeed;
import com.teamwizardry.shotgunsandglitter.client.ClientEventHandler;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import net.minecraft.block.state.IBlockState;
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
		return "effect_firework";
	}

	@Override
	public boolean onCollideBlock(@NotNull World world, @NotNull EntityBullet bullet, @NotNull RayTraceResult pos, @NotNull IBlockState state) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderUpdate(@NotNull World world, @NotNull EntityBullet bullet) {
		Vec3d position = bullet.getPositionVector();

		ParticleBuilder glitter = new ParticleBuilder(50);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setAlphaFunction(new InterpFadeInOut(0.0f, 0.3f));

		RandUtilSeed seed = new RandUtilSeed(glitter.hashCode());
		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(position), 3, 0, (i, build) -> {
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
			ParticleSpawner.spawn(glitter, world, new StaticInterp<>(position), 1, 0, (i, build) -> {
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
