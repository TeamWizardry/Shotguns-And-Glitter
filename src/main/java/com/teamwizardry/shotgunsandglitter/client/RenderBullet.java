package com.teamwizardry.shotgunsandglitter.client;

import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RenderBullet extends Render<EntityBullet> {

	public RenderBullet(RenderManager renderManager) {
		super(renderManager);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntityBullet entity) {
		return null;
	}
}
