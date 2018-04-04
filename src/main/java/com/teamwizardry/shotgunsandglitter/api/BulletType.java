package com.teamwizardry.shotgunsandglitter.api;

import com.teamwizardry.librarianlib.features.config.ConfigDoubleRange;
import com.teamwizardry.librarianlib.features.config.ConfigProperty;
import net.minecraft.util.math.MathHelper;

public enum BulletType {
	SNIPER("sniper"),
	SHOTGUN("shotgun"),
	BASIC("basic");

	public final String serializeName;

	BulletType(String serializeName) {
		this.serializeName = serializeName;
	}

	public static BulletType byOrdinal(int ord) {
		return BulletType.values()[MathHelper.clamp(ord, 0, BulletType.values().length - 1)];
	}

	@ConfigDoubleRange(min = 5, max = 20)
	@ConfigProperty(category = "sniper", comment = "The damage a sniper round will do.")
	public static double sniperDamage = 12;
	@ConfigDoubleRange(min = 5, max = 20)
	@ConfigProperty(category = "shotgun", comment = "The damage a shotgun round will do.")
	public static double shotgunDamage = 5;
	@ConfigDoubleRange(min = 5, max = 20)
	@ConfigProperty(category = "regular", comment = "The damage a regular round will do.")
	public static double basicDamage = 6;

	@ConfigDoubleRange(min = 0.0, max = 2.0)
	@ConfigProperty(category = "sniper", comment = "The knockback an entity will take from a sniper round.")
	public static double sniperKnockback = 0.3;
	@ConfigDoubleRange(min = 0.0, max = 2.0)
	@ConfigProperty(category = "shotgun", comment = "The knockback an entity will take from a shotgun round.")
	public static double shotgunKnockback = 0.6;
	@ConfigDoubleRange(min = 0.0, max = 2.0)
	@ConfigProperty(category = "regular", comment = "The knockback an entity will take from a regular round.")
	public static double basicKnockback = 0.15;

	@ConfigDoubleRange(min = 1.0, max = 10.0)
	@ConfigProperty(category = "sniper", comment = "The explosive radius of a sniper round.")
	public static double sniperExplosiveRadius = 2;
	@ConfigDoubleRange(min = 1.0, max = 10.0)
	@ConfigProperty(category = "shotgun", comment = "The explosive radius of a shotgun round.")
	public static double shotgunExplosiveRadius = 4;
	@ConfigDoubleRange(min = 1.0, max = 10.0)
	@ConfigProperty(category = "regular", comment = "The explosive radius of a regular round.")
	public static double basicExplosiveRadius = 3;

	public double getDamage() {
		switch (this) {
			case SNIPER:
				return sniperDamage;
			case SHOTGUN:
				return shotgunDamage;
			default:
				return basicDamage;
		}
	}

	public double getKnockbackStrength() {
		switch (this) {
			case SNIPER:
				return sniperKnockback;
			case SHOTGUN:
				return shotgunKnockback;
			default:
				return basicKnockback;
		}
	}

	public double getExplosiveRadius() {
		switch (this) {
			case SNIPER:
				return sniperExplosiveRadius;
			case SHOTGUN:
				return shotgunExplosiveRadius;
			default:
				return basicExplosiveRadius;
		}
	}
}
