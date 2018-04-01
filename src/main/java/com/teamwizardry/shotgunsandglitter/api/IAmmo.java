package com.teamwizardry.shotgunsandglitter.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public interface IAmmo {

	default void onAmmoRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (!worldIn.isRemote)
			if (handIn == EnumHand.MAIN_HAND) {
				ItemStack bullets = playerIn.getHeldItemMainhand().copy();
				ItemStack offhand = playerIn.getHeldItemOffhand().copy();
				playerIn.setHeldItem(EnumHand.OFF_HAND, bullets);
				playerIn.setHeldItem(EnumHand.MAIN_HAND, offhand);
				playerIn.inventoryContainer.detectAndSendChanges();
			} else {
				ItemStack offhand = playerIn.getHeldItemMainhand().copy();
				ItemStack bullets = playerIn.getHeldItemOffhand().copy();

				if (!(offhand.getItem() instanceof IGun)) {
					playerIn.setHeldItem(EnumHand.MAIN_HAND, bullets);
					playerIn.setHeldItem(EnumHand.OFF_HAND, offhand);
					playerIn.inventoryContainer.detectAndSendChanges();
				}
			}
	}
}
