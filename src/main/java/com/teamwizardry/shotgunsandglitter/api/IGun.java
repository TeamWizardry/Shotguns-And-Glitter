package com.teamwizardry.shotgunsandglitter.api;

import com.teamwizardry.librarianlib.features.animator.Easing;
import com.teamwizardry.librarianlib.features.animator.animations.BasicAnimation;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import com.teamwizardry.librarianlib.features.utilities.client.ClientRunnable;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.client.core.ClientEventHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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

	default void fireGun(World world, EntityPlayer player, ItemStack stack, EnumHand hand) {
		NBTTagList list = ItemNBTHelper.getList(stack, "ammo", Constants.NBT.TAG_STRING);
		if (list == null) list = new NBTTagList();
		if (list.tagCount() == 0) return;

		String effectID = list.getStringTagAt(list.tagCount() - 1);
		Effect effect = EffectRegistry.getEffectByID(effectID);

		list.removeTag(list.tagCount() - 1);
		ItemNBTHelper.setList(stack, "ammo", list);

		if (!world.isRemote) {
			IBulletEntity bullet = InternalHandler.INTERNAL_HANDLER.newBulletEntity(world, player, getBulletType(), effect, getInaccuracy());
			bullet.getAsEntity().setPosition(player.posX, player.posY + player.eyeHeight, player.posZ);
			world.spawnEntity(bullet.getAsEntity());
		} else if (effect.getFireSound() != null) {
			world.playSound(player.posX, player.posY, player.posZ, effect.getFireSound(), SoundCategory.PLAYERS, RandUtil.nextFloat(0.95f, 1.1f), RandUtil.nextFloat(0.95f, 1.1f), false);
		}

		setFireCooldown(world, player, stack);
		player.swingArm(hand);

		Vec3d normal = player.getLook(0);
		player.motionX = -normal.x * getBulletType().knockbackStrength;
		player.motionY = -normal.y * getBulletType().knockbackStrength;
		player.motionZ = -normal.z * getBulletType().knockbackStrength;

		ClientRunnable.run(new ClientRunnable() {
			@Override
			@SideOnly(Side.CLIENT)
			public void runIfClient() {
				BasicAnimation<EntityPlayer> anim = new BasicAnimation<>(player, "rotationPitch");
				anim.setDuration(2);
				anim.setTo(player.rotationPitch - headKnockStrength());
				anim.setEasing(Easing.easeOutCubic);
				ClientEventHandler.FLASH_ANIMATION_HANDLER.add(anim);
			}
		});
	}

	default void setFireCooldown(World world, EntityPlayer player, ItemStack stack) {
		if (getFireCooldownTime() > 0)
			player.getCooldownTracker().setCooldown(stack.getItem(), getFireCooldownTime());

		if (world.isRemote && getFireSoundEvents() != null)
			for (SoundEvent sound : getFireSoundEvents())
				world.playSound(player.posX, player.posY, player.posZ, sound, SoundCategory.PLAYERS, RandUtil.nextFloat(3f, 4f), RandUtil.nextFloat(0.95f, 1.1f), false);

	}

	default void setReloadCooldown(World world, EntityPlayer player, ItemStack stack) {
		if (getReloadCooldownTime() <= 0) return;

		player.getCooldownTracker().setCooldown(stack.getItem(), getReloadCooldownTime());

		if (world.isRemote && getReloadSoundEvent() != null)
			world.playSound(player.posX, player.posY, player.posZ, getReloadSoundEvent(), SoundCategory.PLAYERS, RandUtil.nextFloat(0.95f, 1.1f), RandUtil.nextFloat(0.95f, 1.1f), false);
	}

	int headKnockStrength();
}
