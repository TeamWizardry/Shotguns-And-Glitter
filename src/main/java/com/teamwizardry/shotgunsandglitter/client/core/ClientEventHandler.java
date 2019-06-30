package com.teamwizardry.shotgunsandglitter.client.core;

import com.teamwizardry.librarianlib.features.animator.Animator;
import com.teamwizardry.shotgunsandglitter.ShotgunsAndGlitter;
import com.teamwizardry.shotgunsandglitter.api.IBulletEntity;
import com.teamwizardry.shotgunsandglitter.api.IGrenadeEntity;
import com.teamwizardry.shotgunsandglitter.api.ILingeringEffect;
import com.teamwizardry.shotgunsandglitter.api.capability.SAGWorld;
import com.teamwizardry.shotgunsandglitter.api.capability.SAGWorldCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

public class ClientEventHandler {

	public static final Animator FLASH_ANIMATION_HANDLER = new Animator();
	public static final Animator DISCO_ANIMATION_HANDLER = new Animator();
	public static final Animator HEAD_TILT_ANIMATION_HANDLER = new Animator();

	public static ResourceLocation SPARKLE = new ResourceLocation(ShotgunsAndGlitter.MODID, "particles/sparkle_blurred");
	public static ResourceLocation HEART = new ResourceLocation(ShotgunsAndGlitter.MODID, "particles/heart");
	public static ResourceLocation WEIGHTED_COMPANION_CUBE = new ResourceLocation(ShotgunsAndGlitter.MODID, "particles/weighted_companion_cube");

	public static ResourceLocation getResource(ItemStack stack, ResourceLocation loc) {
		if (stack.getDisplayName().equals("item.gun")) {
			return new ResourceLocation("");
		}
		if (stack.getDisplayName().equals("lemons")) {
			return WEIGHTED_COMPANION_CUBE;
		}

		return loc;
	}

	public static ResourceLocation getResource(@NotNull World world, int casterID, ResourceLocation loc) {
		ItemStack stack = ItemStack.EMPTY;
		Entity entity = world.getEntityByID(casterID);
		if (entity instanceof EntityLivingBase) {
			stack = ((EntityLivingBase) entity).getHeldItemMainhand();
		}

		return getResource(stack, loc);
	}


	public static ResourceLocation getResource(@NotNull World world, @NotNull IBulletEntity bullet, ResourceLocation loc) {
		return getResource(world, bullet.getCasterId(), loc);
	}

	public static ResourceLocation getResource(@NotNull World world, @NotNull IGrenadeEntity grenade, ResourceLocation loc) {
		return getResource(world, grenade.getCasterId(), loc);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onTextureStitchEvent(TextureStitchEvent event) {
		event.getMap().registerSprite(HEART);
		event.getMap().registerSprite(SPARKLE);
		event.getMap().registerSprite(WEIGHTED_COMPANION_CUBE);
	}

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;
		if (Minecraft.getMinecraft().world == null) return;

		SAGWorld worldCap = SAGWorldCapability.get(Minecraft.getMinecraft().world);
		if (worldCap == null) return;

		worldCap.getLingeringObjects().forEach(lingerObject -> {
			if (lingerObject.world != Minecraft.getMinecraft().world.provider.getDimension()) return;

			long sub = Minecraft.getMinecraft().world.getTotalWorldTime() - lingerObject.lastTime;
			if (sub < lingerObject.ticks) {
				if (lingerObject.effect instanceof ILingeringEffect)
					((ILingeringEffect) lingerObject.effect).renderLingeringEffect(Minecraft.getMinecraft().world, lingerObject);
			}
		});
	}
}
