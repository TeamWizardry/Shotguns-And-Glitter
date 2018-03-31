package com.teamwizardry.shotgunsandglitter;

import com.teamwizardry.shotgunsandglitter.common.items.ItemBullet;
import com.teamwizardry.shotgunsandglitter.common.items.ItemPistol;
import com.teamwizardry.shotgunsandglitter.common.items.ItemShotgun;

public class ModItems {

	public static ItemPistol PISTOL;
	public static ItemShotgun SHOTGUN;

	public static ItemBullet BULLET;

	public static void init() {
		PISTOL = new ItemPistol();
		SHOTGUN = new ItemShotgun();

		BULLET = new ItemBullet();
	}
}
