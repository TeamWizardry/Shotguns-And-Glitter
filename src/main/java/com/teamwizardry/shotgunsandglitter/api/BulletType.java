package com.teamwizardry.shotgunsandglitter.api;

public enum BulletType {
	HEAVY(8f), MEDIUM(5f), SMALL(2f);

	public final float damage;

	BulletType(float damage) {
		this.damage = damage;
	}
}
