package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner;
import com.teamwizardry.librarianlib.features.particle.functions.InterpFadeInOut;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.util.InterpScale;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.client.core.ClientEventHandler;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class EffectTainted implements Effect {

	@Override
	public String getID() {
		return "tainted";
	}

	@Override
	public boolean onCollideEntity(@NotNull World world, @NotNull EntityBullet bullet, @NotNull Entity hitEntity) {
		Effect.super.onCollideEntity(world, bullet, hitEntity);
		if (hitEntity instanceof EntityLivingBase && !world.isRemote)
			((EntityLivingBase) hitEntity).addPotionEffect(new PotionEffect(MobEffects.POISON, 300, bullet.getBulletType().ordinal()));
		return true;
	}

	@Override
	public boolean onCollideBlock(@NotNull World world, @NotNull EntityBullet bullet, BlockPos pos, @NotNull IBlockState state) {
		if (!world.isRemote) {
			EntityAreaEffectCloud aEC = new EntityAreaEffectCloud(world, bullet.posX, bullet.posY, bullet.posZ);

			aEC.setOwner(bullet.getThrower());
			aEC.setRadius(3.0F);
			aEC.setRadiusOnUse(-0.25F);
			aEC.setWaitTime(10);
			aEC.setRadiusPerTick(-aEC.getRadius() / aEC.getDuration());
			aEC.setPotion(PotionTypes.LONG_POISON);

			world.spawnEntity(aEC);
		}
		return true;
	}

	@Override
	public void renderImpact(@NotNull World world, @NotNull EntityBullet bullet) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setCollision(true);
		glitter.setCanBounce(true);
		glitter.setAlphaFunction(new InterpFadeInOut(0f, 1f));

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionVector()), 30, 0, (i, build) -> {
			build.setScaleFunction(new InterpScale(0.5f, 1f));
			build.setLifetime(RandUtil.nextInt(20, 40));
			build.setColor(RandUtil.nextBoolean() ? Color.GREEN : new Color(0xb2ff00));
			build.setMotion(new Vec3d(
					RandUtil.nextDouble(-0.1, 0.1),
					RandUtil.nextDouble(-0.3, 0.3),
					RandUtil.nextDouble(-0.1, 0.1)
			));
		});

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionVector()), 100, 0, (i, build) -> {
			build.setScaleFunction(new InterpScale(0.5f, 1f));
			build.setLifetime(RandUtil.nextInt(20, 40));
			build.setColor(RandUtil.nextBoolean() ? Color.GREEN : new Color(0xb2ff00));
			build.setDeceleration(new Vec3d(0.95, 0.95, 0.95));

			double radius = 0.5 * RandUtil.nextFloat();
			double theta = 2.0f * (float) Math.PI * RandUtil.nextFloat();
			double x = radius * MathHelper.cos((float) theta);
			double z = radius * MathHelper.sin((float) theta);

			build.setMotion(new Vec3d(x, RandUtil.nextDouble(0.05, 0.15), z));
		});
	}

	@Override
	public void renderUpdate(@NotNull World world, @NotNull EntityBullet bullet) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setCollision(true);
		glitter.setAlphaFunction(new InterpFadeInOut(0f, 1f));

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionVector()), 1, 0, (i, build) -> {
			build.setScaleFunction(new InterpScale(0.5f, 1f));
			build.setLifetime(RandUtil.nextInt(20, 40));
			build.setAcceleration(new Vec3d(0, RandUtil.nextDouble(-0.01, -0.05), 0));
			build.setColor(RandUtil.nextBoolean() ? Color.GREEN : new Color(0xb2ff00));
		});

		if (RandUtil.nextInt(5) == 0) {
			ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionVector()), 10, 0, (i, build) -> {
				build.setScaleFunction(new InterpScale(0.5f, 1f));
				build.setLifetime(RandUtil.nextInt(20, 40));
				build.setColor(RandUtil.nextBoolean() ? Color.GREEN : new Color(0xb2ff00));
				build.setDeceleration(new Vec3d(0.9, 0.9, 0.9));

				build.addMotion(new Vec3d(
						RandUtil.nextDouble(-0.2, 0.2),
						RandUtil.nextDouble(-0.2, 0.2),
						RandUtil.nextDouble(-0.2, 0.2)
				));
			});
		}
	}
}
