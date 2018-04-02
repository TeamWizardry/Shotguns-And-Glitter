package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class BulletEffectBiotic implements BulletEffect {

	@Override
	public String getID() {
		return "biotic";
	}

	@Override
	public boolean onCollideEntity(@NotNull World world, @NotNull IBulletEntity bullet, @NotNull Entity hitEntity) {
		hitEntity.attackEntityFrom(DamageSource.GENERIC, 0.000001f);
		if (hitEntity instanceof EntityLivingBase && !world.isRemote)
			((EntityLivingBase) hitEntity).addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 20, bullet.getBulletType().ordinal()));
		return true;
	}

	@Override
	public void renderImpact(@NotNull World world, @NotNull IBulletEntity bullet) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setCanBounce(true);
		glitter.disableMotionCalculation();

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionAsVector()), (int) (50 * bullet.getFalloff()), 0, (i, build) -> {
			build.setLifetime(RandUtil.nextInt(20, 40));
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(0.4f, 1f), 0));
			build.setColorFunction(new InterpColorHSV(Color.RED, Color.YELLOW));
			build.setAlphaFunction(new InterpFadeInOut(1f, 1f));

			double radius = 2;
			double theta = 2.0f * (float) Math.PI * RandUtil.nextFloat();
			double r = radius * RandUtil.nextFloat();
			double x = r * MathHelper.cos((float) theta);
			double z = r * MathHelper.sin((float) theta);

			Vec3d offset = new Vec3d(x, RandUtil.nextDouble(-1, 1), z);
			build.setPositionOffset(offset);
			build.setMotion(offset.scale(-1 / 10.0));
		});
	}

	@Override
	public void renderUpdate(@NotNull World world, @NotNull IBulletEntity bullet) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setCollision(true);
		glitter.setCanBounce(true);
		glitter.disableMotionCalculation();
		glitter.setColor(Color.RED);

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionAsVector()), 15, 0, (i, build) -> {
			build.setLifetime(RandUtil.nextInt(10, 30));
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(0.2f, 0.5f), 0));
			build.setColorFunction(new InterpColorHSV(Color.RED, Color.YELLOW));
		});
	}

	@Override
	public @Nullable SoundEvent getImpactSound() {
		return ModSounds.HEAL;
	}
}
