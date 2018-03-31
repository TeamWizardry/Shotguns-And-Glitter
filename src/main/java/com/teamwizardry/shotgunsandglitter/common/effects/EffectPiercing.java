package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class EffectPiercing implements Effect {

	@Override
	public String getID() {
		return "piercing";
	}

	@Override
	public boolean onCollideEntity(@NotNull World world, @NotNull EntityBullet bullet, @NotNull Entity hitEntity, @NotNull RayTraceResult hit) {
		Effect.super.onCollideEntity(world, bullet, hitEntity, hit);
		return true; // Piercing
	}

	@Override
	public void renderImpact(@NotNull World world, @NotNull EntityBullet bullet, @NotNull RayTraceResult hit) {

	}

	@Override
	public void renderUpdate(@NotNull World world, @NotNull EntityBullet bullet) {

	}
}
