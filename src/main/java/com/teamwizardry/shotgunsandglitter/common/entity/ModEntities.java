package com.teamwizardry.shotgunsandglitter.common.entity;

import com.teamwizardry.shotgunsandglitter.ShotgunsAndGlitter;
import com.teamwizardry.shotgunsandglitter.client.render.RenderBullet;
import com.teamwizardry.shotgunsandglitter.client.render.RenderFalling;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModEntities {

	private static int i = 0;

	public static void init() {
		registerEntity(new ResourceLocation(ShotgunsAndGlitter.MODID, "bullet"), EntityBullet.class, "bullet");
		registerEntity(new ResourceLocation(ShotgunsAndGlitter.MODID, "dropping"), EntityDroppingBlock.class, "dropping");
	}

	public static void registerEntity(ResourceLocation loc, Class<? extends Entity> entityClass, String entityName) {
		registerEntity(loc, entityClass, entityName, 256, 3, true);
	}

	public static void registerEntity(ResourceLocation loc, Class<? extends Entity> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		EntityRegistry.registerModEntity(loc, entityClass, entityName, i, ShotgunsAndGlitter.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
		i++;
	}

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		RenderingRegistry.registerEntityRenderingHandler(EntityBullet.class, RenderBullet::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDroppingBlock.class, RenderFalling::new);
	}
}
