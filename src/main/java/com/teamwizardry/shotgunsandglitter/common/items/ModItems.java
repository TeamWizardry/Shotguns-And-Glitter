package com.teamwizardry.shotgunsandglitter.common.items;

public class ModItems {

	public static ItemPistol PISTOL;
	public static ItemShotgun SHOTGUN;
	public static ItemSniper SNIPER;
	public static ItemMinigun MINIGUN;

	public static ItemMagazine MAGAZINE;
	public static ItemDrum DRUM;

	public static ItemBullet BULLET;

	public static void init() {
		PISTOL = new ItemPistol();
		SHOTGUN = new ItemShotgun();
		SNIPER = new ItemSniper();
		MINIGUN = new ItemMinigun();

		MAGAZINE = new ItemMagazine();
		DRUM = new ItemDrum();

		BULLET = new ItemBullet();
	}
}
