package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.features.math.interpolate.position.InterpHelix;
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner;
import com.teamwizardry.librarianlib.features.particle.functions.InterpColorHSV;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.util.InterpScale;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.client.core.ClientEventHandler;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityDroppingBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class EffectBalefire implements Effect {

	@Override
	public String getID() {
		return "balefire";
	}

	@Override
	public boolean onCollideEntity(@NotNull World world, @NotNull EntityBullet bullet, @NotNull Entity hitEntity) {
		Effect.super.onCollideEntity(world, bullet, hitEntity);
		hitEntity.setFire(20 + 20 * bullet.getBulletType().ordinal());
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
		if (!world.isRemote) {
			int expansion = bullet.getBulletType().ordinal();
			for (BlockPos pos : BlockPos.getAllInBoxMutable(
					bullet.getPosition().add(-expansion, -1, -expansion),
					bullet.getPosition().add(expansion, -1, expansion)))
				if (world.isAirBlock(pos))
					world.setBlockState(pos, Blocks.FIRE.getDefaultState());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderImpact(@NotNull World world, @NotNull EntityBullet bullet) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setCollision(true);
		glitter.setCanBounce(true);

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionVector()), 100, 0, (i, build) -> {
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(0.5f, 2f), RandUtil.nextFloat(0, 0.5f)));
			build.setLifetime(RandUtil.nextInt(50, 100));
			build.setColorFunction(new InterpColorHSV(new Color(0x893000), Color.YELLOW));
			build.setAcceleration(new Vec3d(0, RandUtil.nextDouble(-0.04, -0.07), 0));
			build.setDeceleration(new Vec3d(0.6, 1, 0.6));

			double radius = 3 * RandUtil.nextFloat();
			double theta = 2.0f * (float) Math.PI * RandUtil.nextFloat();
			double x = radius * MathHelper.cos((float) theta);
			double z = radius * MathHelper.sin((float) theta);

			build.setMotion(new Vec3d(x, RandUtil.nextDouble(0.1, 1), z));
			build.setJitter(3, new Vec3d(
					RandUtil.nextDouble(-0.4, 0.4),
					RandUtil.nextDouble(-0.4, 0.4),
					RandUtil.nextDouble(-0.4, 0.4)
			));
		});
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderUpdate(@NotNull World world, @NotNull EntityBullet bullet) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setCollision(true);

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionVector()), 3, 0, (i, build) -> {
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(0.2f, 0.8f), 0));
			build.setLifetime(RandUtil.nextInt(20, 40));
			build.setColorFunction(new InterpColorHSV(new Color(0x893000), Color.YELLOW));
			build.setPositionFunction(new InterpHelix(
					Vec3d.ZERO,
					new Vec3d(bullet.motionX, bullet.motionY, bullet.motionZ),
					0.1f, 0.1f, RandUtil.nextInt(-3, 3), RandUtil.nextFloat()));
		});
	}

	@Override
	public @Nullable SoundEvent getImpactSound() {
		return ModSounds.FIRE;
	}

	@Override
	public float getVolume() {
		return RandUtil.nextFloat(4, 4.5f);
	}
}
