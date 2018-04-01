package com.teamwizardry.shotgunsandglitter.api;

import net.minecraft.util.math.MathHelper;

public enum BulletType {
	SNIPER(5f, 0.35f, "sniper"),
	SHOTGUN(2f, 3f, "shotgun"),
	PISTOL(1f, 0.15f, "pistol");

	public final float damage;
	public final float knockbackStrength;
	public final String serializeName;

	BulletType(float damage, float knockbackStrength, String serializeName) {
		this.damage = damage;
		this.knockbackStrength = knockbackStrength;
		this.serializeName = serializeName;
	}

	public static BulletType byOrdinal(int ord) {
		return BulletType.values()[MathHelper.clamp(ord, 0, BulletType.values().length - 1)];
	}
}
