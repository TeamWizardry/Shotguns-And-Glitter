package com.teamwizardry.shotgunsandglitter.client.render;

import com.teamwizardry.librarianlib.features.animator.Animator;
import com.teamwizardry.librarianlib.features.animator.Easing;
import com.teamwizardry.librarianlib.features.animator.animations.BasicAnimation;
import com.teamwizardry.librarianlib.features.tesr.TileRenderHandler;
import com.teamwizardry.librarianlib.features.utilities.client.ClientRunnable;
import com.teamwizardry.shotgunsandglitter.ShotgunsAndGlitter;
import com.teamwizardry.shotgunsandglitter.common.tile.TileMiniTurret;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = ShotgunsAndGlitter.MODID)
public class TESRMiniTurret extends TileRenderHandler<TileMiniTurret> {

	private static Animator ANIMATOR = new Animator();

	public double rotationPitch = 0;
	public double rotationYaw = 0;
	public double currentTarget = -1;
	public boolean animating = false;
	public boolean firing = false;

	private static IBakedModel mini_turret_head = null,
			mini_turret_barrels = null,
			mini_turret_barrel_right = null,
			mini_turret_barrel_left = null;

	public TESRMiniTurret(@NotNull TileMiniTurret tile) {
		super(tile);
	}

	static {
		ClientRunnable.registerReloadHandler(() -> {
			mini_turret_barrels = null;
			mini_turret_head = null;
			mini_turret_barrel_left = null;
			mini_turret_barrel_right = null;
		});
	}

