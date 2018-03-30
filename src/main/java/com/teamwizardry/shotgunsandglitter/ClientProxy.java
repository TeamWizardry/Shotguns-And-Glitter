package com.teamwizardry.shotgunsandglitter;

import com.teamwizardry.shotgunsandglitter.client.ClientEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);

		ModEntities.initModels();

		MinecraftForge.EVENT_BUS.register(ClientEventHandler.class);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
	}

}
