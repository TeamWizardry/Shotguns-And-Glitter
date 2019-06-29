package com.teamwizardry.shotgunsandglitter.common.items;

import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.IAmmoItem;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import org.jetbrains.annotations.NotNull;


public class ItemPistol extends ItemArtillery {

	public ItemPistol() {
		super("pistol");
	}

	@Override
	public float getPotency() {
		return 1f;
	}

	@NotNull
	@Override
	public BulletType getBulletType(@NotNull ItemStack stack) {
		return BulletType.BASIC;
	}

	@Override
	public boolean isValidAmmo(IAmmoItem ammoItem, ItemStack gun, ItemStack ammo) {
		return super.isValidAmmo(ammoItem, gun, ammo) && ammoItem == ModItems.MAGAZINE;
	}

	@Override
	public int getMaxAmmo(@NotNull ItemStack stack) {
		return 10;
	}

	@Override
	public int getReloadCooldownTime(ItemStack stack) {
		return 40;
	}

	@Override
	public int getFireCooldownTime(ItemStack stack) {
		return 10;
	}

	@Override
	public float getInaccuracy(ItemStack stack) {
		return 2.5f;
	}

	@Override
	public SoundEvent[] getFireSoundEvents(ItemStack stack) {
		return new SoundEvent[]{stack.getDisplayName().equals("bruh") ? ModSounds.BRUH : ModSounds.SHOT_PISTOL, ModSounds.MAGIC_SPARKLE};
	}

	@Override
	public SoundEvent getReloadSoundEvent(ItemStack stack) {
		return ModSounds.RELOAD_PISTOL;
	}

	@Override
	public int headKnockStrength(ItemStack stack) {
		return 5;
	}
}
