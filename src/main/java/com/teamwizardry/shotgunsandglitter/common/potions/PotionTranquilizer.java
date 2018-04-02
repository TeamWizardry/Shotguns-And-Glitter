package com.teamwizardry.shotgunsandglitter.common.potions;

import com.teamwizardry.librarianlib.features.base.PotionMod;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WireSegal
 * Created at 10:35 AM on 3/31/18.
 */
public class PotionTranquilizer extends PotionMod {
	public PotionTranquilizer(@NotNull String name, boolean badEffect, int color) {
		super(name, badEffect, color);

		MinecraftForge.EVENT_BUS.register(this);
		registerPotionAttributeModifier(SharedMonsterAttributes.KNOCKBACK_RESISTANCE, "F412C29C-0DB3-11E6-B4DD-7CEA70D5A8C7", 1.0, 0);
	}

	@Override
	public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBaseIn, @NotNull AbstractAttributeMap attributeMapIn, int amplifier) {
		if (entityLivingBaseIn instanceof EntityLiving)
			((EntityLiving) entityLivingBaseIn).setNoAI(true);
		super.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, amplifier);
	}

	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, @NotNull AbstractAttributeMap attributeMapIn, int amplifier) {
		if (entityLivingBaseIn instanceof EntityLiving)
			((EntityLiving) entityLivingBaseIn).setNoAI(false);
		super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
	}

	@Override
	public List<ItemStack> getCurativeItems() {
		return new ArrayList<>();
	}

	@SubscribeEvent
	public void onLivingTick(LivingEvent.LivingUpdateEvent e) {
		EntityLivingBase entity = e.getEntityLiving();
		if (hasEffect(entity)) {
			if (entity instanceof EntityLiving) {
				((EntityLiving) entity).setNoAI(true);
				if (entity.onGround) {
					entity.motionX = 0.0;
					entity.motionY = 0.0;
					entity.motionZ = 0.0;
					entity.onGround = false;
				}
			} else {
				if (entity.onGround)
					entity.setPosition(entity.prevPosX, entity.prevPosY, entity.prevPosZ);
				if (entity instanceof EntityPlayer &&
						((EntityPlayer) entity).capabilities.isFlying &&
						!((EntityPlayer) entity).isCreative())
					((EntityPlayer) entity).capabilities.isFlying = false;
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent e) {
		Minecraft minecraft = Minecraft.getMinecraft();
		EntityPlayer player = minecraft.player;
		if (player != null) {
			if (hasEffect(player)) {
				if (!player.getEntityData().hasKey("sng:smooth"))
					player.getEntityData().setBoolean("sng:smooth", minecraft.gameSettings.smoothCamera);
				minecraft.gameSettings.smoothCamera = true;
			} else {
				if (player.getEntityData().hasKey("sng:smooth")) {
					minecraft.gameSettings.smoothCamera = player.getEntityData().getBoolean("sng:smooth");
					player.getEntityData().removeTag("sng:smooth");
				}
			}
		}
	}


	@SubscribeEvent
	public void onJump(LivingEvent.LivingJumpEvent e) {
		EntityLivingBase entity = e.getEntityLiving();
		if (!(entity instanceof EntityLiving) && hasEffect(entity)) {
			entity.motionY = -1.0;
			entity.setPosition(entity.prevPosX, entity.prevPosY, entity.prevPosZ);
		}
	}
}
