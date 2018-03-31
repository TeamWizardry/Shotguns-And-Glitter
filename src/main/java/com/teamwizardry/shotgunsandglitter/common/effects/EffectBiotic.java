package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.features.math.interpolate.position.InterpCircle;
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.util.InterpScale;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.client.ClientEventHandler;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class EffectBiotic implements Effect {

	@Override
	public String getID() {
		return "biotic";
	}

	@Override
	public boolean onCollideEntity(@NotNull World world, @NotNull EntityBullet bullet, @NotNull Entity hitEntity, @NotNull RayTraceResult hit) {
		hitEntity.attackEntityFrom(DamageSource.causeThrownDamage(bullet, hitEntity), 0.0f);
		if (hitEntity instanceof EntityLivingBase)
			((EntityLivingBase) hitEntity).addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 20, bullet.getBulletType().ordinal()));
		return true;
	}

	@Override
	public void renderImpact(@NotNull World world, @NotNull EntityBullet bullet, @NotNull RayTraceResult hit) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setCollision(true);
		glitter.setCanBounce(true);
		glitter.disableMotionCalculation();
		glitter.setColor(Color.RED);

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionVector()), 15, 10, (i, build) -> {
			build.setLifetime(RandUtil.nextInt(5, 10));
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(0.1f, 0.5f), 0));

			Vec3d position = new InterpCircle(Vec3d.ZERO, new Vec3d(bullet.motionX, bullet.motionY, bullet.motionZ).normalize(), RandUtil.nextFloat(0.2f, 0.5f), 50, 0).get(RandUtil.nextFloat());
			build.setPositionOffset(position);
			build.setMotion(position.scale(-1));
		});
	}

	@Override
	public void renderUpdate(@NotNull World world, @NotNull EntityBullet bullet) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setCollision(true);
		glitter.setCanBounce(true);
		glitter.disableMotionCalculation();
		glitter.setColor(Color.RED);

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionVector()), 15, 0, (i, build) -> {
			build.setLifetime(RandUtil.nextInt(10, 30));
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(0.2f, 0.5f), 0));

			build.setMotion(new Vec3d(bullet.motionX, bullet.motionY, bullet.motionZ).scale(1.0 / 2.0));
		});
	}
}
