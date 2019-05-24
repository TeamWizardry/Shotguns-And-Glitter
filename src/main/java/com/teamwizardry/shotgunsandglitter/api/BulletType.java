package com.teamwizardry.shotgunsandglitter.api;

import com.teamwizardry.shotgunsandglitter.common.config.ModConfig;
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



	public double getDamage() {
		switch (this) {
			case SNIPER:
				return ModConfig.sniperDamage;
			case SHOTGUN:
				return ModConfig.shotgunDamage;
			default:
				return ModConfig.basicDamage;
		}
	}

	public double getKnockbackStrength() {
		switch (this) {
			case SNIPER:
				return ModConfig.sniperKnockback;
			case SHOTGUN:
				return ModConfig.shotgunKnockback;
			default:
				return ModConfig.basicKnockback;
		}
	}

	public double getExplosiveRadius() {
		switch (this) {
			case SNIPER:
				return ModConfig.sniperExplosiveRadius;
			case SHOTGUN:
				return ModConfig.shotgunExplosiveRadius;
			default:
				return ModConfig.basicExplosiveRadius;
		}
	}
}
