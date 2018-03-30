package com.teamwizardry.shotgunsandglitter.client;

import com.teamwizardry.shotgunsandglitter.api.Effect;
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

	@Override
	public void doRender(@Nonnull EntityBullet entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);

		Effect effect = entity.getEffect();

		if (effect == null) return;

		effect.renderTrail(entity.world, entity.getPositionVector());
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(@Nonnull EntityBullet entity) {
		return null;
	}
}
