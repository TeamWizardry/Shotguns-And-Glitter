package com.teamwizardry.shotgunsandglitter.api.capability;

import com.teamwizardry.shotgunsandglitter.ShotgunsAndGlitter;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = ShotgunsAndGlitter.MODID)
public final class SAGWorldCapability {
	private static final ResourceLocation SAG_WORLD = new ResourceLocation(ShotgunsAndGlitter.MODID, "sag_world");
	@CapabilityInject(SAGWorld.class)
	private static final Capability<SAGWorld> CAPABILITY = null;

	private SAGWorldCapability() {
	}

	public static Capability<SAGWorld> capability() {
		//noinspection ConstantConditions
		return Objects.requireNonNull(CAPABILITY, "CAPABILITY");
	}

	// call in preinit
	public static void init() {
		CapabilityManager.INSTANCE.register(SAGWorld.class, new SAGWorldStorage(), StandardSAGWorld::create);
	}

	public static SAGWorld get(World world) {
		SAGWorld cap = world.getCapability(capability(), null);
		if (cap == null) {
			throw new IllegalStateException("Missing capability: " + world.getWorldInfo().getWorldName() + "/" + world.provider.getDimensionType().getName());
		}
		return cap;
	}

	@SubscribeEvent
	public static void onAttachCapabilities(AttachCapabilitiesEvent<World> event) {
		event.addCapability(SAG_WORLD, StandardSAGWorld.create());
	}

	// other event subscriptions related to cap behavior

	private static final class SAGWorldStorage implements Capability.IStorage<SAGWorld> {
		@Override
		public NBTBase writeNBT(Capability<SAGWorld> capability, SAGWorld world, EnumFacing side) {
			return world.serializeNBT();
		}

		@Override
		public void readNBT(Capability<SAGWorld> capability, SAGWorld world, EnumFacing side, NBTBase nbt) {
			if (nbt instanceof NBTTagCompound) {
				world.deserializeNBT((NBTTagCompound) nbt);
			}
		}
	}
}
