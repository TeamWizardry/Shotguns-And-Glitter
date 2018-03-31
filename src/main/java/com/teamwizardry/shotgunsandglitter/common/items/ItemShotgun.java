package com.teamwizardry.shotgunsandglitter.common.items;

import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.shotgunsandglitter.ModSounds;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.EffectRegistry;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;


public class ItemShotgun extends ItemMod {

	public ItemShotgun() {
		super("shotgun");
	}

	@Override
	@NotNull
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @NotNull EnumHand handIn) {
		if (!worldIn.isRemote) {
			Effect firework = EffectRegistry.getEffectByID("piercing");
			EntityBullet bullet = new EntityBullet(worldIn, playerIn, BulletType.SMALL, firework, 0.05f);
			Vec3d position = playerIn.getPositionVector().addVector(0, playerIn.eyeHeight, 0).add(playerIn.getLook(0));
			bullet.setPosition(position.x, position.y, position.z);
			worldIn.spawnEntity(bullet);
		} else {
			worldIn.playSound(playerIn.posX, playerIn.posY, playerIn.posZ, ModSounds.SHOT_SHOTGUN, SoundCategory.HOSTILE, RandUtil.nextFloat(0.8f, 1f), RandUtil.nextFloat(0.8f, 1.2f), false);
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}
