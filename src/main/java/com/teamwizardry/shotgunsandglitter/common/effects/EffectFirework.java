package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner;
import com.teamwizardry.librarianlib.features.particle.functions.InterpFadeInOut;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtilSeed;
import com.teamwizardry.shotgunsandglitter.client.ClientEventHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.awt.*;

public class EffectFirework extends Effect {

	@Override
	public String getID() {
		return "effect_firework";
	}

	@Override
	public void onCollideEntity(@Nonnull World world, @Nonnull Entity entity) {

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onCollideEntityRender(@Nonnull World world, @Nonnull Entity entity) {

	}

	@Override
	public void onCollideBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onCollideBlockRender(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderTrail(@Nonnull World world, @Nonnull Vec3d position) {
		ParticleBuilder glitter = new ParticleBuilder(1000);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setAlphaFunction(new InterpFadeInOut(0.0f, 0.3f));

		RandUtilSeed seed = new RandUtilSeed(glitter.hashCode());
		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(position), 1, 0, (i, build) -> {
			build.setColor(new Color(seed.nextInt(100, 255), seed.nextInt(100, 255), seed.nextInt(100, 255), 255));
			build.setScale((float) RandUtil.nextDouble(0.3, 0.5));
			build.addMotion(new Vec3d(
					RandUtil.nextDouble(-0.03, 0.03),
					RandUtil.nextDouble(-0.03, 0.03),
					RandUtil.nextDouble(-0.03, 0.03)
			));
		});
	}
}
