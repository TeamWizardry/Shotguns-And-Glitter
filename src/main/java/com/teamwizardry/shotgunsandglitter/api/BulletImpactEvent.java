package com.teamwizardry.shotgunsandglitter.api;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

public class BulletImpactEvent extends Event {

	private final World world;
	private final IBulletEntity iBulletEntity;

	public BulletImpactEvent(World world, IBulletEntity iBulletEntity) {
		this.world = world;
		this.iBulletEntity = iBulletEntity;
	}

	public World getWorld() {
		return world;
	}

	public IBulletEntity getiBulletEntity() {
		return iBulletEntity;
	}
}
