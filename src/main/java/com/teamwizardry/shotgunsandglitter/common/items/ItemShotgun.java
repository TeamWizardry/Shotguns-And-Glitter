package com.teamwizardry.shotgunsandglitter.common.items;

import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.librarianlib.features.config.ConfigProperty;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.IGunItem;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class ItemShotgun extends ItemMod implements IGunItem {

	public ItemShotgun() {
		super("shotgun");
		setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @javax.annotation.Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		addTooltipContents(stack, tooltip);
	}

	@Override
	@NotNull
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @NotNull EnumHand handIn) {
		ItemStack offHand = playerIn.getHeldItemOffhand();
		ItemStack mainHand = playerIn.getHeldItemMainhand();

		if (reloadAmmo(worldIn, playerIn, mainHand, offHand))
			fireGun(worldIn, playerIn, playerIn.getHeldItem(handIn), handIn);
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public float getPotency() {
		return 0.6f;
	}

	@ConfigProperty(category = "shotgun", comment = "How many bullets a shotgun will fire per charge.")
	public static int shotgunFireCount = 5;

	@Override
	public int getFireCount(ItemStack stack) {
		return shotgunFireCount;
	}

	@Override
	public int headKnockStrength(ItemStack stack) {
		return 20;
	}

	@NotNull
	@Override
	public BulletType getBulletType(@NotNull ItemStack stack) {
		return BulletType.SHOTGUN;
	}

	@Override
	public int getMaxAmmo(@NotNull ItemStack stack) {
		return 1;
	}

	@Override
	public int getReloadCooldownTime(ItemStack stack) {
		return 10;
	}

	@Override
	public int getFireCooldownTime(ItemStack stack) {
		return 40;
	}

	@Override
	public float getInaccuracy(ItemStack stack) {
		return 15f;
	}

	@Nullable
	@Override
	public SoundEvent[] getFireSoundEvents(ItemStack stack) {
		return new SoundEvent[]{ModSounds.SHOT_SHOTGUN_COCK, ModSounds.MAGIC_SPARKLE};
	}

	@Nullable
	@Override
	public SoundEvent getReloadSoundEvent(ItemStack stack) {
		return ModSounds.RELOAD_SHOTGUN;
	}
}
