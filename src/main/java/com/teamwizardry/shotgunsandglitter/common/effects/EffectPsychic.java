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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class EffectPsychic implements Effect {

	@Override
	public String getID() {
		return "psychic";
	}

	@Override
	public void onUpdate(@NotNull World world, @NotNull EntityBullet bullet) {
		List<EntityLivingBase> targets = world.getEntitiesWithinAABB(EntityLivingBase.class,
				new AxisAlignedBB(bullet.posX - 10, bullet.posY - 10, bullet.posZ - 10,
						bullet.posX + 10, bullet.posY + 10, bullet.posZ + 10),
				(entity) -> {
					if (entity == null || !entity.isEntityAlive()) return false;
					Vec3d motionVec = new Vec3d(bullet.motionX, bullet.motionY, bullet.motionZ);
					Vec3d differenceVec = entity.getPositionVector().subtract(bullet.getPositionVector());
					double dot = motionVec.normalize().dotProduct(differenceVec.normalize());

					return differenceVec.lengthSquared() < 7.5 * 7.5 && dot >= 0.75;
				});

		Vec3d acceleration = Vec3d.ZERO;

		for (EntityLivingBase target : targets) {
			Vec3d differenceVec = target.getPositionVector().addVector(0, target.height, 0).subtract(bullet.getPositionVector());
			acceleration = acceleration.add(differenceVec.scale(Math.pow(differenceVec.lengthVector(), -3)));
		}

		acceleration = acceleration.normalize().scale(getVelocity(world, bullet.getBulletType()) * 0.75);

		bullet.motionX += acceleration.x;
		bullet.motionY += acceleration.y;
		bullet.motionZ += acceleration.z;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderImpact(@NotNull World world, @NotNull EntityBullet bullet) {
		Vec3d position = bullet.getPositionVector();

		ParticleBuilder glitter = new ParticleBuilder(30);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setAlphaFunction(new InterpFadeInOut(0.0f, 1f));
		glitter.setCollision(true);
		glitter.setCanBounce(true);

		glitter.setColor(new Color(0x730089));

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(position), 100, 0, (i, build) -> {
			double radius = RandUtil.nextDouble(0.1, 1);
			double theta = 2.0f * (float) Math.PI * RandUtil.nextFloat();
			double r = radius * RandUtil.nextFloat();
			double x = r * MathHelper.cos((float) theta);
			double z = r * MathHelper.sin((float) theta);
			build.setLifetime(RandUtil.nextInt(50, 100));
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(0.2f, 1f), 0f));
			build.setMotion(new Vec3d(x, RandUtil.nextDouble(-1, 1), z));

			build.setTick(particle -> {
				if (particle.getAge() >= particle.getLifetime() / 30) {

					particle.setVelocity(particle.getVelocity().add(bullet.getPositionVector().subtract(particle.getPos()).normalize().scale(1 / 15.0)));
					particle.setAcceleration(Vec3d.ZERO);
				} else {
					particle.setAcceleration(new Vec3d(0, -0.05, 0));
				}
			});
		});
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderUpdate(@NotNull World world, @NotNull EntityBullet bullet) {
		ParticleBuilder builder = new ParticleBuilder(50);
		builder.setRender(ClientEventHandler.SPARKLE);
		builder.disableRandom();

		builder.setColor(new Color(0x730089));

		float size = 0.5f;
		ParticleSpawner.spawn(builder, world, new StaticInterp<>(bullet.getPositionVector()), 2, 0, (i, particleBuilder) ->
		{
			particleBuilder.setAlphaFunction(new InterpScale(1f, 0f));
			particleBuilder.setScaleFunction(new InterpScale(size, 0.3f));
		});
		double scatter = 0.1f;
		ParticleSpawner.spawn(builder, world, new StaticInterp<>(bullet.getPositionVector()), 3, 0, (i, particleBuilder) ->
		{
			particleBuilder.setAlphaFunction(new InterpScale(1f, 0f));
			particleBuilder.setScaleFunction(new InterpScale(size, 0.3f));
			Vec3d offset = new Vec3d(
					RandUtil.nextDouble(-scatter, scatter),
					RandUtil.nextDouble(-scatter, scatter),
					RandUtil.nextDouble(-scatter, scatter));
			particleBuilder.setPositionOffset(offset);

		});
		ParticleSpawner.spawn(builder, world, new StaticInterp<>(bullet.getPositionVector()), 4, 0, (i, particleBuilder) -> {
			particleBuilder.setAlphaFunction(new InterpScale(1f, 0f));
			particleBuilder.setScaleFunction(new InterpScale(size, 0.3f));
			Vec3d offset = new Vec3d(
					RandUtil.nextDouble(-scatter, scatter),
					RandUtil.nextDouble(-scatter, scatter),
					RandUtil.nextDouble(-scatter, scatter));
			particleBuilder.setPositionOffset(offset);
		});
	}
}
