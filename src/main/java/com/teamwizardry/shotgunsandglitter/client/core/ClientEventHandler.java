package com.teamwizardry.shotgunsandglitter.client.core;

import com.teamwizardry.librarianlib.features.animator.Animator;
import com.teamwizardry.shotgunsandglitter.ShotgunsAndGlitter;
import com.teamwizardry.shotgunsandglitter.api.LingerObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;

public class ClientEventHandler {

	public static final Animator FLASH_ANIMATION_HANDLER = new Animator();
	public static final Animator DISCO_ANIMATION_HANDLER = new Animator();
	public static final Animator HEAD_TILT_ANIMATION_HANDLER = new Animator();

	public static ResourceLocation SPARKLE = new ResourceLocation(ShotgunsAndGlitter.MODID, "particles/sparkle_blurred");

	public static Set<LingerObject> lingerObjects = new HashSet<>();

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onTextureStitchEvent(TextureStitchEvent event) {
		event.getMap().registerSprite(SPARKLE);
	}

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
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
