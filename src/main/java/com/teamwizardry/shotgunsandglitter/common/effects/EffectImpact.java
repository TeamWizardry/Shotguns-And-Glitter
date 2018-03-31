package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityDroppingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class EffectImpact implements Effect {

	@Override
	public String getID() {
		return "impact";
	}


	@Override
	public float knockbackStrength(@NotNull World world, @NotNull EntityBullet bullet) {
		return bullet.getBulletType().knockbackStrength * 2;
	}

	@Override
	public void onImpact(@NotNull World world, @NotNull EntityBullet bullet) {
		if (!world.isRemote) {
			BulletType type = bullet.getBulletType();
			float range = 2f * type.knockbackStrength;

			for (BlockPos pos : BlockPos.getAllInBoxMutable(bullet.getPosition().add(-range, -range, -range),
					bullet.getPosition().add(range, range / 2, range))) {
				if (pos.distanceSq(bullet.getPosition()) <= range * range) {
					Entity block = EntityDroppingBlock.dropBlock(bullet.getThrower(), world, pos, null, false, true, true);
					if (block != null) {
						block.motionX = RandUtil.nextDouble(-0.5, 0.5);
						block.motionY = 0.625;
						block.motionZ = RandUtil.nextDouble(-0.5, 0.5);
						block.velocityChanged = true;
						world.spawnEntity(block);
					}
				}
			}
		}
	}
}
