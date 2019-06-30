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
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.awt.*;

public class BulletEffectAttractive implements BulletEffect {
	@Override
	public String getID() {
		return "attractive";
	}

	@Override
	public boolean onCollideEntity(@NotNull World world, @NotNull IBulletEntity bullet, @NotNull Entity hitEntity) {
		if (hitEntity instanceof EntityAnimal) {
			((EntityAnimal) hitEntity).setInLove(bullet.getEntityThrower() instanceof EntityPlayer ? (EntityPlayer) bullet.getEntityThrower() : null);
			return true;
		}
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderImpact(@NotNull World world, @NotNull IBulletEntity bullet) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.getResource(world, bullet, ClientEventHandler.HEART));
		glitter.disableMotionCalculation();
		glitter.setCollision(true);

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionAsVector()), 100, 0, (i, build) -> {
			build.setLifetime(RandUtil.nextInt(20, 40));
			build.setColorFunction(new InterpColorHSV(Color.RED, Color.CYAN));
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

	@SideOnly(Side.CLIENT)
	@Override
	public void renderUpdate(@NotNull World world, @NotNull IBulletEntity bullet) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.getResource(world, bullet, ClientEventHandler.HEART));
		glitter.setCollision(true);
		glitter.disableMotionCalculation();
		glitter.disableRandom();

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionAsVector()), 1, 0, (i, build) -> {
			build.setScaleFunction(new InterpScale(0.5f, 0));
			build.setColorFunction(new InterpColorHSV(Color.GREEN, Color.CYAN));
		});
	}

	@Override
	public @Nullable
	SoundEvent getImpactSound() {
		return RandUtil.nextInt(100000) == 0 ? ModSounds.BONE : ModSounds.HEAL_TWINKLE;
	}
}
