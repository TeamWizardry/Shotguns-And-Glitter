package com.teamwizardry.shotgunsandglitter.api;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class BulletImpactEvent extends Event {

	private final World world;
	private final IBulletEntity bullet;
	private final RayTraceResult result;

	public BulletImpactEvent(World world, IBulletEntity bullet, RayTraceResult result) {
		this.world = world;
		this.bullet = bullet;
		this.result = result;
	}

	public World getWorld() {
		return world;
	}

	public IBulletEntity getBullet() {
		return bullet;
	}

	public RayTraceResult getTraceResult() {
		return result;
	}
}
