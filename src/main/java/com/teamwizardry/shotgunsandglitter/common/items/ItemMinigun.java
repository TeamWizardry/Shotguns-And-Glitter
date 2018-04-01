package com.teamwizardry.shotgunsandglitter.common.items;

import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.IGun;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

import static com.teamwizardry.shotgunsandglitter.common.items.ItemMagazine.addTooltipContents;


public class ItemMinigun extends ItemMod implements IGun {

	public ItemMinigun() {
		super("minigun");
		setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		addTooltipContents(stack, tooltip);
	}

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

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if (entityLiving instanceof EntityPlayer)
			((EntityPlayer) entityLiving).getCooldownTracker().setCooldown(stack.getItem(), 100);
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}

	@Override
	public boolean reloadAmmo(World world, EntityPlayer player, ItemStack gun, ItemStack ammo) {
		if (ammo.getItem() != ModItems.DRUM) return true;

		ItemStack realAmmo = ammo.copy();
		realAmmo.setCount(1);

		NBTTagList newAmmoList = ItemNBTHelper.getList(realAmmo, "ammo", Constants.NBT.TAG_STRING);
		if (newAmmoList == null) return true;

		NBTTagList loadedAmmo = ItemNBTHelper.getList(gun, "ammo", Constants.NBT.TAG_STRING);
		if (loadedAmmo == null) loadedAmmo = new NBTTagList();

		if (loadedAmmo.tagCount() >= getMaxAmmo()) return true;


		NBTTagList ammoToAdd = newAmmoList.copy();

		int removed = 0;

		for (int index = 0; index < ammoToAdd.tagCount(); index++) {
			if (loadedAmmo.tagCount() >= getMaxAmmo()) break;

			String effectID = ammoToAdd.getStringTagAt(index);

			loadedAmmo.appendTag(new NBTTagString(effectID));
			newAmmoList.removeTag(index - removed++);
		}

		if (newAmmoList.tagCount() == 0) {
			realAmmo.shrink(1);
		} else {
			player.addItemStackToInventory(realAmmo);
		}

		ammo.shrink(1);
		ItemNBTHelper.setList(gun, "ammo", loadedAmmo);

		setReloadCooldown(world, player, gun);
		return false;
	}

	@Override
	public BulletType getBulletType() {
		return BulletType.SMALL;
	}

	@Override
	public int getMaxAmmo() {
		return 200;
	}

	@Override
	public int getReloadCooldownTime() {
		return 100;
	}

	@Override
	public int getFireCooldownTime() {
		return 0;
	}

	@Override
	public float getInaccuracy() {
		return 10f;
	}

	@Override
	public SoundEvent[] getFireSoundEvents() {
		return new SoundEvent[]{ModSounds.SHOT_MINIGUN};
	}

	@Override
	public SoundEvent getReloadSoundEvent() {
		return ModSounds.RELOAD_PISTOL;
	}

	@Override
	public int headKnockStrength() {
		return 3;
	}
}
