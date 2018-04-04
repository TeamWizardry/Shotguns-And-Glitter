package com.teamwizardry.shotgunsandglitter.api;

import net.minecraft.util.math.MathHelper;

public enum BulletType {
	SNIPER(8f, 0.3f, "sniper"),
	SHOTGUN(3.5f, 0.6f, "shotgun"),
	BASIC(5f, 0.15f, "basic");

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
