package com.teamwizardry.shotgunsandglitter.common.core;

import com.teamwizardry.librarianlib.features.base.ModSoundEvent;

public class ModSounds {

	public static ModSoundEvent SHOT_PISTOL;
	public static ModSoundEvent SHOT_SHOTGUN;
	public static ModSoundEvent SHOT_SNIPER;
	public static ModSoundEvent SHOT_MINIGUN;
	public static ModSoundEvent RELOAD_PISTOL;
	public static ModSoundEvent RELOAD_SHOTGUN;
	public static ModSoundEvent RELOAD_SNIPER;
	public static ModSoundEvent BULLET_IMPACT;
	public static ModSoundEvent BULLET_FLYBY;
	public static ModSoundEvent SHOTGUN_COCK;
	public static ModSoundEvent SHOT_SHOTGUN_COCK;
	public static ModSoundEvent BURN;
	public static ModSoundEvent DUST_SPARKLE;
	public static ModSoundEvent MAGIC_SPARKLE;
	public static ModSoundEvent SHORT_TWINKLE;
	public static ModSoundEvent CHAINY_ZAP;
	public static ModSoundEvent COLD_WIND;
	public static ModSoundEvent POP;
	public static ModSoundEvent ELECTRIC_BLAST;
	public static ModSoundEvent FIRE;
	public static ModSoundEvent FIREBALL;
	public static ModSoundEvent FROST_FORM;
	public static ModSoundEvent LIGHTNING;
	public static ModSoundEvent SMOKE_BLAST;
	public static ModSoundEvent THUNDERBLAST;
	public static ModSoundEvent HEAL;
	public static ModSoundEvent ETHEREAL;

	public static void init() {
		SHOT_PISTOL = new ModSoundEvent("shot_pistol");
		SHOT_SHOTGUN = new ModSoundEvent("shot_shotgun");
		SHOT_SNIPER = new ModSoundEvent("shot_sniper");
		SHOT_MINIGUN = new ModSoundEvent("shot_minigun");
		RELOAD_PISTOL = new ModSoundEvent("reload_pistol");
		RELOAD_SHOTGUN = new ModSoundEvent("reload_shotgun");
		RELOAD_SNIPER = new ModSoundEvent("reload_sniper");
		BULLET_IMPACT = new ModSoundEvent("bullet_impact");
		BULLET_FLYBY = new ModSoundEvent("bullet_flyby");
		SHOTGUN_COCK = new ModSoundEvent("shotgun_cock");
		BURN = new ModSoundEvent("burn");
		DUST_SPARKLE = new ModSoundEvent("dust_sparkle");
		MAGIC_SPARKLE = new ModSoundEvent("magic_sparkle");
		SHORT_TWINKLE = new ModSoundEvent("short_twinkle");
		CHAINY_ZAP = new ModSoundEvent("chainy_zap");
		COLD_WIND = new ModSoundEvent("cold_wind");
		ELECTRIC_BLAST = new ModSoundEvent("electric_blast");
		FIRE = new ModSoundEvent("fire");
		FIREBALL = new ModSoundEvent("fireball");
		FROST_FORM = new ModSoundEvent("frost_form");
		LIGHTNING = new ModSoundEvent("lightning");
		SMOKE_BLAST = new ModSoundEvent("smoke_blast");
		THUNDERBLAST = new ModSoundEvent("thunderblast");
		POP = new ModSoundEvent("pop");
		HEAL = new ModSoundEvent("heal");
		ETHEREAL = new ModSoundEvent("ethereal_pass_by");
		SHOT_SHOTGUN_COCK = new ModSoundEvent("shot_shotgun_cock");
	}
}
