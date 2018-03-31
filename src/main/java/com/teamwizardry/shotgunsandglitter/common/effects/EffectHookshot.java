package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class EffectHookshot implements Effect {

	@Override
	public String getID() {
		return "hookshot";
	}

	@Override
	public void onImpact(@NotNull World world, @NotNull EntityBullet bullet, @NotNull RayTraceResult hit) {
		EntityLivingBase thrower = bullet.getThrower();
		if (thrower != null) {
			Vec3d differenceVec = thrower.getPositionVector().subtract(bullet.getPositionVector());
			thrower.motionX = differenceVec.x * 2f;
			thrower.motionY = Math.max(differenceVec.y, 0) * 2f + 0.5f;
			thrower.motionZ = differenceVec.z * 2f;
			thrower.velocityChanged = true;
		}
	}
}
