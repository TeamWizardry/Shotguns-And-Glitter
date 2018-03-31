package com.teamwizardry.shotgunsandglitter.client.render;

import com.teamwizardry.shotgunsandglitter.common.entity.EntityDroppingBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

/**
 * @author WireSegal
 * Created at 3:03 PM on 3/31/18.
 */
@SideOnly(Side.CLIENT)
public class RenderFalling extends Render<EntityDroppingBlock> {
	public RenderFalling(RenderManager renderManagerIn) {
		super(renderManagerIn);
		shadowSize = 0.5F;
	}

	@Override
	public void doRender(@NotNull EntityDroppingBlock entity, double x, double y, double z, float entityYaw, float partialTicks) {
		if (entity.getBlock() != null) {
			IBlockState state = entity.getBlock();

			if (state.getRenderType() == EnumBlockRenderType.MODEL) {
				World world = entity.world;

				if (state != world.getBlockState(new BlockPos(entity))) {
					bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
					GlStateManager.pushMatrix();
					GlStateManager.disableLighting();
					Tessellator tessellator = Tessellator.getInstance();
					BufferBuilder bufferbuilder = tessellator.getBuffer();

					if (renderOutlines) {
						GlStateManager.enableColorMaterial();
						GlStateManager.enableOutlineMode(getTeamColor(entity));
					}

					bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
					BlockPos blockpos = new BlockPos(entity.posX, entity.getEntityBoundingBox().maxY, entity.posZ);
					GlStateManager.translate(x - blockpos.getX() - 0.5, y - blockpos.getY(), z - blockpos.getZ() - 0.5);
					BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
					dispatcher.getBlockModelRenderer().renderModel(world, dispatcher.getModelForState(state), state,
							blockpos, bufferbuilder, false, MathHelper.getPositionRandom(entity.getOrigin()));
					tessellator.draw();

					if (renderOutlines) {
						GlStateManager.disableOutlineMode();
						GlStateManager.disableColorMaterial();
					}

					GlStateManager.enableLighting();
					GlStateManager.popMatrix();
					super.doRender(entity, x, y, z, entityYaw, partialTicks);
				}
			}
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(@NotNull EntityDroppingBlock entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}
}
