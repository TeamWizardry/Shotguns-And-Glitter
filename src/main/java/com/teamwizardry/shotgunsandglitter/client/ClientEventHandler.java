package com.teamwizardry.shotgunsandglitter.client;

import com.teamwizardry.shotgunsandglitter.ShotgunsAndGlitter;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {

	public static ResourceLocation SPARKLE = new ResourceLocation(ShotgunsAndGlitter.MODID, "particles/sparkle_blurred");

	@SubscribeEvent
	public void onTextureStitchEvent(TextureStitchEvent event) {
		event.getMap().registerSprite(SPARKLE);
	}

}
