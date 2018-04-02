package com.teamwizardry.shotgunsandglitter.common.recipes;

import com.google.common.collect.Lists;
import com.teamwizardry.shotgunsandglitter.api.BulletEffect;
import com.teamwizardry.shotgunsandglitter.api.IAmmoItem;
import com.teamwizardry.shotgunsandglitter.api.IBulletItem;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecipeAmmoHolder extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	private final ItemStack testStack;
	private final IAmmoItem ammoItem;

	public RecipeAmmoHolder(IAmmoItem ammoItem, ItemStack testStack) {
		this.ammoItem = ammoItem;
		this.testStack = testStack;
	}

	public RecipeAmmoHolder(IAmmoItem ammoItem) {
		this(ammoItem, new ItemStack((Item) ammoItem));
	}

	@Override
	public boolean matches(@NotNull InventoryCrafting inv, @NotNull World worldIn) {
		int bulletsFound = 0;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack.isEmpty()) continue;

			stack = stack.copy();
			stack.setCount(1);

			if ((stack.getItem() instanceof IBulletItem || stack.getItem() == ammoItem) &&
					((IAmmoItem) stack.getItem()).getBulletType(stack) == ammoItem.getBulletType(testStack))
				bulletsFound += ammoItem.getEffectsFromItem(stack).size();
			else
				return false;
		}

		return bulletsFound > ammoItem.getMaxAmmo(testStack) && bulletsFound > ammoItem.getMinAmmo(testStack);
	}

	@NotNull
	@Override
	public ItemStack getCraftingResult(@NotNull InventoryCrafting inv) {
		List<BulletEffect> loadedAmmo = Lists.newArrayList();

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			stack = stack.copy();
			stack.setCount(1);

			if (stack.getItem() instanceof IBulletItem || stack.getItem() == ammoItem)
				loadedAmmo.addAll(((IAmmoItem) stack.getItem()).getEffectsFromItem(stack));
		}

		return ammoItem.setEffects(testStack.copy(), loadedAmmo);
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
		return testStack;
	}
}
