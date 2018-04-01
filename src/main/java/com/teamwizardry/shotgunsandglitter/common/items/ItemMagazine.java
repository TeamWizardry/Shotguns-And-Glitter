package com.teamwizardry.shotgunsandglitter.common.items;

import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper;
import com.teamwizardry.shotgunsandglitter.ShotgunsAndGlitter;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemMagazine extends ItemMod {

	public ItemMagazine() {
		super("magazine");
	}

	@SideOnly(Side.CLIENT)
	public static void addTooltipContents(ItemStack stack, List<String> tooltip) {
		NBTTagList loadedAmmo = ItemNBTHelper.getList(stack, "ammo", Constants.NBT.TAG_STRING);
		if (loadedAmmo == null) loadedAmmo = new NBTTagList();

		for (NBTBase base : loadedAmmo) {
			if (!(base instanceof NBTTagString)) continue;
			TooltipHelper.addToTooltip(tooltip, ShotgunsAndGlitter.MODID + "." + ((NBTTagString) base).getString() + ".name");
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		addTooltipContents(stack, tooltip);
	}
}
