package com.teamwizardry.shotgunsandglitter;

import com.teamwizardry.librarianlib.features.base.ModSoundEvent;

public class ModSounds {

	public static ModSoundEvent SHOT_PISTOL;
	public static ModSoundEvent SHOT_SHOTGUN;
	public static ModSoundEvent SHOT_SNIPER;

	public static void init() {
		SHOT_PISTOL = new ModSoundEvent("shot_pistol");
		SHOT_SHOTGUN = new ModSoundEvent("shot_shotgun");
		SHOT_SNIPER = new ModSoundEvent("shot_sniper");
	}
}
