package com.teamwizardry.shotgunsandglitter.common.items;

public class ModItems {

	public static ItemPistol PISTOL;
	public static ItemShotgun SHOTGUN;
	public static ItemSniper SNIPER;

	public static ItemMagazine MAGAZINE;

	public static ItemBullet BULLET;

	public static void init() {
		PISTOL = new ItemPistol();
		SHOTGUN = new ItemShotgun();
		SNIPER = new ItemSniper();

		MAGAZINE = new ItemMagazine();

		BULLET = new ItemBullet();
	}
}
