package com.teamwizardry.shotgunsandglitter.common.items;

import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.EffectRegistry;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemPistol extends ItemMod {

	public ItemPistol() {
		super("pistol");
	}

	@Override
	@Nonnull
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
		if (worldIn.isRemote) return super.onItemRightClick(worldIn, playerIn, handIn);

		Effect firework = EffectRegistry.getEffectByID("gravity_out");
		if (firework != null) {
			EntityBullet bullet = new EntityBullet(worldIn, playerIn, BulletType.SMALL, firework, 0.05f);
			Vec3d position = playerIn.getPositionVector().addVector(0, playerIn.eyeHeight, 0).add(playerIn.getLook(0));
			bullet.setPosition(position.x, position.y, position.z);
			worldIn.spawnEntity(bullet);
		}

		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}
