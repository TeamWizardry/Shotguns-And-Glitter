package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner;
import com.teamwizardry.librarianlib.features.particle.functions.InterpColorHSV;
import com.teamwizardry.librarianlib.features.particle.functions.InterpFadeInOut;
import com.teamwizardry.shotgunsandglitter.api.BulletEffect;
import com.teamwizardry.shotgunsandglitter.api.IBulletEntity;
import com.teamwizardry.shotgunsandglitter.api.util.InterpScale;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.client.core.ClientEventHandler;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
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

public class BulletEffectBalefire implements BulletEffect {

	@Override
	public String getID() {
		return "balefire";
	}

	@Override
	public boolean onCollideEntity(@NotNull World world, @NotNull IBulletEntity bullet, @NotNull Entity hitEntity) {
		BulletEffect.super.onCollideEntity(world, bullet, hitEntity);
		hitEntity.setFire(20 + 20 * bullet.getBulletType().ordinal());
		if (hitEntity instanceof EntityLiving && RandUtil.nextDouble() < 0.25)
			hitEntity.onKillCommand();
		return RandUtil.nextDouble() < 0.8;
	}

	@Override
	public boolean onCollideBlock(@NotNull World world, @NotNull IBulletEntity bullet, @NotNull BlockPos pos, @NotNull IBlockState state) {
		EntityDroppingBlock.dropBlock(bullet.getEntityThrower(), world, pos, false, true, true, true);
		for (EnumFacing facing : EnumFacing.VALUES)
			EntityDroppingBlock.dropBlock(bullet.getEntityThrower(), world, pos.offset(facing), false, true, true, true);
		return RandUtil.nextDouble() < 0.8;
	}

	@Override
	public void onUpdate(@NotNull World world, @NotNull IBulletEntity bullet) {
		if (!world.isRemote) {
			int expansion = bullet.getBulletType().ordinal();
			for (BlockPos pos : BlockPos.getAllInBoxMutable(
					bullet.getPositionAsBlockPos().add(-expansion, 0, -expansion),
					bullet.getPositionAsBlockPos().add(expansion, -1, expansion)))
				if (world.isAirBlock(pos) && !world.isAirBlock(pos.down()))
					world.setBlockState(pos, Blocks.FIRE.getDefaultState());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderImpact(@NotNull World world, @NotNull IBulletEntity bullet) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setCollision(true);
		glitter.setCanBounce(true);

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionAsVector()), (int) (50 * bullet.getFalloff()), 0, (i, build) -> {
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
	public void renderUpdate(@NotNull World world, @NotNull IBulletEntity bullet) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setCollision(true);

		for (int j = 0; j < 5; j++) {
			ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionAsVector()), 5, 1, (i, build) -> {
				build.setScaleFunction(new InterpScale(RandUtil.nextFloat(0.2f, 1f), 0));
				build.setAlphaFunction(new InterpFadeInOut(0.1f, 1f));
				build.setLifetime(RandUtil.nextInt(40, 80));
				build.setColorFunction(new InterpColorHSV(new Color(0xffc300), Color.YELLOW));
				Vec3d bulletMotion = new Vec3d(bullet.getAsEntity().motionX, bullet.getAsEntity().motionY, bullet.getAsEntity().motionZ);
				Vec3d norm = bulletMotion.normalize();
				build.setMotion(norm.scale(1.0 / 10.0).addVector(
						norm.x * RandUtil.nextDouble(0, 0.3),
						norm.y * RandUtil.nextDouble(0, 0.3),
						norm.z * RandUtil.nextDouble(0, 0.3)
				));
			});
		}
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
