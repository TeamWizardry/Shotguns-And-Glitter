package com.teamwizardry.shotgunsandglitter.common.items;

import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.IGunItem;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class ItemSniper extends ItemMod implements IGunItem {

	public ItemSniper() {
		super("sniper");
		setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		addTooltipContents(stack, tooltip);
	}

	@Override
	@NotNull
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @NotNull EnumHand handIn) {
		ItemStack offHand = playerIn.getHeldItemOffhand();
		ItemStack mainHand = playerIn.getHeldItemMainhand();

		if (reloadAmmo(worldIn, playerIn, mainHand, offHand))
			fireGun(worldIn, playerIn, playerIn.getHeldItem(handIn), handIn);
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@NotNull
	@Override
	public BulletType getBulletType(@NotNull ItemStack stack) {
		return BulletType.SNIPER;
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
