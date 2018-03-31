package com.teamwizardry.shotgunsandglitter.client.core;

import com.teamwizardry.librarianlib.features.animator.Animator;
import com.teamwizardry.shotgunsandglitter.ShotgunsAndGlitter;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientEventHandler {

	public static final Animator FLASH_ANIMATION_HANDLER = new Animator();

	public static ResourceLocation SPARKLE = new ResourceLocation(ShotgunsAndGlitter.MODID, "particles/sparkle_blurred");

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onTextureStitchEvent(TextureStitchEvent event) {
		event.getMap().registerSprite(SPARKLE);
	}

}
