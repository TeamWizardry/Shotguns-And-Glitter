package com.teamwizardry.shotgunsandglitter.common.core;

import com.teamwizardry.librarianlib.features.animator.Animation;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.BulletEffect;
import com.teamwizardry.shotgunsandglitter.api.IBulletEntity;
import com.teamwizardry.shotgunsandglitter.api.InternalHandler;
import com.teamwizardry.shotgunsandglitter.client.core.ClientEventHandler;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

/**
 * @author WireSegal
 * Created at 8:33 AM on 4/1/18.
 */
public class ShotgunsInternalHandler extends InternalHandler {
	@Override
	public IBulletEntity newBulletEntity(@NotNull World world, @NotNull EntityLivingBase caster, @NotNull BulletType bulletType, @NotNull BulletEffect bulletEffect, float inaccuracy, float potency) {
		return new EntityBullet(world, caster, bulletType, bulletEffect, inaccuracy, potency);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ResourceLocation getSparkle() {
		return ClientEventHandler.SPARKLE;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addTiltAnimation(Animation<?> animation) {
		ClientEventHandler.HEAD_TILT_ANIMATION_HANDLER.add(animation);
	}
}
