package com.teamwizardry.shotgunsandglitter.common.items;

import com.teamwizardry.librarianlib.core.client.ModelHandler;
import com.teamwizardry.librarianlib.features.base.IExtraVariantHolder;
import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import com.teamwizardry.shotgunsandglitter.ShotgunsAndGlitter;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.EffectRegistry;
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


public class ItemBullet extends ItemMod implements IExtraVariantHolder {

	public ItemBullet() {
		super("bullet", Arrays.stream(BulletType.values()).map((type) -> "bullet_" + type.serializeName).toArray(String[]::new));
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
	public static Effect getEffectFromItem(@NotNull ItemStack stack) {
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
		super.getSubItems(tab, subItems);

		for (int damage = 0; damage < getVariants().length; damage++) {
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
