package com.teamwizardry.shotgunsandglitter.common.items;

import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.EffectRegistry;
import com.teamwizardry.shotgunsandglitter.api.IAmmoItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDrum extends ItemMod implements IAmmoItem {

	public ItemDrum() {
		super("drum");
	}

	@Override
	public @NotNull BulletType getBulletType(@NotNull ItemStack stack) {
		return BulletType.PISTOL;
	}

	@Override
	public int getMaxAmmo(@NotNull ItemStack stack) {
		return 50;
	}

	@Override
	public int getMinAmmo(@NotNull ItemStack stack) {
		return 10;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		addTooltipContents(stack, tooltip);
	}

	@Override
	public void getSubItems(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> subItems) {
		if (tab == CreativeTabs.SEARCH) for (Effect effect : EffectRegistry.getEffects())
			subItems.add(fillEffects(new ItemStack(this), effect));
	}
}
