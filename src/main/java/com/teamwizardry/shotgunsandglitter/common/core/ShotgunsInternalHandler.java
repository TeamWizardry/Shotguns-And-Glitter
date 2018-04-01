package com.teamwizardry.shotgunsandglitter.common.core;

import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.IBulletEntity;
import com.teamwizardry.shotgunsandglitter.api.InternalHandler;
import com.teamwizardry.shotgunsandglitter.client.core.ClientEventHandler;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

/**
 * @author WireSegal
 * Created at 8:33 AM on 4/1/18.
 */
public class ShotgunsInternalHandler extends InternalHandler {
	@Override
	public IBulletEntity newBulletEntity(@NotNull World world, @NotNull EntityLivingBase caster, @NotNull BulletType bulletType, @NotNull Effect effect, float inaccuracy) {
		return new EntityBullet(world, caster, bulletType, effect, inaccuracy);
	}

	@Override
	public ResourceLocation getSparkle() {
		return ClientEventHandler.SPARKLE;
	}
}
