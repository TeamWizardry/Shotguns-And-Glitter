package com.teamwizardry.shotgunsandglitter.common.items;

import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper;
import com.teamwizardry.shotgunsandglitter.ShotgunsAndGlitter;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.EffectRegistry;
import com.teamwizardry.shotgunsandglitter.api.IAmmo;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDrum extends ItemMod implements IAmmo {

	public ItemDrum() {
		super("drum");
		setMaxStackSize(1);
	}

	@SideOnly(Side.CLIENT)
	public static void addTooltipContents(ItemStack stack, List<String> tooltip) {
		NBTTagList loadedAmmo = ItemNBTHelper.getList(stack, "ammo", Constants.NBT.TAG_STRING);
		if (loadedAmmo == null) loadedAmmo = new NBTTagList();

		for (int i = 0; i < loadedAmmo.tagCount(); i++) {
			if (i > 5) {
				tooltip.add(TextFormatting.ITALIC.toString() + "... " + (loadedAmmo.tagCount() - 5) + "+");
				break;
			}
			TooltipHelper.addToTooltip(tooltip, ShotgunsAndGlitter.MODID + "." + loadedAmmo.getStringTagAt(i) + ".name");
		}

	}

	@NotNull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @NotNull EnumHand handIn) {
		onAmmoRightClick(worldIn, playerIn, handIn);
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		addTooltipContents(stack, tooltip);
	}

	@Override
	public void getSubItems(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> subItems) {
		if (tab == CreativeTabs.SEARCH) {
			for (Effect effect : EffectRegistry.getEffects()) {
				ItemStack stack = new ItemStack(this);
				NBTTagList list = new NBTTagList();
				for (int i = 0; i < 200; i++)
					list.appendTag(new NBTTagString(effect.getID()));
				ItemNBTHelper.setList(stack, "ammo", list);
				subItems.add(stack);
			}
		}
	}
}
