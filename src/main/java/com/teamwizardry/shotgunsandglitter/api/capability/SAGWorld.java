package com.teamwizardry.shotgunsandglitter.api.capability;

import com.teamwizardry.shotgunsandglitter.api.LingeringObject;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import java.util.Set;

public interface SAGWorld extends ICapabilitySerializable<NBTTagCompound> {

	void addLingeringObject(LingeringObject object);

	Set<LingeringObject> getLingeringObjects();
}
