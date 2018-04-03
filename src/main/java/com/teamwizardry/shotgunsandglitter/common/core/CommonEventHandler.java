package com.teamwizardry.shotgunsandglitter.common.core;

import com.teamwizardry.shotgunsandglitter.api.LingerObject;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashSet;
import java.util.Set;

public class CommonEventHandler {

	public static Set<LingerObject> lingerObjects = new HashSet<>();

	@SubscribeEvent
	public void onClientTick(TickEvent.WorldTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;
		lingerObjects.removeIf(lingerObject -> {
			long sub = lingerObject.world.getTotalWorldTime() - lingerObject.lastTime;
			if (sub >= lingerObject.ticks) {
				return true;
			} else {
				lingerObject.consumer.accept(lingerObject);
			}
			return false;
		});
	}
}
