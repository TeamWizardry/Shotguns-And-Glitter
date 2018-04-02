package com.teamwizardry.shotgunsandglitter.common.items;

import com.teamwizardry.librarianlib.core.client.ModelHandler;
import com.teamwizardry.librarianlib.features.base.IExtraVariantHolder;
import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import com.teamwizardry.shotgunsandglitter.ShotgunsAndGlitter;
import com.teamwizardry.shotgunsandglitter.api.EffectRegistry;
import com.teamwizardry.shotgunsandglitter.api.GrenadeEffect;
import com.teamwizardry.shotgunsandglitter.api.IGrenadeItem;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityGrenade;
import kotlin.jvm.functions.Function1;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import static net.minecraft.item.ItemBow.getArrowVelocity;


public class ItemGrenade extends ItemMod implements IExtraVariantHolder, IGrenadeItem {

	public ItemGrenade() {
		super("grenade");
	}

	public static ItemStack getStackOfEffect(String effect) {
		return getStackOfEffect(effect, 1);
	}

	public static ItemStack getStackOfEffect(String effect, int count) {
		ItemStack stack = new ItemStack(ModItems.GRENADE, count);
		if (!EffectRegistry.getGrenadeEffectByID(effect).getID().equals("basic"))
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
						EntityGrenade entityGrenade = new EntityGrenade(world, entityplayer, getEffectFromStack(stack));
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

	@NotNull
	@Override
	public String[] getExtraVariants() {
		return EffectRegistry.getGrenadeEffects().stream()
				.map((effect) -> "grenade/" + effect.getID())
				.toArray(String[]::new);
	}

	@Nullable
	@Override
	@SideOnly(Side.CLIENT)
	public Function1<ItemStack, ModelResourceLocation> getMeshDefinition() {
		return (stack) -> {
			GrenadeEffect effect = getEffectFromStack(stack);
			return ModelHandler.INSTANCE.getResource(ShotgunsAndGlitter.MODID, "grenade/" + effect.getID());
		};
	}

	@NotNull
	@Override
	public String getUnlocalizedName(@NotNull ItemStack stack) {
		return super.getUnlocalizedName(stack) + "." + getEffectFromStack(stack).getID();
	}

	@Override
	public void getSubItems(@NotNull CreativeTabs tab, @NotNull NonNullList<ItemStack> subItems) {
		for (GrenadeEffect effect : EffectRegistry.getGrenadeEffects())
			subItems.add(getStackOfEffect(effect.getID()));
	}
}
