package com.teamwizardry.shotgunsandglitter;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import static com.teamwizardry.shotgunsandglitter.ShotgunsAndGlitter.*;

@Mod(modid = MODID, version = VERSION, name = MODNAME, dependencies = DEPENDENCIES)
public class ShotgunsAndGlitter {

	public static final String MODID = "shotgunsandglitter";
	public static final String MODNAME = "Shotguns And Glitter";
	public static final String VERSION = "GRADLE:VERSION";
	public static final String CLIENT = "com.teamwizardry.shotgunsandglitter.ClientProxy";
	public static final String SERVER = "com.teamwizardry.shotgunsandglitter.ServerProxy";
	public static final String DEPENDENCIES = "required-before:librarianlib";
	public static Logger LOG;

	@SidedProxy(clientSide = CLIENT, serverSide = SERVER)
	public static CommonProxy PROXY;
	@Mod.Instance
	public static ShotgunsAndGlitter instance;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		LOG = event.getModLog();

		LOG.info("Did someone say glitter? OwO");

		PROXY.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
		PROXY.init(e);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		PROXY.postInit(e);
	}
}
