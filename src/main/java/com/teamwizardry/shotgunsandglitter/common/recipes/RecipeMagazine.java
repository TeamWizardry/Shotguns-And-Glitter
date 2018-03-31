package com.teamwizardry.shotgunsandglitter.common.recipes;

import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import com.teamwizardry.shotgunsandglitter.common.items.ModItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;

public class RecipeMagazine extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	@Override
	public boolean matches(@NotNull InventoryCrafting inv, @NotNull World worldIn) {
		int damage = -1;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.getItem() == ModItems.BULLET) {
				if (damage == -1) {
					damage = stack.getItemDamage();

				} else if (stack.getItemDamage() != damage) {
					return false;
				}

			}
		}
		return damage != -1;
	}

	@NotNull
	@Override
	public ItemStack getCraftingResult(@NotNull InventoryCrafting inv) {
		int damage = -1;
		int count = 0;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.getItem() == ModItems.BULLET) {
				if (damage == -1) {
					damage = stack.getItemDamage();
					count += stack.getCount();

				} else if (stack.getItemDamage() != damage) {
					return ItemStack.EMPTY;
				}

				count++;
			}
		}

		if (damage == -1) return ItemStack.EMPTY;

		ItemStack magazine = new ItemStack(ModItems.MAGAZINE, 0, 0);
		ItemNBTHelper.setInt(magazine, "bullets", count);
		ItemNBTHelper.setInt(magazine, "type", damage);

		return magazine;
	}

	@Override
	public boolean canFit(int width, int height) {
		return false;
	}

	@NotNull
	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}
}
