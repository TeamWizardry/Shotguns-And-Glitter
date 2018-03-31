package com.teamwizardry.shotgunsandglitter.common.potions;

import com.teamwizardry.librarianlib.features.animator.Easing;
import com.teamwizardry.librarianlib.features.animator.animations.BasicAnimation;
import com.teamwizardry.librarianlib.features.base.PotionMod;
import com.teamwizardry.shotgunsandglitter.client.core.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

/**
 * @author WireSegal
 * Created at 10:35 AM on 3/31/18.
 */
public class PotionFlash extends PotionMod {

	public float opacity = 0f;

	private static int lastFlashState = -1;

	public PotionFlash(@NotNull String name, boolean badEffect, int color) {
		super(name, badEffect, color);

		MinecraftForge.EVENT_BUS.register(this);
		registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F182890", -0.15, 2);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent e) {
		Minecraft minecraft = Minecraft.getMinecraft();
		EntityPlayer player = minecraft.player;
		if (player != null) {
			PotionEffect effect = getEffect(player);
			int amp = effect == null ? -1 : effect.getAmplifier();
			if (amp != -1) {
				if (!player.getEntityData().hasKey("sng:smooth"))
					player.getEntityData().setBoolean("sng:smooth", minecraft.gameSettings.smoothCamera);
				minecraft.gameSettings.smoothCamera = true;
			} else {
				if (player.getEntityData().hasKey("sng:smooth")) {
					minecraft.gameSettings.smoothCamera = player.getEntityData().getBoolean("sng:smooth");
					player.getEntityData().removeTag("sng:smooth");
				}
			}

			if (lastFlashState != amp) {
				if (amp != -1) {
					BasicAnimation<PotionFlash> animation = new BasicAnimation<>(this, "opacity");
					animation.setEasing(Easing.easeInQuint);
					animation.setTo(0.5f + 0.5f * ((amp + 1) / 4.0));
					animation.setDuration(5);
					ClientEventHandler.FLASH_ANIMATION_HANDLER.add(animation);
				} else {
					BasicAnimation<PotionFlash> animation = new BasicAnimation<>(this, "opacity");
					animation.setEasing(Easing.easeOutBack);
					animation.setTo(0f);
					animation.setDuration(20);
					ClientEventHandler.FLASH_ANIMATION_HANDLER.add(animation);
				}
			}

			lastFlashState = amp;
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void playSound(PlaySoundEvent e) {
		Minecraft minecraft = Minecraft.getMinecraft();
		EntityPlayer player = minecraft.player;
		if (player != null && hasEffect(player))
			e.setResultSound(null);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderOverlayAndCancelSubtitles(RenderGameOverlayEvent.Pre e) {
		if (e.getType() == RenderGameOverlayEvent.ElementType.SUBTITLES)
			e.setCanceled(true);
		else if (e.getType() == RenderGameOverlayEvent.ElementType.ALL) {
			opacity = Math.min(opacity, 1);

			if (opacity > 0) {
				ScaledResolution res = e.getResolution();
				double h = res.getScaledHeight();
				double w = res.getScaledWidth();

				GlStateManager.disableDepth();
				GlStateManager.depthMask(false);
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
				GlStateManager.color(1, 1, 1, 1);
				BufferBuilder buffer = Tessellator.getInstance().getBuffer();
				buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
				buffer.pos(0, h, -90).color(1, 1, 1, opacity).endVertex();
				buffer.pos(w, h, -90).color(1, 1, 1, opacity).endVertex();
				buffer.pos(w, 0, -90).color(1, 1, 1, opacity).endVertex();
				buffer.pos(0, 0, -90).color(1, 1, 1, opacity).endVertex();
				Tessellator.getInstance().draw();
				GlStateManager.depthMask(true);
				GlStateManager.disableBlend();
				GlStateManager.enableDepth();
				GlStateManager.enableAlpha();
			}
		}
	}
}
