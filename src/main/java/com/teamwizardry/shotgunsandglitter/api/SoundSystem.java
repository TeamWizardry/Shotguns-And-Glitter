package com.teamwizardry.shotgunsandglitter.api;

import com.teamwizardry.librarianlib.features.network.PacketHandler;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.common.config.ModConfig;
import com.teamwizardry.shotgunsandglitter.common.network.PacketPlaySound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nonnull;

public class SoundSystem {


	public static void playSounds(@Nonnull World world, double x, double y, double z, float volume, SoundEvent... sounds) {
		if (world.isRemote) return;
		if (sounds == null) return;

        SoundCategory category = SoundCategory.getByName(ModConfig.soundCategory);

		for (SoundEvent sound : sounds) {
			if (sound != null) {
                PacketHandler.NETWORK.sendToAllAround(new PacketPlaySound(sound, category, x, y, z, 256, volume * ModConfig.masterVolume, RandUtil.nextFloat(0.9f, 1.1f)), new NetworkRegistry.TargetPoint(world.provider.getDimension(), x, y, z, 256));
			}
		}
	}

	public static void playSounds(@Nonnull World world, @Nonnull Vec3d pos, float volume, SoundEvent... sounds) {
		playSounds(world, pos.x, pos.y, pos.z, volume, sounds);
	}

	public static void playSounds(@Nonnull World world, @Nonnull Vec3d pos, SoundEvent... sounds) {
		playSounds(world, pos.x, pos.y, pos.z, 2f, sounds);
	}

	public static void playSounds(@Nonnull World world, double x, double y, double z, SoundEvent... sounds) {
		playSounds(world, x, y, z, 2f, sounds);
	}

	public static void playSoundsQuiet(@Nonnull World world, @Nonnull Vec3d pos, SoundEvent... sounds) {
		playSounds(world, pos.x, pos.y, pos.z, 0.5f, sounds);
	}

	public static void playSoundsQuiet(@Nonnull World world, double x, double y, double z, SoundEvent... sounds) {
		playSounds(world, x, y, z, 0.5f, sounds);
	}
}