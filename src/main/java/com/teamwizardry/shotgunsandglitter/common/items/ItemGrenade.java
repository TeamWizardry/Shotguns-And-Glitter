package com.teamwizardry.shotgunsandglitter.common.items;

import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.EffectRegistry;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityGrenade;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static net.minecraft.item.ItemBow.getArrowVelocity;


public class ItemGrenade extends ItemMod { //TODO: implements IExtraVariantHolder {

	public ItemGrenade() {
		super("grenade");
	}

	// TODO
	public static ItemStack getStackOfEffect(BulletType type, String effect) {
		return getStackOfEffect(type, effect, 1);
	}

	// TODO
	public static ItemStack getStackOfEffect(BulletType type, String effect, int count) {
		ItemStack stack = new ItemStack(ModItems.BULLET, count, type.ordinal());
		if (!EffectRegistry.getEffectByID(effect).getID().equals("basic"))
			ItemNBTHelper.setString(stack, "effect", effect);
		return stack;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) entityLiving;
			boolean shouldRemoveStack = entityplayer.capabilities.isCreativeMode;

			int i = this.getMaxItemUseDuration(stack) - timeLeft;

			if (!stack.isEmpty() || shouldRemoveStack) {

				float f = getArrowVelocity(i);
				if ((double) f >= 0.5D) {
					boolean isCreativeMode = entityplayer.capabilities.isCreativeMode;

					if (!world.isRemote) {
						EntityGrenade entityGrenade = new EntityGrenade(world, entityplayer);
						world.spawnEntity(entityGrenade);
					}

					world.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
					world.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, ModSounds.MAGIC_SPARKLE, SoundCategory.NEUTRAL, RandUtil.nextFloat(0.9f, 1.1f), RandUtil.nextFloat(0.9f, 1.1f));

					if (!isCreativeMode) stack.shrink(1);
				}
			}
		}
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand hand) {
		ItemStack stack = playerIn.getHeldItem(hand);
		if (worldIn.isRemote && (Minecraft.getMinecraft().currentScreen != null))
			return new ActionResult<>(EnumActionResult.FAIL, stack);
		else {
			playerIn.setActiveHand(hand);
			return new ActionResult<>(EnumActionResult.PASS, stack);
		}
	}

	@Nonnull
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	// TODO
	//@NotNull
	//@Override
	//public String[] getExtraVariants() {
	//	return EffectRegistry.getEffects().stream()
	//			.flatMap((effect) -> Arrays.stream(BulletType.values())
	//					.map((bullet) -> bullet.serializeName + "/" + effect.getID()))
	//			.toArray(String[]::new);
	//}

	// TODO
	//@Nullable
	//@Override
	//@SideOnly(Side.CLIENT)
	//public Function1<ItemStack, ModelResourceLocation> getMeshDefinition() {
	//	return (stack) -> {
	//		BulletType type = BulletType.byOrdinal(stack.getItemDamage());
	//		Effect effect = getEffectFromItem(stack);
	//		return ModelHandler.INSTANCE.getResource(ShotgunsAndGlitter.MODID,
	//				type.serializeName + "/" + effect.getID());
	//	};
	//}

	// TODO
	//@NotNull
	//@Override
	//public Effect getEffectFromItem(@NotNull ItemStack stack) {
	//	String effectID = ItemNBTHelper.getString(stack, "effect", "basic");
	//	return EffectRegistry.getEffectByID(effectID);
	//}

	// TODO
	//@NotNull
	//@Override
	//public String getUnlocalizedName(@NotNull ItemStack stack) {
	//	return super.getUnlocalizedName(stack) + "." + getEffectFromItem(stack).getID();
	//}

	// TODO
	//@Override
	//public void getSubItems(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> subItems) {
	//	for (Effect effect : EffectRegistry.getEffects())
	//		for (BulletType type : BulletType.values())
	//			subItems.add(getStackOfEffect(type, effect.getID()));
	//}
}
