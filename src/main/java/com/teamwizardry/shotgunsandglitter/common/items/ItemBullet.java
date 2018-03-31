package com.teamwizardry.shotgunsandglitter.common.items;

import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.EffectRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemBullet extends ItemMod {

	public ItemBullet() {
		super("bullet", "bullet_heavy", "bullet_medium", "bullet_light");

		this.addPropertyOverride(new ResourceLocation("effect"), new IItemPropertyGetter() {
			@SideOnly(Side.CLIENT)
			@Override
			public float apply(@Nonnull ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
				String effectID = ItemNBTHelper.getString(stack, "effect", "basic");

				Effect effect = EffectRegistry.getEffectByID(effectID);

				return EffectRegistry.getEffectIndex(effect);
			}
		});
	}

	@NotNull
	@Override
	public String getUnlocalizedName(@NotNull ItemStack stack) {
		String effectID = ItemNBTHelper.getString(stack, "effect", "basic");

		Effect effect = EffectRegistry.getEffectByID(effectID);

		return super.getUnlocalizedName(stack) + "." + effect.getID();
	}

	@Override
	public void getSubItems(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> subItems) {
		super.getSubItems(tab, subItems);

		for (int damage = 0; damage <= 2; damage++) {
			ItemStack base = new ItemStack(this);
			base.setItemDamage(damage);
			subItems.add(base);

			for (Effect effect : EffectRegistry.getEffects()) {
				ItemStack effectStack = base.copy();
				ItemNBTHelper.setString(effectStack, "effect", effect.getID());
				subItems.add(effectStack);
			}
		}
	}
}
