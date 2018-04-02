package com.teamwizardry.shotgunsandglitter.api;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.function.Consumer;

public class LingerObject {

	public final World world;
	public final Vec3d pos;
	public final int ticks;
	public final Consumer<LingerObject> consumer;
	public final long lastTime;

	public LingerObject(World world, Vec3d pos, int ticks, Consumer<LingerObject> consumer) {
		this.world = world;
		this.pos = pos;
		this.ticks = ticks;
		this.consumer = consumer;
		this.lastTime = world.getTotalWorldTime();
	}
}
