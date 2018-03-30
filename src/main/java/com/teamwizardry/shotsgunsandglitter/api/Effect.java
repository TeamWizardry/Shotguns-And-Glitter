package com.teamwizardry.shotsgunsandglitter.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public abstract class Effect {

	public Effect() {
	}

	public abstract String getID();

	public abstract void onCollideEntity(@Nonnull World world, @Nonnull Entity entity);

	public abstract void onCollideEntityRender(@Nonnull World world, @Nonnull Entity entity);

	@SideOnly(Side.CLIENT)
	public abstract void onCollideBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state);

	@SideOnly(Side.CLIENT)
	public abstract void onCollideBlockRender(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state);
}
