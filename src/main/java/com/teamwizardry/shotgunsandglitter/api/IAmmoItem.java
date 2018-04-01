package com.teamwizardry.shotgunsandglitter.api;

import com.google.common.collect.Lists;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper;
import com.teamwizardry.shotgunsandglitter.ShotgunsAndGlitter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
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
			TooltipHelper.addToTooltip(tooltip, ShotgunsAndGlitter.MODID + "." + effects.get(i).getID() + ".name");
		}

	}

	@NotNull
	default List<Effect> getEffectsFromItem(@NotNull ItemStack stack) {
		NBTTagList ammo = ItemNBTHelper.getList(stack, "ammo", Constants.NBT.TAG_STRING);
		if (ammo == null) return Lists.newArrayList();

		List<Effect> effects = Lists.newArrayList();
		for (NBTBase effect : ammo)
			effects.add(EffectRegistry.getEffectByID(((NBTTagString) effect).getString()));

		return effects;
	}

	default void takeEffectsFromItem(@NotNull ItemStack stack, int n) {
		NBTTagList ammo = ItemNBTHelper.getList(stack, "ammo", Constants.NBT.TAG_STRING);
		if (ammo == null)
			stack.setCount(0);
		else {
			for (int i = 0; i < n && !ammo.hasNoTags(); i++)
				ammo.removeTag(0);

			if (ammo.hasNoTags())
				stack.setCount(0);
		}
	}

	@NotNull
	BulletType getBulletType(@NotNull ItemStack stack);
}
