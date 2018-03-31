package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class EffectTainted implements Effect {

	@Override
	public String getID() {
		return "tainted";
	}

	@Override
	public boolean onCollideEntity(@NotNull World world, @NotNull EntityBullet bullet, @NotNull Entity hitEntity) {
		Effect.super.onCollideEntity(world, bullet, hitEntity);
		if (hitEntity instanceof EntityLivingBase && !world.isRemote)
			((EntityLivingBase) hitEntity).addPotionEffect(new PotionEffect(MobEffects.POISON, 300, bullet.getBulletType().ordinal()));
		return true;
	}

	@Override
	public boolean onCollideBlock(@NotNull World world, @NotNull EntityBullet bullet, BlockPos pos, @NotNull IBlockState state) {
		if (!world.isRemote) {
			EntityAreaEffectCloud aEC = new EntityAreaEffectCloud(world, bullet.posX, bullet.posY, bullet.posZ);

			aEC.setOwner(bullet.getThrower());
			aEC.setRadius(3.0F);
			aEC.setRadiusOnUse(-0.25F);
			aEC.setWaitTime(10);
			aEC.setRadiusPerTick(-aEC.getRadius() / aEC.getDuration());
			aEC.setPotion(PotionTypes.LONG_POISON);

			world.spawnEntity(aEC);
		}
		return true;
	}

	@Override
	public void renderImpact(@NotNull World world, @NotNull EntityBullet bullet) {

	}

	@Override
	public void renderUpdate(@NotNull World world, @NotNull EntityBullet bullet) {

	}
}
