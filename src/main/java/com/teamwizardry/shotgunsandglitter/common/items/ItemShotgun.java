package com.teamwizardry.shotgunsandglitter.common.items;

import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.EffectRegistry;
import com.teamwizardry.shotgunsandglitter.api.IGun;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class ItemShotgun extends ItemMod implements IGun {

	public ItemShotgun() {
		super("shotgun");
		setMaxStackSize(1);
	}

	@Override
	@NotNull
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @NotNull EnumHand handIn) {
		ItemStack offHand = playerIn.getHeldItemOffhand();
		ItemStack mainHand = playerIn.getHeldItemMainhand();

		if (reloadAmmo(worldIn, playerIn, mainHand, offHand)) {
			fireGun(worldIn, playerIn, playerIn.getHeldItemMainhand());
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}


	@Override
	public void fireGun(World world, EntityPlayer player, ItemStack stack) {
		NBTTagList list = ItemNBTHelper.getList(stack, "ammo", Constants.NBT.TAG_STRING);
		if (list == null) list = new NBTTagList();
		if (list.tagCount() == 0) return;

		String effectID = list.getStringTagAt(list.tagCount() - 1);
		Effect effect = EffectRegistry.getEffectByID(effectID);

		list.removeTag(list.tagCount() - 1);
		ItemNBTHelper.setList(stack, "ammo", list);

		if (!world.isRemote) {
			for (int i = 0; i < 5; i++) {
				EntityBullet bullet = new EntityBullet(world, player, getBulletType(), effect, getInaccuracy());
				bullet.setPosition(player.posX, player.posY + player.eyeHeight, player.posZ);
				world.spawnEntity(bullet);
			}
		} else if (effect.getFireSound() != null) {
			world.playSound(player.posX, player.posY, player.posZ, effect.getFireSound(), SoundCategory.PLAYERS, RandUtil.nextFloat(0.95f, 1.1f), RandUtil.nextFloat(0.95f, 1.1f), false);
		}

		setFireCooldown(world, player, stack);
	}

	@Override
	public boolean reloadAmmo(World world, EntityPlayer player, ItemStack gun, ItemStack ammo) {
		if (ammo.getItem() != ModItems.BULLET || BulletType.byOrdinal(ammo.getItemDamage()) != BulletType.MEDIUM)
			return true;

		NBTTagList loadedAmmo = ItemNBTHelper.getList(gun, "ammo", Constants.NBT.TAG_STRING);
		if (loadedAmmo == null) loadedAmmo = new NBTTagList();

		if (loadedAmmo.tagCount() >= getMaxAmmo()) return true;

		Effect effect = ItemBullet.getEffectFromItem(ammo);
		loadedAmmo.appendTag(new NBTTagString(effect.getID()));

		ammo.shrink(1);
		ItemNBTHelper.setList(gun, "ammo", loadedAmmo);

		setReloadCooldown(world, player, gun);
		return false;
	}

	@Override
	public BulletType getBulletType() {
		return BulletType.MEDIUM;
	}

	@Override
	public int getMaxAmmo() {
		return 4;
	}

	@Override
	public int getReloadCooldownTime() {
		return 10;
	}

	@Override
	public int getFireCooldownTime() {
		return 40;
	}

	@Override
	public float getInaccuracy() {
		return 25f;
	}

	@Nullable
	@Override
	public SoundEvent[] getFireSoundEvents() {
		return new SoundEvent[]{ModSounds.SHOT_SHOTGUN_COCK, ModSounds.MAGIC_SPARKLE};
	}

	@Nullable
	@Override
	public SoundEvent getReloadSoundEvent() {
		return ModSounds.RELOAD_SHOTGUN;
	}
}
