package com.teamwizardry.shotgunsandglitter.common.items;

import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.IGun;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class ItemSniper extends ItemMod implements IGun {

	public ItemSniper() {
		super("sniper");
		setMaxStackSize(1);
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
		if (ammo.getItem() != ModItems.BULLET || BulletType.byOrdinal(ammo.getItemDamage()) != BulletType.HEAVY)
			return false;

		NBTTagList loadedAmmo = ItemNBTHelper.getList(gun, "ammo", Constants.NBT.TAG_STRING);
		if (loadedAmmo == null) loadedAmmo = new NBTTagList();

		if (loadedAmmo.tagCount() >= getMaxAmmo()) return false;

		Effect effect = ItemBullet.getEffectFromItem(ammo);
		loadedAmmo.appendTag(new NBTTagString(effect.getID()));

		ammo.shrink(1);
		ItemNBTHelper.setList(gun, "ammo", loadedAmmo);

		setReloadCooldown(world, player, gun);
		return true;
	}

	@Override
	public BulletType getBulletType() {
		return BulletType.HEAVY;
	}

	@Override
	public int getMaxAmmo() {
		return 1;
	}

	@Override
	public int getReloadCooldownTime() {
		return 40;
	}

	@Override
	public int getFireCooldownTime() {
		return 50;
	}

	@Override
	public float getInaccuracy() {
		return 0;
	}

	@Nullable
	@Override
	public SoundEvent[] getFireSoundEvents() {
		return new SoundEvent[]{ModSounds.SHOT_SNIPER, ModSounds.DUST_SPARKLE};
	}

	@Nullable
	@Override
	public SoundEvent getReloadSoundEvent() {
		return ModSounds.RELOAD_SNIPER;
	}
}
