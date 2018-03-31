package com.teamwizardry.shotgunsandglitter.api;

public enum BulletType {
	HEAVY(8f, 1f),
	MEDIUM(5f, 0.75f),
	SMALL(2f, 0.1f);

	public final float damage;
	public final float knockbackStrength;

	BulletType(float damage, float knockbackStrength) {
		this.damage = damage;
		this.knockbackStrength = knockbackStrength;
	}
}
