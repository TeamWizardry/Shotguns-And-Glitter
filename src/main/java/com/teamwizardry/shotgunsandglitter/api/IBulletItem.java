package com.teamwizardry.shotgunsandglitter.api;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author WireSegal
 * Created at 1:24 PM on 4/1/18.
 */
public interface IBulletItem extends IAmmoItem {
	Effect getEffectFromItem(ItemStack stack);

	@NotNull
	@Override
	default List<Effect> getEffectsFromItem(@NotNull ItemStack stack) {
		return Lists.newArrayList(getEffectFromItem(stack));
	}

	@Override
	default void takeEffectsFromItem(@NotNull ItemStack stack, int n) {
		stack.shrink(n);
	}
}
