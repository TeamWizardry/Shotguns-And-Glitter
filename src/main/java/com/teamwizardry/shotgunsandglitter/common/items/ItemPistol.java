package com.teamwizardry.shotgunsandglitter.common.items;

import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.IGun;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;


public class ItemPistol extends ItemMod implements IGun {

	public ItemPistol() {
		super("pistol");
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);


		NBTTagList loadedAmmo = ItemNBTHelper.getList(stack, "ammo", Constants.NBT.TAG_STRING);
		if (loadedAmmo == null) loadedAmmo = new NBTTagList();

		for (NBTBase base : loadedAmmo) {
			if (!(base instanceof NBTTagString)) continue;
			tooltip.add(((NBTTagString) base).getString());
		}
	}

	@Override
	@NotNull
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @NotNull EnumHand handIn) {
		ItemStack offHand = playerIn.getHeldItemOffhand();
		ItemStack mainHand = playerIn.getHeldItemMainhand();

		if (!reloadAmmo(worldIn, playerIn, mainHand, offHand)) {
			fireGun(worldIn, playerIn, playerIn.getHeldItemMainhand());
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	@Override
	public boolean reloadAmmo(World world, EntityPlayer player, ItemStack gun, ItemStack ammo) {
		if (ammo.getItem() != ModItems.MAGAZINE) return false;

		NBTTagList newAmmoList = ItemNBTHelper.getList(ammo, "ammo", Constants.NBT.TAG_STRING);
		if (newAmmoList == null) return false;

		NBTTagList loadedAmmo = ItemNBTHelper.getList(gun, "ammo", Constants.NBT.TAG_STRING);
		if (loadedAmmo == null) loadedAmmo = new NBTTagList();

		if (loadedAmmo.tagCount() >= getMaxAmmo()) return false;

		for (int j = 0; j < newAmmoList.tagCount(); j++) {
			if (loadedAmmo.tagCount() >= getMaxAmmo()) break;

			String effectID = newAmmoList.getStringTagAt(j);

			loadedAmmo.appendTag(new NBTTagString(effectID));
			newAmmoList.removeTag(j);
		}

		if (newAmmoList.tagCount() <= 0) {
			ammo.setCount(0);
		}

		ItemNBTHelper.setList(gun, "ammo", loadedAmmo);

		setReloadCooldown(world, player, gun);
		return true;

		//if (getBulletType() != BulletType.byOrdinal(ammo.getItemDamage())) return false;
//
		//NBTTagList list = ItemNBTHelper.getList(gun, "ammo", Constants.NBT.TAG_STRING);
		//if (list == null) list = new NBTTagList();
//
		//if (list.tagCount() >= maxAmmo()) return false;
		//Effect effect = ItemBullet.getEffectFromItem(ammo);
//
		//list.appendTag(new NBTTagString(effect.getID()));
		//ItemNBTHelper.setList(ammo, "ammo", list);
//
		//reloadCooldown(world, player, gun);
		//return true;
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
		return 20;
	}

	@Override
	public int getFireCooldownTime() {
		return 10;
	}

	@Override
	public float getInaccuracy() {
		return 0.05f;
	}

	@Override
	public SoundEvent[] getFireSoundEvents() {
		return new SoundEvent[]{ModSounds.SHOT_PISTOL, ModSounds.MAGIC_SPARKLE};
	}

	@Override
	public SoundEvent getReloadSoundEvent() {
		return null;
	}
}
