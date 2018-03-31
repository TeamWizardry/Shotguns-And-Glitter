package com.teamwizardry.shotgunsandglitter.common.core;

import com.teamwizardry.librarianlib.features.network.PacketHandler;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.common.effects.ModEffects;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import com.teamwizardry.shotgunsandglitter.common.entity.ModEntities;
import com.teamwizardry.shotgunsandglitter.common.items.ModItems;
import com.teamwizardry.shotgunsandglitter.common.potions.ModPotions;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {

		ModEffects.init();
		ModItems.init();
		ModEntities.init();
		new ModTab();
		new ModPotions();
		ModSounds.init();
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

	public boolean collideBulletWithBlock(World world, EntityBullet bullet, BlockPos hit, IBlockState state, Effect effect, Vec3d position) {
		effect.onImpact(world, bullet);
		if (!world.isRemote)
			PacketHandler.NETWORK.sendToAllAround(new MessageBulletImpactBlock(hit, bullet.getEntityId(), position),
					new NetworkRegistry.TargetPoint(world.provider.getDimension(), position.x, position.y, position.z, 64));
		return effect.onCollideBlock(world, bullet, hit, state);
	}

	public boolean collideBulletWithEntity(World world, EntityBullet bullet, Entity entity, Effect effect, Vec3d position) {
		effect.onImpact(world, bullet);
		if (!world.isRemote)
			PacketHandler.NETWORK.sendToAllAround(new MessageBulletImpactEntity(entity.getEntityId(), bullet.getEntityId(), position),
					new NetworkRegistry.TargetPoint(world.provider.getDimension(), position.x, position.y, position.z, 64));
		return effect.onCollideEntity(world, bullet, entity);
	}
}
