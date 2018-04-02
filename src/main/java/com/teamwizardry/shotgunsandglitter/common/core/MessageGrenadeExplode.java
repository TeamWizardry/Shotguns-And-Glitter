package com.teamwizardry.shotgunsandglitter.common.core;

import com.teamwizardry.librarianlib.core.LibrarianLib;
import com.teamwizardry.librarianlib.features.autoregister.PacketRegister;
import com.teamwizardry.librarianlib.features.network.PacketBase;
import com.teamwizardry.librarianlib.features.saving.Save;
import com.teamwizardry.shotgunsandglitter.ShotgunsAndGlitter;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityGrenade;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;

/**
 * @author WireSegal
 * Created at 2:05 PM on 3/31/18.
 */
@PacketRegister(Side.CLIENT)
public class MessageGrenadeExplode extends PacketBase {

	@Save
	public int eID;
	@Save
	public Vec3d grenadePosition;

	public MessageGrenadeExplode(int eID, Vec3d bulletPosition) {
		this.eID = eID;
		this.grenadePosition = bulletPosition;
	}

	public MessageGrenadeExplode() {
		this(0, Vec3d.ZERO);
	}

	@Override
	public void handle(@NotNull MessageContext ctx) {
		EntityPlayer player = LibrarianLib.PROXY.getClientPlayer();
		World world = player.world;
		Entity grenade = world.getEntityByID(eID);
		if (grenade instanceof EntityGrenade) {
			grenade.setPosition(grenadePosition.x, grenadePosition.y, grenadePosition.z);
			ShotgunsAndGlitter.PROXY.grenadeImpact(world, (EntityGrenade) grenade,
					((EntityGrenade) grenade).getEffect(), grenadePosition);
			grenade.setDead();
		}
	}
}
