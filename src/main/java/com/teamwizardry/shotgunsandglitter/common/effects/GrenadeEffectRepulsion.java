package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.shotgunsandglitter.api.GrenadeEffect;
import com.teamwizardry.shotgunsandglitter.api.IGrenadeEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

/**
 * @author WireSegal
 * Created at 9:41 AM on 4/2/18.
 */
public class GrenadeEffectRepulsion implements GrenadeEffect {
	@Override
	public String getID() {
		return "repulsion";
	}

	@Override
	public float range(@NotNull World world, @NotNull IGrenadeEntity grenade) {
		return 15;
	}

	@Override
	public void hitEntity(@NotNull World world, @NotNull IGrenadeEntity grenade, @NotNull Entity entity, float intensity) {
		Vec3d repulsion = entity.getPositionVector().subtract(grenade.getPositionVector()).normalize().scale(intensity * 1.5);

		entity.motionX += repulsion.x;
		entity.motionY += repulsion.y + 0.25;
		entity.motionZ += repulsion.z;
	}
}
