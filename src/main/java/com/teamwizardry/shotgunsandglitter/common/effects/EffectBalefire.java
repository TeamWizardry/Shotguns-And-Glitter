package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityDroppingBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class EffectBalefire implements Effect {

	@Override
	public String getID() {
		return "balefire";
	}

	@Override
	public boolean onCollideEntity(@NotNull World world, @NotNull EntityBullet bullet, @NotNull Entity hitEntity) {
		Effect.super.onCollideEntity(world, bullet, hitEntity);
		hitEntity.setFire(20);
		if (hitEntity instanceof EntityLiving && RandUtil.nextDouble() < 0.25)
			hitEntity.onKillCommand();
		return RandUtil.nextDouble() < 0.8;
	}

	@Override
	public boolean onCollideBlock(@NotNull World world, @NotNull EntityBullet bullet, @NotNull BlockPos pos, @NotNull IBlockState state) {
		EntityDroppingBlock.dropBlock(bullet.getThrower(), world, pos, false, true, true, true);
		for (EnumFacing facing : EnumFacing.VALUES)
			EntityDroppingBlock.dropBlock(bullet.getThrower(), world, pos.offset(facing), false, true, true, true);
		return RandUtil.nextDouble() < 0.8;
	}

	@Override
	public void onUpdate(@NotNull World world, @NotNull EntityBullet bullet) {
		for (BlockPos pos : BlockPos.getAllInBoxMutable(bullet.getPosition().add(-2, -1, -2), bullet.getPosition().add(2, -1, 2)))
			if (world.isAirBlock(pos))
				world.setBlockState(pos, Blocks.FIRE.getDefaultState());
	}
}
