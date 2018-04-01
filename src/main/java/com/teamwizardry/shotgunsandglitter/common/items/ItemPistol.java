package com.teamwizardry.shotgunsandglitter.common.items;

import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.IGun;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

import static com.teamwizardry.shotgunsandglitter.common.items.ItemMagazine.addTooltipContents;


public class ItemPistol extends ItemMod implements IGun {

	public ItemPistol() {
		super("pistol");
		setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		addTooltipContents(stack, tooltip);
	}

	@Override
	@NotNull
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @NotNull EnumHand handIn) {
		ItemStack offHand = playerIn.getHeldItemOffhand();
		ItemStack mainHand = playerIn.getHeldItemMainhand();

		if (reloadAmmo(worldIn, playerIn, mainHand, offHand)) {
			fireGun(worldIn, playerIn, playerIn.getHeldItem(handIn), handIn);
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public boolean reloadAmmo(World world, EntityPlayer player, ItemStack gun, ItemStack ammo) {
		if (ammo.getItem() != ModItems.MAGAZINE) return true;

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
		return 5;
	}

	@Override
	public int getReloadCooldownTime() {
		return 40;
	}

	@Override
	public int getFireCooldownTime() {
		return 10;
	}

	@Override
	public float getInaccuracy() {
		return 4f;
	}

	@Override
	public SoundEvent[] getFireSoundEvents() {
		return new SoundEvent[]{ModSounds.SHOT_PISTOL, ModSounds.MAGIC_SPARKLE};
	}

	@Override
	public SoundEvent getReloadSoundEvent() {
		return ModSounds.RELOAD_PISTOL;
	}
}
