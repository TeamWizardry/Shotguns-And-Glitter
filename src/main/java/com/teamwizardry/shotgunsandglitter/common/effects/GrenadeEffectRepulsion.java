package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner;
import com.teamwizardry.librarianlib.features.particle.functions.InterpFadeInOut;
import com.teamwizardry.shotgunsandglitter.api.GrenadeEffect;
import com.teamwizardry.shotgunsandglitter.api.IGrenadeEntity;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.client.core.ClientEventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * @author WireSegal
 * Created at 9:41 AM on 4/2/18.
 */
public class GrenadeEffectRepulsion implements GrenadeEffect {
	@Override
	public String getID() {
		return "repulsion";
	}

	@Override
	public float range(@NotNull World world, @NotNull IGrenadeEntity grenade) {
		return 15;
	}

	@Override
	public void hitEntity(@NotNull World world, @NotNull IGrenadeEntity grenade, @NotNull Entity entity, float intensity) {
		if (!world.isRemote) {
			Vec3d repulsion = entity.getPositionVector().add(0.0, entity.height / 2, 0.0).subtract(grenade.getPositionAsVector()).normalize().scale(intensity * 3);

			entity.motionX += repulsion.x;
			entity.motionY += repulsion.y + 0.5;
			entity.motionZ += repulsion.z;

			entity.velocityChanged = true;
		}

		entity.attackEntityFrom(DamageSource.GENERIC, 0.000001f);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderUpdate(@NotNull World world, @NotNull IGrenadeEntity grenade) {
		Color color = Color.RED, color2 = Color.ORANGE;

		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.getResource(world, grenade, ClientEventHandler.SPARKLE));
		glitter.setCollision(true);
		glitter.setCanBounce(true);
		glitter.disableMotionCalculation();
		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(grenade.getPositionAsVector()), 10, 0, (i, build) -> {
			build.setMotion(Vec3d.ZERO);
			build.setLifetime(RandUtil.nextInt(10, 20));
			build.setScale(RandUtil.nextFloat(0.2f, 1));

			build.setAlphaFunction(new InterpFadeInOut(0, RandUtil.nextFloat(0.5f, 1f)));
			double radius = RandUtil.nextDouble(0.1, 0.2);
			double theta = 2.0f * (float) Math.PI * RandUtil.nextFloat();
			double r = radius * RandUtil.nextFloat();
			double x = r * MathHelper.cos((float) theta);
			double z = r * MathHelper.sin((float) theta);
			build.addMotion(new Vec3d(x, RandUtil.nextDouble(-radius, radius), z));

			if (RandUtil.nextBoolean()) build.setColor(color);
			else build.setColor(color2);
		});
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderImpact(@NotNull World world, @NotNull IGrenadeEntity grenade) {
		Color color = Color.RED, color2 = Color.ORANGE;

		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.getResource(world, grenade, ClientEventHandler.SPARKLE));
		glitter.setCollision(true);
		glitter.setAcceleration(new Vec3d(0, RandUtil.nextDouble(-0.03, -0.04), 0));
		glitter.setCanBounce(true);
		glitter.enableMotionCalculation();

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(grenade.getPositionAsVector()), 100, 0, (i, build) -> {
			build.setMotion(Vec3d.ZERO);
			build.setLifetime(RandUtil.nextInt(50, 100));
			build.setScale(RandUtil.nextFloat(0.2f, 1));

			build.setAlphaFunction(new InterpFadeInOut(0, RandUtil.nextFloat(0.5f, 1f)));
			double radius = 3 * RandUtil.nextFloat();
			double theta = 2.0f * (float) Math.PI * RandUtil.nextFloat();
			double r = radius * RandUtil.nextFloat();
			double x = r * MathHelper.cos((float) theta);
			double z = r * MathHelper.sin((float) theta);
			build.addMotion(new Vec3d(x, RandUtil.nextDouble(0, radius), z));

			if (RandUtil.nextBoolean()) build.setColor(color);
			else build.setColor(color2);
		});
	}
}
