package com.teamwizardry.shotgunsandglitter.common.core;

import com.teamwizardry.librarianlib.features.network.PacketHandler;
import com.teamwizardry.shotgunsandglitter.api.BulletEffect;
import com.teamwizardry.shotgunsandglitter.api.GrenadeEffect;
import com.teamwizardry.shotgunsandglitter.api.capability.SAGWorldCapability;
import com.teamwizardry.shotgunsandglitter.common.blocks.ModBlocks;
import com.teamwizardry.shotgunsandglitter.common.effects.ModEffects;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityGrenade;
import com.teamwizardry.shotgunsandglitter.common.entity.ModEntities;
import com.teamwizardry.shotgunsandglitter.common.items.ModItems;
import com.teamwizardry.shotgunsandglitter.common.potions.ModPotions;
import com.teamwizardry.shotgunsandglitter.common.recipes.ModRecipes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {

		ModEffects.init();
		ModItems.init();
		ModBlocks.init();
		ModEntities.init();
		new ModTab();
		new ModPotions();
		ModSounds.init();
		MiniTurretHelper.init();
		ModRecipes.init();

		SAGWorldCapability.init();

		MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
	}

	public void init(FMLInitializationEvent event) {
		// NO-OP
	}

	public void postInit(FMLPostInitializationEvent event) {
		// NO-OP
	}

	public void updateBulletEntity(World world, EntityBullet bullet, BulletEffect bulletEffect) {
		bulletEffect.onUpdate(world, bullet);
	}

	public boolean collideBulletWithBlock(World world, EntityBullet bullet, BlockPos hit, IBlockState state, BulletEffect bulletEffect, Vec3d position) {
		bulletEffect.onImpact(world, bullet);
		if (!world.isRemote)
			PacketHandler.NETWORK.sendToAllAround(new MessageBulletImpactBlock(hit, bullet.getEntityId(), position),
					new NetworkRegistry.TargetPoint(world.provider.getDimension(), position.x, position.y, position.z, 64));
		return bulletEffect.onCollideBlock(world, bullet, hit, state);
	}

	public boolean collideBulletWithEntity(World world, EntityBullet bullet, Entity entity, BulletEffect bulletEffect, Vec3d position) {
		bulletEffect.onImpact(world, bullet);
		if (!world.isRemote)
			PacketHandler.NETWORK.sendToAllAround(new MessageBulletImpactEntity(entity.getEntityId(), bullet.getEntityId(), position),
					new NetworkRegistry.TargetPoint(world.provider.getDimension(), position.x, position.y, position.z, 64));
		return bulletEffect.onCollideEntity(world, bullet, entity);
	}

	public void grenadeUpdate(World world, EntityGrenade grenade, GrenadeEffect grenadeEffect) {
		grenadeEffect.onUpdate(world, grenade);
	}

	public void grenadeImpact(World world, EntityGrenade grenade, GrenadeEffect grenadeEffect, Vec3d position) {
		grenadeEffect.onImpact(world, grenade);
		if (!world.isRemote)
			PacketHandler.NETWORK.sendToAllAround(new MessageGrenadeExplode(grenade.getEntityId(), position),
					new NetworkRegistry.TargetPoint(world.provider.getDimension(), position.x, position.y, position.z, 64));
	}
}
