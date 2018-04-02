package com.teamwizardry.shotgunsandglitter.api;

import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner;
import com.teamwizardry.librarianlib.features.particle.functions.InterpFadeInOut;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.client.core.ClientEventHandler;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

/**
 * @author WireSegal
 * Created at 9:41 AM on 4/2/18.
 */
public class GrenadeEffectBasic implements GrenadeEffect {
	@Override
	public String getID() {
		return "basic";
	}

	@Override
	public float range(@NotNull World world, @NotNull IGrenadeEntity grenade) {
		return 30;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderUpdate(@NotNull World world, @NotNull IGrenadeEntity grenade) {

		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(InternalHandler.INTERNAL_HANDLER.getSparkle());
		glitter.setCollision(true);
		glitter.setScale(0.3f);
		glitter.setAlphaFunction(new InterpFadeInOut(0f, 1f));

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(grenade.getPositionAsVector()), 1);

	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderImpact(@NotNull World world, @NotNull IGrenadeEntity grenade) {

		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setCollision(true);
		glitter.setCanBounce(true);

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(grenade.getPositionAsVector()), 100, 0, (i, build) -> {
			build.setLifetime(RandUtil.nextInt(50, 100));
			build.setScale(RandUtil.nextFloat(0.2f, 3f));
			build.setAcceleration(new Vec3d(0, RandUtil.nextDouble(-0.05, -0.1), 0));

			build.setAlphaFunction(new InterpFadeInOut(0, RandUtil.nextFloat(0.5f, 1f)));
			double radius = 6 * RandUtil.nextFloat();
			double theta = 2.0f * (float) Math.PI * RandUtil.nextFloat();
			double r = radius * RandUtil.nextFloat();
			double x = r * MathHelper.cos((float) theta);
			double z = r * MathHelper.sin((float) theta);

			build.setMotion(new Vec3d(x, RandUtil.nextDouble(-radius, radius), z));
		});
	}
}
