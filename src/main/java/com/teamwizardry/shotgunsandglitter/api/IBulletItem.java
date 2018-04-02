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
	BulletEffect getEffectFromItem(ItemStack stack);

	@NotNull
	@Override
	default List<BulletEffect> getEffectsFromItem(@NotNull ItemStack stack) {
		List<BulletEffect> outList = Lists.newArrayList();
		for (int i = 0; i < stack.getCount(); i++)
			outList.add(getEffectFromItem(stack));
		return outList;
	}

	@Override
	default int getMaxAmmo(@NotNull ItemStack stack) {
		return stack.getMaxStackSize();
	}

	@Override
	default void takeEffectsFromItem(@NotNull ItemStack stack, int n) {
		stack.shrink(n);
	}
}
