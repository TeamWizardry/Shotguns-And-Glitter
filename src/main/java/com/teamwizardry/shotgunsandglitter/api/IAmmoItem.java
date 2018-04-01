package com.teamwizardry.shotgunsandglitter.api;

import com.google.common.collect.Lists;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IAmmoItem {
	@SideOnly(Side.CLIENT)
	default void addTooltipContents(ItemStack stack, List<String> tooltip) {
		List<Effect> effects = getEffectsFromItem(stack);

		for (int i = 0; i < effects.size(); i++) {
			if (i > 5) {
				tooltip.add(TextFormatting.ITALIC.toString() + "... " + (effects.size() - 5) + "+");
				break;
			}
			TooltipHelper.addToTooltip(tooltip, "shotgunsandglitter." + effects.get(i).getID() + ".name");
		}

	}

	@NotNull
	default List<Effect> getEffectsFromItem(@NotNull ItemStack stack) {
		NBTTagList ammo = ItemNBTHelper.getList(stack, "ammo", Constants.NBT.TAG_STRING);
		if (ammo == null) return Lists.newArrayList();

		List<Effect> effects = Lists.newArrayList();
		for (int i = 0; i < stack.getCount(); i++) for (NBTBase effect : ammo)
			effects.add(EffectRegistry.getEffectByID(((NBTTagString) effect).getString()));

		return effects;
	}

	default void takeEffectsFromItem(@NotNull ItemStack stack, int n) {
		boolean canDestroy = destroyable(stack);

		NBTTagList ammo = ItemNBTHelper.getList(stack, "ammo", Constants.NBT.TAG_STRING);
		if (ammo == null) {
			if (canDestroy) stack.setCount(0);
		} else {
			int size = getEffectsFromItem(stack).size();

			int toTake = n;

			if (canDestroy) {
				stack.shrink(n / size);
				toTake = n % size;
			}

			if (!canDestroy || stack.getCount() == 1) {
				for (int i = 0; i < toTake && !ammo.hasNoTags(); i++)
					ammo.removeTag(0);

				if (ammo.hasNoTags()) {
					if (destroyable(stack))
						stack.setCount(0);
					else
						ItemNBTHelper.removeEntry(stack, "ammo");
				}
			} else stack.shrink(1);
		}
	}

	default boolean destroyable(@NotNull ItemStack stack) {
		return true;
	}

	default ItemStack fillEffects(ItemStack stack, Effect effect) {
		setEffects(stack, NonNullList.withSize(getMaxAmmo(stack), effect));
		return stack;
	}

	default ItemStack setEffects(ItemStack stack, List<Effect> effects) {
		NBTTagList ammo = new NBTTagList();
		ItemNBTHelper.setList(stack, "ammo", ammo);

		for (Effect effect : effects) {
			if (ammo.tagCount() >= getMaxAmmo(stack))
				break;
			ammo.appendTag(new NBTTagString(effect.getID()));
		}

		return stack;
	}

	int getMaxAmmo(@NotNull ItemStack stack);

	default int getMinAmmo(@NotNull ItemStack stack) {
		return 0;
	}

	@NotNull
	BulletType getBulletType(@NotNull ItemStack stack);
}
