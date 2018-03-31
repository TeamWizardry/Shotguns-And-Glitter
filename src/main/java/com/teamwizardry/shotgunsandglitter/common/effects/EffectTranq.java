package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner;
import com.teamwizardry.librarianlib.features.particle.functions.InterpColorHSV;
import com.teamwizardry.librarianlib.features.particle.functions.InterpFadeInOut;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.util.InterpScale;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.client.core.ClientEventHandler;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import com.teamwizardry.shotgunsandglitter.common.potions.ModPotions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class EffectTranq implements Effect {

	@Override
	public String getID() {
		return "tranq";
	}

	@Override
	public boolean onCollideEntity(@NotNull World world, @NotNull EntityBullet bullet, @NotNull Entity hitEntity) {
		hitEntity.attackEntityFrom(DamageSource.causeThrownDamage(bullet, hitEntity), 0.0f);
		if (hitEntity instanceof EntityLivingBase && !world.isRemote)
			((EntityLivingBase) hitEntity).addPotionEffect(new PotionEffect(ModPotions.TRANQUILIZER, (int) (120 * bullet.getBulletType().damage)));
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderImpact(@NotNull World world, @NotNull EntityBullet bullet) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.disableMotionCalculation();
		glitter.setCollision(true);

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionVector()), 100, 0, (i, build) -> {
			build.setLifetime(RandUtil.nextInt(20, 40));
			build.setColorFunction(new InterpColorHSV(Color.GREEN, Color.CYAN));
			build.setAlphaFunction(new InterpFadeInOut(0f, 1f));
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(0.2f, 0.8f), 0));
			build.setDeceleration(new Vec3d(0.7, 0.7, 0.7));

			double radius = 2 * RandUtil.nextFloat();
			double theta = 2.0f * (float) Math.PI * RandUtil.nextFloat();
			double x = radius * MathHelper.cos((float) theta);
			double z = radius * MathHelper.sin((float) theta);

			build.setMotion(new Vec3d(x, RandUtil.nextDouble(-2, 2), z));
		});
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderUpdate(@NotNull World world, @NotNull EntityBullet bullet) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setCollision(true);
		glitter.disableMotionCalculation();
		glitter.disableRandom();

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionVector()), 1, 0, (i, build) -> {
			build.setScaleFunction(new InterpScale(0.5f, 0));
			build.setColorFunction(new InterpColorHSV(Color.GREEN, Color.CYAN));
		});
	}
}
