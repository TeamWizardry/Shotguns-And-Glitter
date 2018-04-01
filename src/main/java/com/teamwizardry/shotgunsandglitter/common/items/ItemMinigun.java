package com.teamwizardry.shotgunsandglitter.common.items;

import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.IGunItem;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;


public class ItemMinigun extends ItemMod implements IGunItem {

	public ItemMinigun() {
		super("minigun");
		setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		addTooltipContents(stack, tooltip);
	}

	@NotNull
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
		super.onUsingTick(stack, player, count);

		if (!(player instanceof EntityPlayer)) return;

		if (!reloadAmmo(player.world, (EntityPlayer) player, player.getHeldItemMainhand(), player.getHeldItemOffhand())) {
			fireGun(player.world, (EntityPlayer) player, player.getHeldItemMainhand(), player.getActiveHand());
		} else {
			player.swingArm(player.getActiveHand());
		}

	}

	@NotNull
	@Override
	public ItemStack onItemUseFinish(@NotNull ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if (entityLiving instanceof EntityPlayer)
			((EntityPlayer) entityLiving).getCooldownTracker().setCooldown(stack.getItem(), 100);
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}

	@NotNull
	@Override
	public BulletType getBulletType(@NotNull ItemStack stack) {
		return BulletType.SMALL;
	}

	@Override
	public int getMaxAmmo(ItemStack stack) {
		return 200;
	}

	@Override
	public int getReloadCooldownTime(ItemStack stack) {
		return 100;
	}

	@Override
	public int getFireCooldownTime(ItemStack stack) {
		return 0;
	}

	@Override
	public float getInaccuracy(ItemStack stack) {
		return 10f;
	}

	@Override
	public SoundEvent[] getFireSoundEvents(ItemStack stack) {
		return new SoundEvent[]{ModSounds.SHOT_MINIGUN};
	}

	@Override
	public SoundEvent getReloadSoundEvent(ItemStack stack) {
		return ModSounds.RELOAD_PISTOL;
	}

	@Override
	public int headKnockStrength(ItemStack stack) {
		return 3;
	}
}
