package com.teamwizardry.shotgunsandglitter.common.items;

import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.common.config.ModConfig;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class ItemShotgun extends ItemArtillery {

	public ItemShotgun() {
		super("shotgun");
	}

	@Override
	public float getPotency() {
		return 1f;
	}

	@Override
	public int getFireCount(ItemStack stack) {
		return ModConfig.shotgunFireCount;
	}

	@Override
	public int headKnockStrength(ItemStack stack) {
		return 20;
	}

	@NotNull
	@Override
	public BulletType getBulletType(@NotNull ItemStack stack) {
		return BulletType.SHOTGUN;
	}

	@Override
	public int getMaxAmmo(@NotNull ItemStack stack) {
		return 1;
	}

	@Override
	public int getReloadCooldownTime(ItemStack stack) {
		return 10;
	}

	@Override
	public int getFireCooldownTime(ItemStack stack) {
		return 40;
	}

	@Override
	public float getInaccuracy(ItemStack stack) {
		return 15f;
	}

	@Nullable
	@Override
	public SoundEvent[] getFireSoundEvents(ItemStack stack) {
		return new SoundEvent[]{stack.getDisplayName().equals("bruh") ? ModSounds.BRUH : ModSounds.SHOT_SHOTGUN_COCK, ModSounds.MAGIC_SPARKLE};
	}

	@Nullable
	@Override
	public SoundEvent getReloadSoundEvent(ItemStack stack) {
		return ModSounds.RELOAD_SHOTGUN;
	}
}
