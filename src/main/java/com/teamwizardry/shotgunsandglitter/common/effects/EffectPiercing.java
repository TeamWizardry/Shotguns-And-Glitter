package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.features.math.interpolate.position.InterpCircle;
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.util.InterpScale;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.client.core.ClientEventHandler;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import com.teamwizardry.shotgunsandglitter.api.IBulletEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class EffectPiercing implements Effect {

	@Override
	public String getID() {
		return "piercing";
	}

	@Override
	public boolean onCollideEntity(@NotNull World world, @NotNull IBulletEntity bullet, @NotNull Entity hitEntity) {
		Effect.super.onCollideEntity(world, bullet, hitEntity);
		return false; // Piercing
	}

	@Override
	public void renderImpact(@NotNull World world, @NotNull IBulletEntity bullet) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setCollision(true);
		glitter.setCanBounce(true);
		glitter.disableMotionCalculation();
		glitter.setColor(Color.YELLOW);

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionVector()), 50, 0, (i, build) -> {
			build.setLifetime(RandUtil.nextInt(10, 30));
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(0.8f, 1.4f), 0));

			build.setMotion(new Vec3d(
					RandUtil.nextDouble(-0.3, 0.3),
					RandUtil.nextDouble(-0.3, 0.6),
					RandUtil.nextDouble(-0.3, 0.3)
			));
		});
	}

	@Override
	public void renderUpdate(@NotNull World world, @NotNull IBulletEntity bullet) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setCollision(true);
		glitter.setCanBounce(true);
		glitter.disableMotionCalculation();
		glitter.setColor(Color.YELLOW);

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionVector()), 15, 0, (i, build) -> {
			build.setLifetime(RandUtil.nextInt(10, 30));
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(0.1f, 0.5f), 0));

			Vec3d position = new InterpCircle(Vec3d.ZERO, new Vec3d(bullet.motionX(), bullet.motionY(), bullet.motionZ()).normalize(), 1, 50, 0).get(RandUtil.nextFloat());
			build.setMotion(position.scale(1.0 / 10.0));
		});
	}

	@Override
	public @Nullable SoundEvent getImpactSound() {
		return ModSounds.POP;
	}
}
