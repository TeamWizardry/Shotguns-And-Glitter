package com.teamwizardry.shotgunsandglitter.api;

import net.minecraft.util.math.MathHelper;

public enum BulletType {
	HEAVY(5f, 1f, "heavy"),
	MEDIUM(1f, 0.25f, "medium"),
	SMALL(2f, 0.5f, "light");

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
