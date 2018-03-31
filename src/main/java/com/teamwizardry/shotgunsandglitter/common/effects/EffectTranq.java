package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner;
import com.teamwizardry.librarianlib.features.particle.functions.InterpColorHSV;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.util.InterpScale;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.client.ClientEventHandler;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import com.teamwizardry.shotgunsandglitter.common.potions.ModPotions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class EffectTranq implements Effect {

	@Override
	public String getID() {
		return "tranq";
	}

	@Override
	public boolean onCollideEntity(@NotNull World world, @NotNull EntityBullet bullet, @NotNull Entity hitEntity, @NotNull RayTraceResult hit) {
		hitEntity.attackEntityFrom(DamageSource.causeThrownDamage(bullet, hitEntity), 0.0f);
		if (hitEntity instanceof EntityLivingBase && !world.isRemote)
			((EntityLivingBase) hitEntity).addPotionEffect(new PotionEffect(ModPotions.TRANQUILIZER, (int) (120 * bullet.getBulletType().damage)));
		return true;
	}

	@Override
	public void renderImpact(@NotNull World world, @NotNull EntityBullet bullet, @NotNull RayTraceResult hit) {

	}

	@Override
	public void renderUpdate(@NotNull World world, @NotNull EntityBullet bullet) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setCollision(true);
		glitter.disableMotionCalculation();
		glitter.disableRandom();

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionVector()), 1, 0, (i, build) -> {
			build.setLifetime(RandUtil.nextInt(10, 30));
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(0.2f, 0.5f), 0));
			build.setColorFunction(new InterpColorHSV(Color.GREEN, Color.CYAN));
			//build.setMotion(new Vec3d(bullet.motionX, bullet.motionY, bullet.motionZ));
		});
	}
}
