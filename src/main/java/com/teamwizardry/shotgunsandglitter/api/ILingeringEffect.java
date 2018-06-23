package com.teamwizardry.shotgunsandglitter.api;

import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

public interface ILingeringEffect {

	void runLingeringEffect(@NotNull World world, @NotNull LingeringObject lingeringObject);

	@SideOnly(Side.CLIENT)
	void renderLingeringEffect(@NotNull World world, @NotNull LingeringObject lingeringObject);
}
