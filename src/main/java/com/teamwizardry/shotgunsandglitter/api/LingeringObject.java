package com.teamwizardry.shotgunsandglitter.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class LingeringObject implements INBTSerializable<NBTTagCompound> {

	public World world;
	public Vec3d pos;
	public int ticks;
	public GrenadeEffect effect;
	public long lastTime;

	public LingeringObject(World world, Vec3d pos, int ticks, GrenadeEffect effect) {
		this.world = world;
		this.pos = pos;
		this.ticks = ticks;
		this.effect = effect;
		this.lastTime = world.getTotalWorldTime();
	}

	private LingeringObject() {
	}

	public static LingeringObject deserialize(NBTTagCompound compound) {
		LingeringObject object = new LingeringObject();
		object.deserializeNBT(compound);
		return object;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		if (world != null)
			compound.setInteger("world", world.provider.getDimension());
		if (pos != null) {
			compound.setDouble("pos_x", pos.x);
			compound.setDouble("pos_y", pos.y);
			compound.setDouble("pos_z", pos.z);
		}
		if (effect != null)
			compound.setString("effect", effect.getID());

		compound.setInteger("ticks", ticks);
		compound.setLong("last_time", lastTime);
		return compound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("world"))
			world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(nbt.getInteger("world"));

		if (nbt.hasKey("pos_x") && nbt.hasKey("pos_y") && nbt.hasKey("pos_z")) {
			pos = new Vec3d(nbt.getDouble("pos_x"), nbt.getDouble("pos_y"), nbt.getDouble("pos_z"));
		}

		if (nbt.hasKey("effect")) {
			effect = EffectRegistry.getGrenadeEffectByID(nbt.getString("effect"));
		}

		if (nbt.hasKey("ticks"))
			ticks = nbt.getInteger("ticks");

		if (nbt.hasKey("last_time"))
			lastTime = nbt.getLong("last_time");
	}
}
