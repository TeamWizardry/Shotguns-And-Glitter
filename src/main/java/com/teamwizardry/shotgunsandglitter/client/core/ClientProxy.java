package com.teamwizardry.shotgunsandglitter.client.core;

import com.teamwizardry.librarianlib.features.utilities.client.CustomBlockMapSprites;
import com.teamwizardry.shotgunsandglitter.ShotgunsAndGlitter;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.common.core.CommonProxy;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import com.teamwizardry.shotgunsandglitter.common.entity.ModEntities;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);

		ModEntities.initModels();

		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());

		CustomBlockMapSprites.INSTANCE.register(new ResourceLocation(ShotgunsAndGlitter.MODID, "blocks/mini_turret_head"));
		CustomBlockMapSprites.INSTANCE.register(new ResourceLocation(ShotgunsAndGlitter.MODID, "blocks/mini_turret_barrel_left"));
		CustomBlockMapSprites.INSTANCE.register(new ResourceLocation(ShotgunsAndGlitter.MODID, "blocks/mini_turret_barrel_right"));
		CustomBlockMapSprites.INSTANCE.register(new ResourceLocation(ShotgunsAndGlitter.MODID, "blocks/mini_turret_barrels"));
	}

	@Override
	public void updateBulletEntity(World world, EntityBullet bullet, Effect effect) {
		super.updateBulletEntity(world, bullet, effect);
		if (world.isRemote)
			effect.renderUpdate(world, bullet);
	}

	@Override
	public boolean collideBulletWithBlock(World world, EntityBullet bullet, BlockPos hit, IBlockState state, Effect effect, Vec3d position) {
		boolean returnValue = super.collideBulletWithBlock(world, bullet, hit, state, effect, position);
		if (world.isRemote) {
			effect.renderImpact(world, bullet);
			effect.renderCollideBlock(world, bullet, hit, state);
		}

		return returnValue;
	}

	@Override
	public boolean collideBulletWithEntity(World world, EntityBullet bullet, Entity entity, Effect effect, Vec3d position) {
		boolean returnValue = super.collideBulletWithEntity(world, bullet, entity, effect, position);
		if (world.isRemote) {
			effect.renderImpact(world, bullet);
			effect.renderCollideEntity(world, bullet, entity);
		}

		return returnValue;
	}
}
