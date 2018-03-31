package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class EffectBiotic implements Effect {

	@Override
	public String getID() {
		return "biotic";
	}

	@Override
	public boolean onCollideEntity(@NotNull World world, @NotNull EntityBullet bullet, @NotNull Entity hitEntity, @NotNull RayTraceResult hit) {
		hitEntity.attackEntityFrom(DamageSource.causeThrownDamage(bullet, hitEntity), 0.0f);
		if (hitEntity instanceof EntityLivingBase)
			((EntityLivingBase) hitEntity).addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 20, bullet.getBulletType().ordinal()));
		return true;
	}
}
