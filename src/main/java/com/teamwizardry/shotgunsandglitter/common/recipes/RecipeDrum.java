package com.teamwizardry.shotgunsandglitter.common.recipes;

import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.common.items.ItemBullet;
import com.teamwizardry.shotgunsandglitter.common.items.ModItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;

public class RecipeDrum extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	@Override
	public boolean matches(@NotNull InventoryCrafting inv, @NotNull World worldIn) {
		int bulletsFound = 0;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.isEmpty()) continue;

			if (stack.getItem() != ModItems.BULLET || BulletType.byOrdinal(stack.getItemDamage()) != BulletType.SMALL)
				return false;

			bulletsFound += stack.getCount();

			if (bulletsFound >= 200)
				return false;
		}
		return true;
	}

	@NotNull
	@Override
	public ItemStack getCraftingResult(@NotNull InventoryCrafting inv) {
		ItemStack magazine = new ItemStack(ModItems.MAGAZINE);

		NBTTagList loadedAmmo = new NBTTagList();

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (loadedAmmo.tagCount() >= 200) break;
			ItemStack stack = inv.getStackInSlot(i);

			for (int j = 0; j < stack.getCount(); j++) {
				if (loadedAmmo.tagCount() >= 200) break;
				Effect effect = ItemBullet.getEffectFromItem(stack);
				loadedAmmo.appendTag(new NBTTagString(effect.getID()));
			}
		}

		ItemNBTHelper.setList(magazine, "ammo", loadedAmmo);

		return magazine;
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

	@Override
	public boolean isDynamic() {
		return true;
	}

	@NotNull
	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}
}
