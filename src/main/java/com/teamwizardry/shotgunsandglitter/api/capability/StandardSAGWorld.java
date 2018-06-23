package com.teamwizardry.shotgunsandglitter.api.capability;

import com.teamwizardry.shotgunsandglitter.api.LingeringObject;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class StandardSAGWorld implements SAGWorld {

	private Set<LingeringObject> lingeringObjects = new HashSet<>();

	public static StandardSAGWorld create() {
		return new StandardSAGWorld();
	}

	@Override
	public void addLingeringObject(LingeringObject object) {
		lingeringObjects.add(object);
	}

	@Override
	public Set<LingeringObject> getLingeringObjects() {
		return lingeringObjects;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound compound = new NBTTagCompound();

		NBTTagList lingeringNBT = new NBTTagList();
		for (LingeringObject object : lingeringObjects) {
			if (object == null) continue;
			lingeringNBT.appendTag(object.serializeNBT());
		}

		compound.setTag("lingering", lingeringNBT);
		return compound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound compound) {
		if (compound.hasKey("lingering")) {
			lingeringObjects = new HashSet<>();
			NBTTagList lingeringNBT = compound.getTagList("lingering", Constants.NBT.TAG_COMPOUND);
			for (NBTBase base : lingeringNBT) {
				if (base instanceof NBTTagCompound) {
					lingeringObjects.add(LingeringObject.deserialize((NBTTagCompound) base));
				}
			}
		}
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == SAGWorldCapability.capability();
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == SAGWorldCapability.capability() ? SAGWorldCapability.capability().cast(this) : null;
	}
}