	private static boolean getBakedModels() {
		IModel model;
		if (mini_turret_head == null) {
			try {
				model = ModelLoaderRegistry.getModel(new ResourceLocation(ShotgunsAndGlitter.MODID, "block/mini_turret_head"));
				mini_turret_head = model.bake(model.getDefaultState(), DefaultVertexFormats.ITEM,
						location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (mini_turret_barrels == null) {
			try {
				model = ModelLoaderRegistry.getModel(new ResourceLocation(ShotgunsAndGlitter.MODID, "block/mini_turret_barrels"));
				mini_turret_barrels = model.bake(model.getDefaultState(), DefaultVertexFormats.ITEM,
						location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (mini_turret_barrel_right == null) {
			try {
				model = ModelLoaderRegistry.getModel(new ResourceLocation(ShotgunsAndGlitter.MODID, "block/mini_turret_barrel_right"));
				mini_turret_barrel_right = model.bake(model.getDefaultState(), DefaultVertexFormats.ITEM,
						location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (mini_turret_barrel_left == null) {
			try {
				model = ModelLoaderRegistry.getModel(new ResourceLocation(ShotgunsAndGlitter.MODID, "block/mini_turret_barrel_left"));
				mini_turret_barrel_left = model.bake(model.getDefaultState(), DefaultVertexFormats.ITEM,
						location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mini_turret_barrels != null && mini_turret_head != null && mini_turret_barrel_left != null && mini_turret_barrel_right != null;
	}

	@Override
	public void render(float partialTicks, int destroyStage, float alpha) {
		super.render(partialTicks, destroyStage, alpha);

		if (!getBakedModels()) return;

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableLighting();
		RenderHelper.enableStandardItemLighting();

		TextureManager texturemanager = Minecraft.getMinecraft().renderEngine;

		if (texturemanager != null) {
			texturemanager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		}

		if (Minecraft.isAmbientOcclusionEnabled())
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
		else GlStateManager.shadeModel(GL11.GL_FLAT);

		double subX;
		double subY;
		double subZ;

		if (!tile.firing && (firing || rotationYaw != 0 || rotationPitch != 45)) {
			firing = false;
			animating = true;
			BasicAnimation<TESRMiniTurret> animPitch = new BasicAnimation<>(this, "rotationPitch");
			animPitch.setDuration(10);
			animPitch.setTo(45);
			animPitch.setEasing(Easing.easeOutCubic);
			animPitch.setCompletion(() -> animating = false);
			ANIMATOR.add(animPitch);

			BasicAnimation<TESRMiniTurret> animYaw = new BasicAnimation<>(this, "rotationYaw");
			animYaw.setDuration(0);
			animYaw.setTo(rotationYaw);
			animYaw.setEasing(Easing.easeOutCubic);
			ANIMATOR.add(animYaw);

		} else if (tile.firing && !firing || tile.firing && tile.getTargetID() != currentTarget) {
			animating = true;
			firing = true;
			currentTarget = tile.getTargetID();
			Entity entity = tile.getWorld().getEntityByID(tile.getTargetID());
			if (entity != null) {

				double ix = tile.getPos().getX() + 0.5;
				double iy = tile.getPos().getY() + 0.5;
				double iz = tile.getPos().getZ() + 0.5;
				double fx = entity.posX;
				double fy = entity.posY + entity.height / 2.0;
				double fz = entity.posZ;

				subX = ix - fx;
				subY = iy - fy;
				subZ = iz - fz;

				double lengthSq = Math.sqrt(subZ * subZ + subX * subX);

				double rotYaw = 180 - Math.toDegrees(MathHelper.atan2(subZ, subX)) + 90;// + Math.sin(System.currentTimeMillis()  / 100.0) * 90.0;
				double rotPitch = 180 - Math.toDegrees(Math.atan2(lengthSq, subY) + Math.PI) + 90;

				BasicAnimation<TESRMiniTurret> animPitch = new BasicAnimation<>(this, "rotationPitch");
				animPitch.setDuration(10);
				animPitch.setTo(rotPitch);
				animPitch.setEasing(Easing.easeOutCubic);
				animPitch.setCompletion(() -> animating = false);
				ANIMATOR.add(animPitch);

				BasicAnimation<TESRMiniTurret> animYaw = new BasicAnimation<>(this, "rotationYaw");
				animYaw.setDuration(10);
				animYaw.setTo(rotYaw);
				animYaw.setEasing(Easing.easeOutCubic);
				animYaw.setCompletion(() -> animating = false);
				ANIMATOR.add(animYaw);
			}
		} else if (tile.firing && firing && !animating) {
			Entity entity = tile.getWorld().getEntityByID(tile.getTargetID());
			if (entity != null) {

				double ix = tile.getPos().getX() + 0.5;
				double iy = tile.getPos().getY() + 0.5;
				double iz = tile.getPos().getZ() + 0.5;
				double fx = entity.posX;
				double fy = entity.posY + entity.height / 2.0;
				double fz = entity.posZ;

				subX = ix - fx;
				subY = iy - fy;
				subZ = iz - fz;

				double lengthSq = Math.sqrt(subZ * subZ + subX * subX);

				double rotationYaw = 180 - Math.toDegrees(MathHelper.atan2(subZ, subX)) + 90;// + Math.sin(System.currentTimeMillis()  / 100.0) * 90.0;
				double rotationPitch = 180 - Math.toDegrees(Math.atan2(lengthSq, subY) + Math.PI) + 90;

				this.rotationYaw = rotationYaw;
				this.rotationPitch = rotationPitch;
			}
		}

		GlStateManager.translate(0.5, 0.65, 0.5);

		GlStateManager.rotate((float) rotationYaw, 0, 1, 0);
		GlStateManager.rotate((float) rotationPitch, 1, 0, 0);
		GlStateManager.translate(-0.5, -0.5, -0.5);

		Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightnessColor(mini_turret_head, 1.0F, 1, 1, 1);

		// Yes I mixed the models, leave me alone.t

		// LEFT
		if (tile.isRightBarrel())
			GlStateManager.translate(0, 0, ((1 - (tile.getCooldown() / 40.0)) / 5.0 - 0.25));

		Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightnessColor(mini_turret_barrel_right, 1.0F, 1, 1, 1);

		if (tile.isRightBarrel())
			GlStateManager.translate(0, 0, -((1 - (tile.getCooldown() / 40.0)) / 5.0 - 0.25));

		// RIGHT
		if (!tile.isRightBarrel())
			GlStateManager.translate(0, 0, ((1 - (tile.getCooldown() / 40.0)) / 5.0 - 0.25));

		Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightnessColor(mini_turret_barrel_left, 1.0F, 1, 1, 1);

		if (!tile.isRightBarrel())
			GlStateManager.translate(0, 0, -((1 - (tile.getCooldown() / 40.0)) / 5.0 - 0.25));

		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}
}
