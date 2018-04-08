package com.teamwizardry.shotgunsandglitter.common.network;

import com.teamwizardry.librarianlib.core.LibrarianLib;
import com.teamwizardry.librarianlib.features.animator.Easing;
import com.teamwizardry.librarianlib.features.animator.animations.BasicAnimation;
import com.teamwizardry.librarianlib.features.autoregister.PacketRegister;
import com.teamwizardry.librarianlib.features.network.PacketBase;
import com.teamwizardry.librarianlib.features.saving.Save;
import com.teamwizardry.shotgunsandglitter.api.InternalHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;

@PacketRegister(Side.CLIENT)
public class PacketAnimateHeadTilt extends PacketBase {

	@Save
	private int amount = 0;

	public PacketAnimateHeadTilt() {
		// NO-OP
	}

	public PacketAnimateHeadTilt(int tilt) {
		this.amount = tilt;
	}

	@Override
	public void handle(@NotNull MessageContext ctx) {
		EntityPlayer clientPlayer = LibrarianLib.PROXY.getClientPlayer();
		BasicAnimation<EntityPlayer> anim = new BasicAnimation<>(clientPlayer, "rotationPitch");
		anim.setDuration(2);
		anim.setTo(Minecraft.getMinecraft().player.rotationPitch - amount);
		anim.setEasing(Easing.easeInExpo);
		InternalHandler.INTERNAL_HANDLER.addTiltAnimation(anim);
	}
}
