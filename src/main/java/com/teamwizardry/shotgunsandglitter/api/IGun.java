package com.teamwizardry.shotgunsandglitter.api;

import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.Nullable;

public interface IGun {

	boolean reloadAmmo(World world, EntityPlayer player, ItemStack gun, ItemStack ammo);

	BulletType getBulletType();

	int getMaxAmmo();

	int getReloadCooldownTime();

	int getFireCooldownTime();

	float getInaccuracy();

	@Nullable
	SoundEvent[] getFireSoundEvents();

	@Nullable
	SoundEvent getReloadSoundEvent();

	default void fireGun(World world, EntityPlayer player, ItemStack stack) {
		NBTTagList list = ItemNBTHelper.getList(stack, "ammo", Constants.NBT.TAG_STRING);
		if (list == null) list = new NBTTagList();
		if (list.tagCount() == 0) return;

		String effectID = list.getStringTagAt(list.tagCount() - 1);
		Effect effect = EffectRegistry.getEffectByID(effectID);

		list.removeTag(list.tagCount() - 1);
		ItemNBTHelper.setList(stack, "ammo", list);

		if (!world.isRemote) {
			EntityBullet bullet = new EntityBullet(world, player, getBulletType(), effect, getInaccuracy());
			bullet.setPosition(player.posX, player.posY + player.eyeHeight, player.posZ);
			world.spawnEntity(bullet);
		} else if (effect.getFireSound() != null) {
			world.playSound(player.posX, player.posY, player.posZ, effect.getFireSound(), SoundCategory.HOSTILE, RandUtil.nextFloat(0.95f, 1.1f), RandUtil.nextFloat(0.95f, 1.1f), false);
		}

		setFireCooldown(world, player, stack);
	}

	default void setFireCooldown(World world, EntityPlayer player, ItemStack stack) {
		player.getCooldownTracker().setCooldown(stack.getItem(), getFireCooldownTime());

		if (world.isRemote && getFireSoundEvents() != null)
			for (SoundEvent sound : getFireSoundEvents())
				world.playSound(player.posX, player.posY, player.posZ, sound, SoundCategory.HOSTILE, RandUtil.nextFloat(0.95f, 1.1f), RandUtil.nextFloat(0.95f, 1.1f), false);

	}

	default void setReloadCooldown(World world, EntityPlayer player, ItemStack stack) {
		player.getCooldownTracker().setCooldown(stack.getItem(), getReloadCooldownTime());

		if (world.isRemote && getReloadSoundEvent() != null)
			world.playSound(player.posX, player.posY, player.posZ, getReloadSoundEvent(), SoundCategory.HOSTILE, RandUtil.nextFloat(0.95f, 1.1f), RandUtil.nextFloat(0.95f, 1.1f), false);
	}
}
