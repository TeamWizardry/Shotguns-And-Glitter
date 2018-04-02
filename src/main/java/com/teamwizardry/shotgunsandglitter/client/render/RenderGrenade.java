package com.teamwizardry.shotgunsandglitter.client.render;

import com.teamwizardry.shotgunsandglitter.common.entity.EntityGrenade;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class RenderGrenade extends Render<EntityGrenade> {

	public RenderGrenade(RenderManager renderManager) {
		super(renderManager);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(@NotNull EntityGrenade entity) {
		return null;
	}
}
