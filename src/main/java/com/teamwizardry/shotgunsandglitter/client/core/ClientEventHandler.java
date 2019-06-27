package com.teamwizardry.shotgunsandglitter.client.core;

import com.teamwizardry.librarianlib.features.animator.Animator;
import com.teamwizardry.shotgunsandglitter.ShotgunsAndGlitter;
import com.teamwizardry.shotgunsandglitter.api.ILingeringEffect;
import com.teamwizardry.shotgunsandglitter.api.capability.SAGWorld;
import com.teamwizardry.shotgunsandglitter.api.capability.SAGWorldCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientEventHandler {

	public static final Animator FLASH_ANIMATION_HANDLER = new Animator();
	public static final Animator DISCO_ANIMATION_HANDLER = new Animator();
	public static final Animator HEAD_TILT_ANIMATION_HANDLER = new Animator();

	public static ResourceLocation SPARKLE = new ResourceLocation(ShotgunsAndGlitter.MODID, "particles/sparkle_blurred");
	public static ResourceLocation HEART = new ResourceLocation(ShotgunsAndGlitter.MODID, "particles/heart");

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onTextureStitchEvent(TextureStitchEvent event) {
		event.getMap().registerSprite(HEART);
		event.getMap().registerSprite(SPARKLE);
	}

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;
		if (Minecraft.getMinecraft().world == null) return;

		SAGWorld worldCap = SAGWorldCapability.get(Minecraft.getMinecraft().world);
		if (worldCap == null) return;

		worldCap.getLingeringObjects().forEach(lingerObject -> {
			if (lingerObject.world != Minecraft.getMinecraft().world.provider.getDimension()) return;

			long sub = Minecraft.getMinecraft().world.getTotalWorldTime() - lingerObject.lastTime;
			if (sub < lingerObject.ticks) {
				if (lingerObject.effect instanceof ILingeringEffect)
					((ILingeringEffect) lingerObject.effect).renderLingeringEffect(Minecraft.getMinecraft().world, lingerObject);
			}
		});
	}
}
