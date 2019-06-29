package com.teamwizardry.shotgunsandglitter.common.items;

import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class ItemSniper extends ItemArtillery {

	public ItemSniper() {
		super("sniper");
	}

	@NotNull
	@Override
	public BulletType getBulletType(@NotNull ItemStack stack) {
		return BulletType.SNIPER;
	}

	@Override
	public float getPotency() {
		return 1;
	}

	@Override
	public int getMaxAmmo(@NotNull ItemStack stack) {
		return 1;
	}

	@Override
	public int getReloadCooldownTime(ItemStack stack) {
		return 40;
	}

	@Override
	public int getFireCooldownTime(ItemStack stack) {
		return 50;
	}

	@Override
	public float getInaccuracy(ItemStack stack) {
		return 0;
	}

	@Nullable
	@Override
	public SoundEvent[] getFireSoundEvents(ItemStack stack) {
		return new SoundEvent[]{ModSounds.SHOT_SNIPER, ModSounds.DUST_SPARKLE};
	}

	@Nullable
	@Override
	public SoundEvent getReloadSoundEvent(ItemStack stack) {
		return ModSounds.RELOAD_SNIPER;
	}

	@Override
	public int headKnockStrength(ItemStack stack) {
		return 15;
	}
}
