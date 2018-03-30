package com.teamwizardry.shotsgunsandglitter;

import com.teamwizardry.shotsgunsandglitter.common.entity.EntityBullet;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModEntities {

	private static int i = 0;

	public static void init() {
		registerEntity(new ResourceLocation(ShotgunsAndGlitter.MODID, "bullet"), EntityBullet.class, "bullet");
	}

	public static void registerEntity(ResourceLocation loc, Class<? extends Entity> entityClass, String entityName) {
		registerEntity(loc, entityClass, entityName, 256, 1, true);
	}

	public static void registerEntity(ResourceLocation loc, Class<? extends Entity> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		EntityRegistry.registerModEntity(loc, entityClass, entityName, i, ShotgunsAndGlitter.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
		i++;
	}

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		//	RenderingRegistry.registerEntityRenderingHandler(EntityZachriel.class, manager -> new RenderZachriel(manager, new ModelZachriel()));

	}
}
