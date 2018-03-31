package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.features.math.interpolate.position.InterpLine;
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner;
import com.teamwizardry.librarianlib.features.particle.functions.InterpFadeInOut;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.util.InterpScale;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.client.ClientEventHandler;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class EffectHookshot implements Effect {

	@Override
	public String getID() {
		return "hookshot";
	}

	@Override
	public void onImpact(@NotNull World world, @NotNull EntityBullet bullet, @NotNull RayTraceResult hit) {
		EntityLivingBase thrower = bullet.getThrower();
		if (thrower != null) {
			Vec3d differenceVec = bullet.getPositionVector().subtract(thrower.getPositionVector());
			thrower.motionX = differenceVec.x * 2f;
			thrower.motionY = Math.max(differenceVec.y, 0) * 2f + 0.5f;
			thrower.motionZ = differenceVec.z * 2f;
			thrower.velocityChanged = true;
		}
	}

	// TODO: check effect once thrower nullity is fixed
	@Override
	public void renderImpact(@NotNull World world, @NotNull EntityBullet bullet, @NotNull RayTraceResult hit) {
		Vec3d position = bullet.getPositionVector();
		EntityLivingBase thrower = bullet.getThrower();
		if (thrower == null) return;
		Vec3d throwerPos = thrower.getPositionVector().addVector(0, thrower.getEyeHeight(), 0);

		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRenderNormalLayer(ClientEventHandler.SPARKLE);
		glitter.setAlphaFunction(new InterpFadeInOut(0.0f, 1f));

		glitter.setColor(Color.GRAY);
		glitter.setCollision(true);
		glitter.setCanBounce(true);

		ParticleSpawner.spawn(glitter, world, new InterpLine(throwerPos, position), 20, 0, (i, build) -> {
			build.setLifetime(RandUtil.nextInt(10, 20));
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(0.5f, 3), 0f));
			build.setAcceleration(new Vec3d(0, RandUtil.nextDouble(-0.005, -0.001), 0));
			build.setMotion(new Vec3d(
					RandUtil.nextDouble(-1, 1),
					RandUtil.nextDouble(-1, 1),
					RandUtil.nextDouble(-1, 1)
			));
		});
	}

	@Override
	public void renderUpdate(@NotNull World world, @NotNull EntityBullet bullet) {
		Vec3d position = bullet.getPositionVector();

		ParticleBuilder glitter = new ParticleBuilder(50);
		glitter.setRenderNormalLayer(ClientEventHandler.SPARKLE);
		glitter.setAlphaFunction(new InterpFadeInOut(0.0f, 1f));

		glitter.setColor(Color.GRAY);
		glitter.setCollision(true);

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(position), 10, 0, (i, build) -> {
			build.setMotion(new Vec3d(bullet.motionX, bullet.motionY, bullet.motionZ).scale(-1 / 2.0));
			build.setPositionOffset(new Vec3d(
					RandUtil.nextDouble(-0.1, 0.1),
					RandUtil.nextDouble(-0.1, 0.1),
					RandUtil.nextDouble(-0.1, 0.1)
			));
			build.setLifetime(RandUtil.nextInt(30, 60));
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(0.5f, 3), 0f));
			build.setAcceleration(new Vec3d(0, RandUtil.nextDouble(-0.005, -0.001), 0));
		});
	}
}
