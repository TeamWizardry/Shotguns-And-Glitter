package com.teamwizardry.shotgunsandglitter.common.core;

import com.teamwizardry.librarianlib.core.LibrarianLib;
import com.teamwizardry.librarianlib.features.autoregister.PacketRegister;
import com.teamwizardry.librarianlib.features.network.PacketBase;
import com.teamwizardry.librarianlib.features.saving.Save;
import com.teamwizardry.shotgunsandglitter.ShotgunsAndGlitter;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
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
public class MessageBulletImpactEntity extends PacketBase {

	@Save
	public int hitID;
	@Save
	public int eID;
	@Save
	public Vec3d bulletPosition;

	public MessageBulletImpactEntity(int hitID, int eID, Vec3d bulletPosition) {
		this.hitID = hitID;
		this.eID = eID;
		this.bulletPosition = bulletPosition;
	}

	public MessageBulletImpactEntity() {
		this(0, 0, Vec3d.ZERO);
	}

	@Override
	public void handle(@NotNull MessageContext ctx) {
		EntityPlayer player = LibrarianLib.PROXY.getClientPlayer();
		World world = player.world;
		Entity bullet = world.getEntityByID(eID);
		Entity hit = world.getEntityByID(hitID);
		if (bullet instanceof EntityBullet) {
			bullet.setPosition(bulletPosition.x, bulletPosition.y, bulletPosition.z);
			if (ShotgunsAndGlitter.PROXY.collideBulletWithEntity(world, (EntityBullet) bullet, hit,
					((EntityBullet) bullet).getEffect(), bulletPosition))
				bullet.setDead();
		}
	}
}
