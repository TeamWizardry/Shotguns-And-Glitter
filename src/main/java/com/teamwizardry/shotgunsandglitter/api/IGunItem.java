package com.teamwizardry.shotgunsandglitter.api;

import com.teamwizardry.librarianlib.features.animator.Easing;
import com.teamwizardry.librarianlib.features.animator.animations.BasicAnimation;
import com.teamwizardry.librarianlib.features.utilities.client.ClientRunnable;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IGunItem extends IAmmoItem {

	int getReloadCooldownTime(ItemStack stack);

	int getFireCooldownTime(ItemStack stack);

	float getInaccuracy(ItemStack stack);

	int headKnockStrength(ItemStack stack);

	@Nullable
	SoundEvent[] getFireSoundEvents(ItemStack stack);

	@Nullable
	SoundEvent getReloadSoundEvent(ItemStack stack);

	@Override
	default boolean destroyable(@NotNull ItemStack stack) {
		return false;
	}

	default int getFireCount(ItemStack stack) {
		return 1;
	}

	default boolean shouldConsumeAmmo(ItemStack stack, int firedPreviouslyThisShot) {
		return firedPreviouslyThisShot == 0;
	}

	default void fireGun(World world, EntityPlayer player, ItemStack stack, EnumHand hand) {
		List<Effect> ammo = getEffectsFromItem(stack);

		if (ammo.isEmpty()) return;

		int consumed = 0;

		Effect effect = ammo.get(0);

		if (!world.isRemote) {
			for (int i = 0; i < getFireCount(stack); i++) {
				if (shouldConsumeAmmo(stack, i)) {
					if (ammo.size() == consumed + 1)
						break;
					effect = ammo.get(consumed++);
				}
				IBulletEntity bullet = InternalHandler.INTERNAL_HANDLER.newBulletEntity(world, player, getBulletType(stack), effect, getInaccuracy(stack));
				bullet.getAsEntity().setPosition(player.posX, player.posY + player.eyeHeight, player.posZ);
				world.spawnEntity(bullet.getAsEntity());
			}
		} else if (effect.getFireSound() != null)
			world.playSound(player.posX, player.posY, player.posZ, effect.getFireSound(), SoundCategory.PLAYERS, RandUtil.nextFloat(0.95f, 1.1f), RandUtil.nextFloat(0.95f, 1.1f), false);

		takeEffectsFromItem(stack, consumed);

		setFireCooldown(world, player, stack);
		player.swingArm(hand);

		Vec3d normal = player.getLook(0);
		player.motionX = -normal.x * getBulletType(stack).knockbackStrength;
		player.motionY = -normal.y * getBulletType(stack).knockbackStrength;
		player.motionZ = -normal.z * getBulletType(stack).knockbackStrength;

		ClientRunnable.run(new ClientRunnable() {
			@Override
			@SideOnly(Side.CLIENT)
			public void runIfClient() {
				EntityPlayer clientPlayer = Minecraft.getMinecraft().player;
				if (clientPlayer == null) return;
				BasicAnimation<EntityPlayer> anim = new BasicAnimation<>(clientPlayer, "rotationPitch");
				anim.setDuration(2);
				anim.setTo(Minecraft.getMinecraft().player.rotationPitch - headKnockStrength(stack));
				anim.setEasing(Easing.easeOutCubic);
				InternalHandler.INTERNAL_HANDLER.addTiltAnimation(anim);
			}
		});
	}

	default boolean reloadAmmo(World world, EntityPlayer player, ItemStack gun, ItemStack ammo) {
		if (!(ammo.getItem() instanceof IAmmoItem) || ammo.getItem() instanceof IGunItem) return true;

		IAmmoItem ammoItem = (IAmmoItem) ammo.getItem();
		if (ammoItem.getBulletType(ammo) != getBulletType(gun)) return true;

		List<Effect> gunAmmo = getEffectsFromItem(gun);

		if (!gunAmmo.isEmpty()) return true;

		List<Effect> ammoEffects = ((IAmmoItem) ammo.getItem()).getEffectsFromItem(ammo);

		for (int index = 0; index < Math.max(ammoEffects.size(), getMaxAmmo(gun)); index++)
			gunAmmo.add(ammoEffects.get(index));

		ammoItem.takeEffectsFromItem(ammo, Math.max(ammoEffects.size(), getMaxAmmo(gun)));
		setEffects(gun, gunAmmo);

		setReloadCooldown(world, player, gun);
		return false;
	}

	default void setFireCooldown(World world, EntityPlayer player, ItemStack stack) {
		if (getFireCooldownTime(stack) > 0)
			player.getCooldownTracker().setCooldown(stack.getItem(), getFireCooldownTime(stack));

		if (world.isRemote && getFireSoundEvents(stack) != null) {
			SoundEvent[] events = getFireSoundEvents(stack);
			if (events != null) for (SoundEvent sound : events)
				world.playSound(player.posX, player.posY, player.posZ, sound, SoundCategory.PLAYERS, RandUtil.nextFloat(3f, 4f), RandUtil.nextFloat(0.95f, 1.1f), false);
		}
	}

	default void setReloadCooldown(World world, EntityPlayer player, ItemStack stack) {
		if (getReloadCooldownTime(stack) <= 0) return;

		player.getCooldownTracker().setCooldown(stack.getItem(), getReloadCooldownTime(stack));

		if (world.isRemote && getReloadSoundEvent(stack) != null)
			world.playSound(player.posX, player.posY, player.posZ, getReloadSoundEvent(stack), SoundCategory.PLAYERS, RandUtil.nextFloat(0.95f, 1.1f), RandUtil.nextFloat(0.95f, 1.1f), false);
	}
}
