package com.teamwizardry.shotgunsandglitter.api;

import com.teamwizardry.librarianlib.core.LibrarianLib;
import com.teamwizardry.librarianlib.features.animator.Easing;
import com.teamwizardry.librarianlib.features.animator.animations.BasicAnimation;
import com.teamwizardry.librarianlib.features.utilities.client.ClientRunnable;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
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

	default float getPotency() {
		return 1;
	}

	default void fireGun(World world, EntityPlayer player, ItemStack stack, EnumHand hand) {
		List<BulletEffect> ammo = getEffectsFromItem(stack);

		if (ammo.isEmpty()) return;

		int consumed = 0;

		BulletEffect bulletEffect = ammo.get(0);

		if (!world.isRemote) {
			for (int i = 0; i < getFireCount(stack); i++) {
				if (shouldConsumeAmmo(stack, i)) {
					if (ammo.size() <= consumed)
						break;
					bulletEffect = ammo.get(consumed++);
				}
				IBulletEntity bullet = InternalHandler.INTERNAL_HANDLER.newBulletEntity(world, player, getBulletType(stack), bulletEffect, getInaccuracy(stack), getPotency());
				bullet.getAsEntity().setPosition(player.posX, player.posY + player.eyeHeight, player.posZ);
				world.spawnEntity(bullet.getAsEntity());
			}
		}

		SoundSystem.playSounds(world, player.getPositionVector(), bulletEffect.getFireSound());

		if (!player.isCreative())
			takeEffectsFromItem(stack, consumed);

		setFireCooldown(world, player, stack);
		player.swingArm(hand);

		Vec3d normal = player.getLook(0);
		player.motionX += -normal.x * (getBulletType(stack).getKnockbackStrength() / 2.0);
		player.motionY += -normal.y * (getBulletType(stack).getKnockbackStrength() / 10.0);
		player.motionZ += -normal.z * (getBulletType(stack).getKnockbackStrength() / 2.0);

		if (player.rotationPitch < (-90 + headKnockStrength(stack)))
			player.attackEntityFrom(DamageSource.causeMobDamage(player), 2f);

		List<BulletEffect> gunAmmo = getEffectsFromItem(stack);
		player.sendStatusMessage(new TextComponentTranslation(gunAmmo.size() + " / " + getMaxAmmo(stack)), true);

		ClientRunnable.run(new ClientRunnable() {
			@Override
			@SideOnly(Side.CLIENT)
			public void runIfClient() {
				EntityPlayer clientPlayer = Minecraft.getMinecraft().player;
				if (clientPlayer == null) return;
				BasicAnimation<EntityPlayer> anim = new BasicAnimation<>(clientPlayer, LibrarianLib.DEV_ENVIRONMENT ? "rotationPitch" : "field_70125_A");
				anim.setDuration(2);
				anim.setTo(Minecraft.getMinecraft().player.rotationPitch - headKnockStrength(stack));
				anim.setEasing(Easing.easeInExpo);
				InternalHandler.INTERNAL_HANDLER.addTiltAnimation(anim);
			}
		});
	}

	default boolean isValidAmmo(IAmmoItem ammoItem, ItemStack gun, ItemStack ammo) {
		return ammoItem.getBulletType(ammo) == getBulletType(gun);
	}

	default boolean reloadAmmo(World world, EntityPlayer player, ItemStack gun, ItemStack ammo) {
		if (!(ammo.getItem() instanceof IAmmoItem) || ammo.getItem() instanceof IGunItem) return true;

		IAmmoItem ammoItem = (IAmmoItem) ammo.getItem();
		if (!isValidAmmo(ammoItem, gun, ammo)) return true;

		List<BulletEffect> gunAmmo = getEffectsFromItem(gun);

		if (!gunAmmo.isEmpty()) return true;

		List<BulletEffect> ammoBulletEffects = ((IAmmoItem) ammo.getItem()).getEffectsFromItem(ammo);

		for (int index = 0; index < Math.min(ammoBulletEffects.size(), getMaxAmmo(gun)); index++)
			gunAmmo.add(ammoBulletEffects.get(index));

		ammoItem.takeEffectsFromItem(ammo, Math.min(ammoBulletEffects.size(), getMaxAmmo(gun)));
		setEffects(gun, gunAmmo);

		setReloadCooldown(world, player, gun);
		return false;
	}

	default void setFireCooldown(World world, EntityPlayer player, ItemStack stack) {
		if (getFireCooldownTime(stack) > 0)
			player.getCooldownTracker().setCooldown(stack.getItem(), getFireCooldownTime(stack));

		SoundSystem.playSounds(world, player.getPositionVector(), getFireSoundEvents(stack));
	}

	default void setReloadCooldown(World world, EntityPlayer player, ItemStack stack) {
		if (getReloadCooldownTime(stack) <= 0) return;

		player.getCooldownTracker().setCooldown(stack.getItem(), getReloadCooldownTime(stack));

		SoundSystem.playSounds(world, player.getPositionVector(), getReloadSoundEvent(stack));

	}
}
