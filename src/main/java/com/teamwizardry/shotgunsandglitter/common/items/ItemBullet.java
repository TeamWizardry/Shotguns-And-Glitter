package com.teamwizardry.shotgunsandglitter.common.items;

import com.teamwizardry.librarianlib.core.client.ModelHandler;
import com.teamwizardry.librarianlib.features.base.IExtraVariantHolder;
import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import com.teamwizardry.shotgunsandglitter.ShotgunsAndGlitter;
import com.teamwizardry.shotgunsandglitter.api.*;
import kotlin.jvm.functions.Function1;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;


public class ItemBullet extends ItemMod implements IExtraVariantHolder, IBulletItem {

	public ItemBullet() {
		super("bullet", Arrays.stream(BulletType.values()).map((type) -> "bullet_" + type.serializeName).toArray(String[]::new));
	}

	public static ItemStack getStackOfEffect(BulletType type, String effect) {
		return getStackOfEffect(type, effect, 1);
	}

	public static ItemStack getStackOfEffect(BulletType type, String effect, int count) {
		ItemStack stack = new ItemStack(ModItems.BULLET, count, type.ordinal());
		if (!EffectRegistry.getEffectByID(effect).getID().equals("basic"))
			ItemNBTHelper.setString(stack, "effect", effect);
		return stack;
	}

	@NotNull
	@Override
	public BulletType getBulletType(@NotNull ItemStack stack) {
		return BulletType.byOrdinal(stack.getItemDamage());
	}

	@NotNull
	@Override
	public String[] getExtraVariants() {
		return EffectRegistry.getEffects().stream()
				.flatMap((effect) -> Arrays.stream(BulletType.values())
						.map((bullet) -> bullet.serializeName + "/" + effect.getID()))
				.toArray(String[]::new);
	}

	@Nullable
	@Override
	@SideOnly(Side.CLIENT)
	public Function1<ItemStack, ModelResourceLocation> getMeshDefinition() {
		return (stack) -> {
			BulletType type = BulletType.byOrdinal(stack.getItemDamage());
			Effect effect = getEffectFromItem(stack);
			return ModelHandler.INSTANCE.getResource(ShotgunsAndGlitter.MODID,
					type.serializeName + "/" + effect.getID());
		};
	}

	@NotNull
	@Override
	public Effect getEffectFromItem(@NotNull ItemStack stack) {
		String effectID = ItemNBTHelper.getString(stack, "effect", "basic");
		return EffectRegistry.getEffectByID(effectID);
	}

	@NotNull
	@Override
	public String getUnlocalizedName(@NotNull ItemStack stack) {
		return super.getUnlocalizedName(stack) + "." + getEffectFromItem(stack).getID();
	}

	@Override
	public void getSubItems(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> subItems) {
		for (Effect effect : EffectRegistry.getEffects())
			for (BulletType type : BulletType.values())
				subItems.add(getStackOfEffect(type, effect.getID()));
	}
}
