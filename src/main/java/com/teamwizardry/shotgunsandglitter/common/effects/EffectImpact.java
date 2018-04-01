package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.features.math.interpolate.position.InterpBezier3D;
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner;
import com.teamwizardry.librarianlib.features.particle.functions.InterpColorHSV;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.util.InterpScale;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.client.core.ClientEventHandler;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityDroppingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

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
			float range = 10f * type.knockbackStrength;

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

	@Override
	@SideOnly(Side.CLIENT)
	public void renderImpact(@NotNull World world, @NotNull EntityBullet bullet) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.SPARKLE);

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionVector()), 100, 0, (i, build) -> {
			build.setLifetime(RandUtil.nextInt(20, 50));
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(1f, 2f), 0));
			build.setColorFunction(new InterpColorHSV(new Color(0xb342f4), new Color(0x700000)));
			build.setPositionFunction(new InterpBezier3D(Vec3d.ZERO,
					new Vec3d(
							RandUtil.nextDouble(-5, 5),
							RandUtil.nextDouble(-5, 5),
							RandUtil.nextDouble(-5, 5)
					),
					new Vec3d(
							RandUtil.nextDouble(-3, 3),
							RandUtil.nextDouble(-3, 3),
							RandUtil.nextDouble(-3, 3)
					),
					new Vec3d(
							RandUtil.nextDouble(-3, 3),
							RandUtil.nextDouble(-3, 3),
							RandUtil.nextDouble(-3, 3)
					)));
		});
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderUpdate(@NotNull World world, @NotNull EntityBullet bullet) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.SPARKLE);

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionVector()), 5, 0, (i, build) -> {
			build.setLifetime(RandUtil.nextInt(5, 20));
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(1f, 2f), 0));
			build.setColorFunction(new InterpColorHSV(new Color(0xb342f4), new Color(0x700000)));
			build.setMotion(new Vec3d(bullet.motionX, bullet.motionY, bullet.motionZ).scale(1.0 / 2.0));
			build.setPositionFunction(new InterpBezier3D(Vec3d.ZERO,
					new Vec3d(
							RandUtil.nextDouble(-0.2, 0.2),
							RandUtil.nextDouble(-0.2, 0.2),
							RandUtil.nextDouble(-0.2, 0.2)
					),
					new Vec3d(
							RandUtil.nextDouble(-2, 2),
							RandUtil.nextDouble(-2, 2),
							RandUtil.nextDouble(-2, 2)
					),
					new Vec3d(
							RandUtil.nextDouble(-2, 2),
							RandUtil.nextDouble(-2, 2),
							RandUtil.nextDouble(-2, 2)
					)));
		});
	}
}
