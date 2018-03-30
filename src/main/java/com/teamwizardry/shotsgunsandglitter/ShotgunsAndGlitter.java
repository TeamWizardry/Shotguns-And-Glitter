package com.teamwizardry.shotsgunsandglitter;

import com.teamwizardry.librarianlib.features.utilities.UnsafeKt;
import net.minecraft.world.DimensionType;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

import static com.teamwizardry.shotsgunsandglitter.ShotgunsAndGlitter.*;

@Mod(modid = MODID, version = VERSION, name = MODNAME, dependencies = DEPENDENCIES)
public class ShotgunsAndGlitter {

	public static final String MODID = "shotsandglitter";
	public static final String MODNAME = "Shotguns And Glitter";
	public static final String VERSION = "GRADLE:VERSION";
	public static final String CLIENT = "com.teamwizardry.shotgunsandglitter.proxy.ClientProxy";
	public static final String SERVER = "com.teamwizardry.shotgunsandglitter.proxy.ServerProxy";
	public static final String DEPENDENCIES = "required-before:librarianlib";
	public static Logger logger;

	@SidedProxy(clientSide = CLIENT, serverSide = SERVER)
	public static CommonProxy proxy;
	@Mod.Instance
	public static ShotgunsAndGlitter instance;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();

		logger.info("Did someone say glitter? OwO");

		logger = event.getModLog();

		proxy.preInit(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
	}
}
