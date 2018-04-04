package com.teamwizardry.shotgunsandglitter.api;

import com.teamwizardry.librarianlib.features.config.ConfigProperty;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class SoundSystem {

	@ConfigProperty(category = "sound")
	public static float masterVolume = 1f;

	@ConfigProperty(category = "sound")
	public static String soundCategory = "ambient";

	public static void playSounds(@Nonnull World world, double x, double y, double z, float volume, SoundEvent... sounds) {
		if (!world.isRemote) return;
		if (sounds == null) return;

		SoundCategory category = SoundCategory.getByName(soundCategory);

		for (SoundEvent sound : sounds)
			world.playSound(x, y, z, sound, category, volume * masterVolume, RandUtil.nextFloat(0.9f, 1.1f), false);
	}

	public static void playSounds(@Nonnull World world, @Nonnull Vec3d pos, float volume, SoundEvent... sounds) {
		playSounds(world, pos.x, pos.y, pos.z, volume, sounds);
	}

	public static void playSoundsLoud(@Nonnull World world, @Nonnull Vec3d pos, SoundEvent... sounds) {
		playSounds(world, pos.x, pos.y, pos.z, 2f, sounds);
	}

	public static void playSoundsLoud(@Nonnull World world, double x, double y, double z, SoundEvent... sounds) {
		playSounds(world, x, y, z, 2f, sounds);
	}

	public static void playSoundsQuiet(@Nonnull World world, @Nonnull Vec3d pos, SoundEvent... sounds) {
		playSounds(world, pos.x, pos.y, pos.z, 0.5f, sounds);
	}

	public static void playSoundsQuiet(@Nonnull World world, double x, double y, double z, SoundEvent... sounds) {
		playSounds(world, x, y, z, 0.5f, sounds);
	}
}