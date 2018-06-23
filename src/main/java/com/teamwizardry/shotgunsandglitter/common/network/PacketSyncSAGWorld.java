package com.teamwizardry.shotgunsandglitter.common.network;

import com.teamwizardry.librarianlib.core.LibrarianLib;
import com.teamwizardry.librarianlib.features.autoregister.PacketRegister;
import com.teamwizardry.librarianlib.features.network.PacketBase;
import com.teamwizardry.librarianlib.features.saving.Save;
import com.teamwizardry.shotgunsandglitter.api.capability.SAGWorld;
import com.teamwizardry.shotgunsandglitter.api.capability.SAGWorldCapability;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;

@PacketRegister(Side.CLIENT)
public class PacketSyncSAGWorld extends PacketBase {

	@Save
	public NBTTagCompound tags;

	public PacketSyncSAGWorld() {
	}

	public PacketSyncSAGWorld(NBTTagCompound tag) {
		tags = tag;
	}


	@Override
	public void handle(@NotNull MessageContext ctx) {
		if (ctx.side.isServer()) return;
		World world = LibrarianLib.PROXY.getClientPlayer().world;
		if (world == null) return;

		SAGWorld cap = SAGWorldCapability.get(world);
		if (cap != null) {
			cap.deserializeNBT(tags);
		}
	}
}
