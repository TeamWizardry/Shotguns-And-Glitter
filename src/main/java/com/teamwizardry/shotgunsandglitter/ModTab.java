package com.teamwizardry.shotgunsandglitter;

import com.teamwizardry.librarianlib.features.base.ModCreativeTab;
import com.teamwizardry.shotgunsandglitter.common.items.ModItems;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;


/**
 * Created by Demoniaque.
 */
public class ModTab extends ModCreativeTab {

	@NotNull
	@Override
	public ItemStack getIconStack() {
		return new ItemStack(ModItems.PISTOL);
	}
}
