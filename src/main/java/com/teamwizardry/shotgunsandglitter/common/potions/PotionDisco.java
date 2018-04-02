package com.teamwizardry.shotgunsandglitter.common.potions;

import com.teamwizardry.librarianlib.features.animator.Easing;
import com.teamwizardry.librarianlib.features.animator.animations.BasicAnimation;
import com.teamwizardry.librarianlib.features.base.PotionMod;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.client.core.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WireSegal
 * Created at 10:35 AM on 3/31/18.
 */
public class PotionDisco extends PotionMod {

	public float discoCountdown = 0;
	public float hue1 = 0;
	public float hue2 = 0;

	public boolean animating = false;

	public PotionDisco(@NotNull String name, boolean badEffect, int color) {
		super(name, badEffect, color);

		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) {
		super.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, amplifier);
		discoCountdown = 200;
		animating = false;
	}

	@SubscribeEvent
	public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
		if (!(event.getEntity() instanceof EntityLivingBase)) return;

		EntityLivingBase entity = (EntityLivingBase) event.getEntity();

		if (hasEffect(entity)) {
			if (RandUtil.nextInt(10) == 0)
				entity.setJumping(true);

			if (RandUtil.nextInt(5) == 0)
				entity.motionX += RandUtil.nextDouble(-0.3, 0.3);
			if (RandUtil.nextInt(5) == 0)
				entity.motionZ += RandUtil.nextDouble(-0.3, 0.3);

			entity.velocityChanged = true;
		}
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

			if (hasEffect(player) && !animating) {
				animating = true;
				float fromHue = RandUtil.nextFloat();
				float toHue = RandUtil.nextFloat();
				BasicAnimation<PotionDisco> discoAnim = new BasicAnimation<>(this, "discoCountdown");
				discoAnim.setEasing(Easing.easeInOutQuint);
				discoAnim.setTo(0);
				discoAnim.setDuration(400);
				discoAnim.setCompletion(() -> animating = false);
				ClientEventHandler.DISCO_ANIMATION_HANDLER.add(discoAnim);

				BasicAnimation<PotionDisco> hue1Anim = new BasicAnimation<>(this, "hue1");
				hue1Anim.setEasing(Easing.easeInOutQuint);
				hue1Anim.setFrom(fromHue);
				hue1Anim.setTo(toHue);
				hue1Anim.setDuration(400);
				ClientEventHandler.DISCO_ANIMATION_HANDLER.add(hue1Anim);

				BasicAnimation<PotionDisco> hue2Anim = new BasicAnimation<>(this, "hue2");
				hue2Anim.setEasing(Easing.easeInOutQuint);
				hue2Anim.setFrom(toHue);
				hue2Anim.setTo(fromHue);
				hue2Anim.setDuration(400);
				ClientEventHandler.DISCO_ANIMATION_HANDLER.add(hue2Anim);
			}
		}
	}

	@Override
	public List<ItemStack> getCurativeItems() {
		return new ArrayList<>();
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderOverlayAndCancelSubtitles(RenderGameOverlayEvent.Pre e) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (player == null) return;

		if (e.getType() == RenderGameOverlayEvent.ElementType.SUBTITLES)
			e.setCanceled(true);
		else if (e.getType() == RenderGameOverlayEvent.ElementType.ALL) {

			if (discoCountdown > 0) {
				float x = 1 - (discoCountdown / 200.0f);
				float o = 1 - (x * x * x * x * x * x * x * x * x * x);

				ScaledResolution res = e.getResolution();
				double h = res.getScaledHeight();
				double w = res.getScaledWidth();
				double depthH = w / 20.0;
				double depthW = h / 10.0;

				GlStateManager.pushMatrix();

				GlStateManager.disableDepth();
				GlStateManager.depthMask(false);
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
				GlStateManager.color(1, 1, 1, 1);
				GlStateManager.disableTexture2D();
				GlStateManager.enableColorMaterial();
				GlStateManager.shadeModel(GL11.GL_SMOOTH);

				Color color1 = Color.getHSBColor(hue1, 1, 1);
				Color color2 = Color.getHSBColor(hue2, 1, 1);

				float r1 = color1.getRed() / 255f;
				float r2 = color2.getRed() / 255f;
				float b1 = color1.getBlue() / 255f;
				float b2 = color2.getBlue() / 255f;
				float g1 = color1.getGreen() / 255f;
				float g2 = color2.getGreen() / 255f;

				BufferBuilder buffer = Tessellator.getInstance().getBuffer();

				buffer.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);
				buffer.pos(0, 0, -90).color(r1, g1, b1, o).endVertex();
				buffer.pos(0, h, -90).color(r1, g1, b1, 0).endVertex();
				buffer.pos(w, 0, -90).color(r1, g1, b1, 0).endVertex();

				buffer.pos(w, h, -90).color(r2, g2, b2, o).endVertex();
				buffer.pos(0, h, -90).color(r2, g2, b2, 0).endVertex();

				GL11.glScissor((int) depthW, (int) depthH, (int) (w - depthW), (int) (h - depthH));

				Tessellator.getInstance().draw();

				GlStateManager.depthMask(true);
				GlStateManager.disableBlend();
				GlStateManager.enableDepth();
				GlStateManager.enableAlpha();
				GlStateManager.enableTexture2D();
				GlStateManager.disableColorMaterial();

				GlStateManager.popMatrix();
			}
		}
	}
}
