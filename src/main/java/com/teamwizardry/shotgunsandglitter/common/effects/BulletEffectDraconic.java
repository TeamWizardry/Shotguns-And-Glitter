package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.features.math.interpolate.position.InterpBezier3D;
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner;
import com.teamwizardry.librarianlib.features.particle.functions.InterpColorHSV;
import com.teamwizardry.shotgunsandglitter.api.BulletEffect;
import com.teamwizardry.shotgunsandglitter.api.IBulletEntity;
import com.teamwizardry.shotgunsandglitter.api.util.InterpScale;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.client.core.ClientEventHandler;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class BulletEffectDraconic implements BulletEffect {

	@Override
	public String getID() {
		return "draconic";
	}

	@Override
	public void onImpact(@NotNull World world, @NotNull IBulletEntity bullet) {
		if (!world.isRemote)
			world.newExplosion(bullet.getAsEntity(),
					bullet.posX(), bullet.posY(), bullet.posZ(),
					(float) bullet.getBulletType().getExplosiveRadius(), true, !world.getGameRules().getBoolean("mobGriefing"));
	}

	@Override
	public void onUpdate(@NotNull World world, @NotNull IBulletEntity bullet) {
		if (!world.isRemote) {
			for (EntityLivingBase target : world.getEntitiesWithinAABB(EntityLivingBase.class,
					new AxisAlignedBB(bullet.posX() - 4, bullet.posY() - 4, bullet.posZ() - 4,
							bullet.posX() + 4, bullet.posY() + 4, bullet.posZ() + 4),
					(entity) -> {
						if (entity == null || !entity.isEntityAlive() || entity == bullet.getEntityThrower()) return false;
						Vec3d differenceVec = entity.getPositionVector().subtract(bullet.getPositionAsVector());
						return differenceVec.lengthSquared() < 4 * 4;
					})) {
				target.setFire(10);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderImpact(@NotNull World world, @NotNull IBulletEntity bullet) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.getResource(world, bullet, ClientEventHandler.SPARKLE));
		glitter.setCollision(true);
		glitter.setCanBounce(true);

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionAsVector()), 50, 0, (i, build) -> {
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(0.5f, 2), 0));
			build.setLifetime(RandUtil.nextInt(50, 100));
			build.setColorFunction(new InterpColorHSV(RandUtil.nextBoolean() ? Color.RED : Color.ORANGE, RandUtil.nextBoolean() ? Color.RED : Color.WHITE));
			build.setDeceleration(new Vec3d(0.6, 0.6, 0.6));

			double radius = 3 * RandUtil.nextFloat();
			double theta = 2.0f * (float) Math.PI * RandUtil.nextFloat();
			double x = radius * MathHelper.cos((float) theta);
			double z = radius * MathHelper.sin((float) theta);

			build.setMotion(new Vec3d(x, RandUtil.nextDouble(0.1, 3), z));

			build.setJitter(3, new Vec3d(
					RandUtil.nextDouble(-0.4, 0.4),
					RandUtil.nextDouble(-0.4, 0.4),
					RandUtil.nextDouble(-0.4, 0.4)
			));
		});
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderUpdate(@NotNull World world, @NotNull IBulletEntity bullet) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.getResource(world, bullet, ClientEventHandler.SPARKLE));
		glitter.setCollision(true);

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionAsVector()), RandUtil.nextInt(1, 5), 0, (i, build) -> {
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(0.5f, 1f), 0));
			build.setLifetime(RandUtil.nextInt(50, 100));
			build.setColorFunction(new InterpColorHSV(Color.WHITE, Color.RED));
		});

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionAsVector()), 5, 0, (i, build) -> {
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(0.2f, 0.5f), 0));
			build.setLifetime(RandUtil.nextInt(40, 80));
			build.setColorFunction(new InterpColorHSV(Color.RED, Color.WHITE));
			build.setPositionFunction(new InterpBezier3D(Vec3d.ZERO,
					new Vec3d(
							RandUtil.nextDouble(-5, 5),
							RandUtil.nextDouble(-5, 5),
							RandUtil.nextDouble(-5, 5)
					),
					new Vec3d(
							RandUtil.nextDouble(-5, 5),
							RandUtil.nextDouble(-5, 5),
							RandUtil.nextDouble(-5, 5)
					),
					new Vec3d(
							RandUtil.nextDouble(-5, 5),
							RandUtil.nextDouble(-5, 5),
							RandUtil.nextDouble(-5, 5)
					)));
		});
	}

	@Override
	public @Nullable SoundEvent getImpactSound() {
		return ModSounds.FIREBALL;
	}

}
