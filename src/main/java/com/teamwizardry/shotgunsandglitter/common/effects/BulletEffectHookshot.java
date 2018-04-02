package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.features.math.interpolate.position.InterpLine;
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner;
import com.teamwizardry.librarianlib.features.particle.functions.InterpFadeInOut;
import com.teamwizardry.shotgunsandglitter.api.BulletEffect;
import com.teamwizardry.shotgunsandglitter.api.IBulletEntity;
import com.teamwizardry.shotgunsandglitter.api.util.InterpScale;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.client.core.ClientEventHandler;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BulletEffectHookshot implements BulletEffect {

	@Override
	public String getID() {
		return "hookshot";
	}

	@Override
	public void onImpact(@NotNull World world, @NotNull IBulletEntity bullet) {
		EntityLivingBase thrower = bullet.getThrower();
		if (thrower != null) {
			Vec3d differenceVec = bullet.getPositionVector().subtract(thrower.getPositionVector()).normalize().scale(0.5 * bullet.getBulletType().damage);
			thrower.motionX = differenceVec.x;
			thrower.motionY = Math.max(differenceVec.y, 0) + 0.5f;
			thrower.motionZ = differenceVec.z;
			thrower.velocityChanged = true;
		}
	}

	@Override
	public void renderImpact(@NotNull World world, @NotNull IBulletEntity bullet) {
		Vec3d position = bullet.getPositionVector();

		int throwerID = bullet.getCasterId();
		if (throwerID == -1) return;

		Entity thrower = world.getEntityByID(throwerID);
		if (thrower == null) return;

		Vec3d throwerPos = thrower.getPositionVector().addVector(0, thrower.getEyeHeight(), 0);

		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setAlphaFunction(new InterpFadeInOut(0.0f, 1f));

		glitter.setCollision(true);
		glitter.setCanBounce(true);

		ParticleSpawner.spawn(glitter, world, new InterpLine(throwerPos, position), 50, 0, (i, build) -> {
			build.setLifetime(RandUtil.nextInt(30, 50));
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(0.5f, 2), 0f));
			build.setAcceleration(new Vec3d(0, RandUtil.nextDouble(-0.005, -0.001), 0));
			build.setMotion(new Vec3d(
					RandUtil.nextDouble(-1, 1),
					RandUtil.nextDouble(-1, 1),
					RandUtil.nextDouble(-1, 1)
			));
		});
	}

	@Override
	public void renderUpdate(@NotNull World world, @NotNull IBulletEntity bullet) {
		Vec3d position = bullet.getPositionVector();

		ParticleBuilder glitter = new ParticleBuilder(50);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setAlphaFunction(new InterpFadeInOut(0.0f, 1f));

		glitter.setCollision(true);

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(position), 5, 0, (i, build) -> {
			build.setMotion(new Vec3d(bullet.motionX(), bullet.motionY(), bullet.motionZ()).scale(-1 / 10.0));
			build.setPositionOffset(new Vec3d(
					RandUtil.nextDouble(-0.1, 0.1),
					RandUtil.nextDouble(-0.1, 0.1),
					RandUtil.nextDouble(-0.1, 0.1)
			));
			build.setLifetime(RandUtil.nextInt(30, 60));
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(0.5f, 1), 0f));
			build.setAcceleration(new Vec3d(0, RandUtil.nextDouble(-0.005, -0.001), 0));
			build.setJitter(4, new Vec3d(
					RandUtil.nextDouble(-0.2, 0.2),
					RandUtil.nextDouble(-0.2, 0.2),
					RandUtil.nextDouble(-0.2, 0.2)
			));
		});
	}

	@Override
	public @Nullable SoundEvent getImpactSound() {
		return ModSounds.CHAINY_ZAP;
	}
}
