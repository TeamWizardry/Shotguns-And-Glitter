package com.teamwizardry.shotgunsandglitter.common.core;

import com.teamwizardry.librarianlib.features.base.ModSoundEvent;

public class ModSounds {

	public static ModSoundEvent SHOT_PISTOL;
	public static ModSoundEvent SHOT_SHOTGUN;
	public static ModSoundEvent SHOT_SNIPER;
	public static ModSoundEvent BULLET_IMPACT;
	public static ModSoundEvent BULLET_FLYBY;
	public static ModSoundEvent SHOTGUN_COCK;
	public static ModSoundEvent BURN;
	public static ModSoundEvent DUST_SPARKLE;
	public static ModSoundEvent MAGIC_SPARKLE;
	public static ModSoundEvent SHORT_TWINKLE;

	public static void init() {
		SHOT_PISTOL = new ModSoundEvent("shot_pistol");
		SHOT_SHOTGUN = new ModSoundEvent("shot_shotgun");
		SHOT_SNIPER = new ModSoundEvent("shot_sniper");
		BULLET_IMPACT = new ModSoundEvent("bullet_impact");
		BULLET_FLYBY = new ModSoundEvent("bullet_flyby");
		SHOTGUN_COCK = new ModSoundEvent("shotgun_cock");
		BURN = new ModSoundEvent("burn");
		DUST_SPARKLE = new ModSoundEvent("dust_sparkle");
		MAGIC_SPARKLE = new ModSoundEvent("magic_sparkle");
		SHORT_TWINKLE = new ModSoundEvent("short_twinkle");
	}
}
