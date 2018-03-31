package com.teamwizardry.shotgunsandglitter.common.potions;

import com.teamwizardry.librarianlib.features.base.PotionMod;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author WireSegal
 * Created at 10:35 AM on 3/31/18.
 */
public class PotionFrost extends PotionMod {
	public PotionFrost(@NotNull String name, boolean badEffect, int color) {
		super(name, badEffect, color);

		MinecraftForge.EVENT_BUS.register(this);
		registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F182890", -0.15, 2);
	}

	@SubscribeEvent
	public void applySlipperiness(LivingEvent.LivingUpdateEvent e) {
		EntityLivingBase entity = e.getEntityLiving();
		if (hasEffect(entity)) {
			entity.motionX = MathHelper.clamp(entity.motionX * 0.98 / 0.6, -0.075, 0.075);
			entity.motionY = MathHelper.clamp(entity.motionY * 0.98 / 0.6, -0.075, 0.075);
			entity.motionZ = MathHelper.clamp(entity.motionZ * 0.98 / 0.6, -0.075, 0.075);
		}
	}
}
