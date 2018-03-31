package com.teamwizardry.shotgunsandglitter;

import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {

		ModItems.init();
		ModEntities.init();
		ModEffects.init();
		new ModTab();
	}

	public void init(FMLInitializationEvent event) {
		// NO-OP
	}

	public void postInit(FMLPostInitializationEvent event) {
		// NO-OP
	}

	public void updateBulletEntity(World world, EntityBullet bullet, Effect effect) {
		effect.onUpdate(world, bullet);
	}

	public boolean collideBulletWithBlock(World world, EntityBullet bullet, RayTraceResult hit, IBlockState state, Effect effect) {
		effect.onImpact(world, bullet, hit);
		return effect.onCollideBlock(world, bullet, hit, state);
	}

	public boolean collideBulletWithEntity(World world, EntityBullet bullet, Entity entity, RayTraceResult hit, Effect effect) {
		effect.onImpact(world, bullet, hit);
		return effect.onCollideEntity(world, bullet, entity, hit);
	}
}
