package com.teamwizardry.shotgunsandglitter;

import com.teamwizardry.librarianlib.features.base.ModCreativeTab;
import com.teamwizardry.shotgunsandglitter.common.items.ModItems;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Created by Demoniaque.
 */
public class ModTab extends ModCreativeTab {

	@Nonnull
	@Override
	public ItemStack getIconStack() {
		return new ItemStack(ModItems.PISTOL);
	}
}
