package com.teamwizardry.shotgunsandglitter.common.items;

import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;


public class ItemSniper extends ItemMod {

	public ItemSniper() {
		super("sniper");
	}

	@Override
	@NotNull
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @NotNull EnumHand handIn) {
		if (!worldIn.isRemote) {
			// Temporary code!
			ItemStack bulletStack = playerIn.getHeldItem(handIn == EnumHand.OFF_HAND ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
			if (bulletStack.getItem() == ModItems.BULLET) {
				Effect effect = ItemBullet.getEffectFromItem(bulletStack);
				BulletType bulletType = BulletType.byOrdinal(bulletStack.getItemDamage());
				// End temporary code!
				EntityBullet bullet = new EntityBullet(worldIn, playerIn, bulletType, effect, 0.05f);
				worldIn.spawnEntity(bullet);
			}
		} else {
			worldIn.playSound(playerIn.posX, playerIn.posY, playerIn.posZ, ModSounds.SHOT_PISTOL, SoundCategory.HOSTILE, RandUtil.nextFloat(0.8f, 1f), RandUtil.nextFloat(0.8f, 1.2f), false);
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}
