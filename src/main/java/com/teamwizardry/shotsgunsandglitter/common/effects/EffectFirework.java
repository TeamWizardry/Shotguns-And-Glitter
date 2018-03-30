package com.teamwizardry.shotsgunsandglitter.common.effects;

import com.teamwizardry.shotsgunsandglitter.api.Effect;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EffectFirework extends Effect {

	@Override
	public String getID() {
		return "effect_firework";
	}

	@Override
	public void onCollideEntity(@Nonnull World world, @Nonnull Entity entity) {

	}

	@Override
	public void onCollideEntityRender(@Nonnull World world, @Nonnull Entity entity) {

	}

	@Override
	public void onCollideBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {

	}

	@Override
	public void onCollideBlockRender(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {

	}

	@Override
	public void renderTrail(@Nonnull World world, @Nonnull Vec3d position) {

	}
}
