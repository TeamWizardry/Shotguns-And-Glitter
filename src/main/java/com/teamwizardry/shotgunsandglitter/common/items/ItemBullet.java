package com.teamwizardry.shotgunsandglitter.common.items;

import com.teamwizardry.librarianlib.core.client.ModelHandler;
import com.teamwizardry.librarianlib.features.base.IExtraVariantHolder;
import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.librarianlib.features.helpers.NBTHelper;
import com.teamwizardry.shotgunsandglitter.ShotgunsAndGlitter;
import com.teamwizardry.shotgunsandglitter.api.BulletEffect;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.EffectRegistry;
import com.teamwizardry.shotgunsandglitter.api.IBulletItem;
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
		if (!EffectRegistry.getBulletEffectByID(effect).getID().equals("basic"))
			NBTHelper.setString(stack, "effect", effect);
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
		return EffectRegistry.getBulletEffects().stream()
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
			BulletEffect bulletEffect = getEffectFromItem(stack);
			return ModelHandler.INSTANCE.getResource(ShotgunsAndGlitter.MODID,
					type.serializeName + "/" + bulletEffect.getID());
		};
	}

	@NotNull
	@Override
	public BulletEffect getEffectFromItem(@NotNull ItemStack stack) {
		String effectID = NBTHelper.hasNBTEntry(stack, "effect") ? NBTHelper.getString(stack, "effect") : "basic";
		return EffectRegistry.getBulletEffectByID(effectID);
	}

	@NotNull
	@Override
	public String getTranslationKey(@NotNull ItemStack stack) {
		return super.getTranslationKey(stack) + "." + getEffectFromItem(stack).getID();
	}

	@Override
	public void getSubItems(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> subItems) {
		if (isInCreativeTab(tab))
			for (BulletEffect bulletEffect : EffectRegistry.getBulletEffects())
				for (BulletType type : BulletType.values())
					subItems.add(getStackOfEffect(type, bulletEffect.getID()));
	}
}
