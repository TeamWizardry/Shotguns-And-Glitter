package com.teamwizardry.shotgunsandglitter.api;

import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Effect {

	// Effect ID

	String getID();

	// Logic Methods

	default void onImpact(@NotNull World world, @NotNull IBulletEntity bullet) {
		// NO-OP
	}

	default boolean onCollideEntity(@NotNull World world, @NotNull IBulletEntity bullet, @NotNull Entity hitEntity) {
		hitEntity.attackEntityFrom(DamageSource.causeThrownDamage(bullet.getAsEntity(), hitEntity), damage(world, bullet));
		if (hitEntity instanceof EntityLivingBase)
			((EntityLivingBase) hitEntity).knockBack(bullet.getAsEntity(), knockbackStrength(world, bullet),
					MathHelper.sin(bullet.getAsEntity().rotationYaw * (float) (Math.PI / 180)),
					-MathHelper.cos(bullet.getAsEntity().rotationYaw * (float) (Math.PI / 180)));
		return true;
	}

	default boolean onCollideBlock(@NotNull World world, @NotNull IBulletEntity bullet, @NotNull BlockPos pos, @NotNull IBlockState state) {
		return true;
	}

	default void onUpdate(@NotNull World world, @NotNull IBulletEntity bullet) {
		// NO-OP
	}

	// Numerical Methods

	default float getVelocity(@NotNull World world, @NotNull BulletType bulletType) {
		return 3.0F;
	}

	default float damage(@NotNull World world, @NotNull IBulletEntity bullet) {
		return bullet.getBulletType().damage;
	}

	default float knockbackStrength(@NotNull World world, @NotNull IBulletEntity bullet) {
		return bullet.getBulletType().knockbackStrength;
	}

	// Render Methods

	@SideOnly(Side.CLIENT)
	default void renderImpact(@NotNull World world, @NotNull IBulletEntity bullet) {
		// NO-OP
	}

	@SideOnly(Side.CLIENT)
	default void renderUpdate(@NotNull World world, @NotNull IBulletEntity bullet) {
		// NO-OP
	}

	@SideOnly(Side.CLIENT)
	default void renderCollideBlock(@NotNull World world, @NotNull IBulletEntity bullet, @NotNull BlockPos pos, @NotNull IBlockState state) {
		// NO-OP
	}

	@SideOnly(Side.CLIENT)
	default void renderCollideEntity(@NotNull World world, @NotNull IBulletEntity bullet, @NotNull Entity hitEntity) {
		// NO-OP
	}

	@Nullable
	default SoundEvent getFireSound() {
		return null;
	}

	@Nullable
	default SoundEvent getImpactSound() {
		return null;
	}

	default float getVolume() {
		return RandUtil.nextFloat(3f, 4f);
	}
}
