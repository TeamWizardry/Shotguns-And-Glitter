package com.teamwizardry.shotgunsandglitter.common.core;

import com.teamwizardry.librarianlib.features.network.PacketHandler;
import com.teamwizardry.shotgunsandglitter.api.ILingeringEffect;
import com.teamwizardry.shotgunsandglitter.api.LingeringObject;
import com.teamwizardry.shotgunsandglitter.api.capability.SAGWorld;
import com.teamwizardry.shotgunsandglitter.api.capability.SAGWorldCapability;
import com.teamwizardry.shotgunsandglitter.common.network.PacketSyncSAGWorld;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashSet;
import java.util.Set;

public class CommonEventHandler {

	@SubscribeEvent
	public void logInEvent(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.player == null) return;
		if (event.player.world.isRemote) return;
		if (!(event.player instanceof EntityPlayerMP)) return;

		SAGWorld worldCap = SAGWorldCapability.get(event.player.world);
		if (worldCap == null) return;
		PacketHandler.NETWORK.sendTo(new PacketSyncSAGWorld(worldCap.serializeNBT()), (EntityPlayerMP) event.player);
	}

	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;

		SAGWorld worldCap = SAGWorldCapability.get(event.world);
		if (worldCap == null) return;

		Set<LingeringObject> tmp = new HashSet<>(worldCap.getLingeringObjects());

		boolean changed = false;
		for (LingeringObject lingerObject : tmp) {
			long sub = lingerObject.world.getTotalWorldTime() - lingerObject.lastTime;
			if (sub > lingerObject.ticks) {
				worldCap.getLingeringObjects().remove(lingerObject);
				changed = true;
			} else {
				if (lingerObject.effect instanceof ILingeringEffect)
					((ILingeringEffect) lingerObject.effect).runLingeringEffect(lingerObject);
			}
		}
		if (changed) {
			PacketHandler.NETWORK.sendToDimension(new PacketSyncSAGWorld(worldCap.serializeNBT()), event.world.provider.getDimension());
		}
	}
}
