package com.teamwizardry.shotgunsandglitter.api;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EffectRegistry {

	private final static HashMap<String, BulletEffect> BULLET_EFFECTS = new HashMap<>();
	private final static HashMap<String, GrenadeEffect> GRENADE_EFFECTS = new HashMap<>();

	private final static BulletEffect BASIC_BULLET_EFFECT = new BulletEffectBasic();
	private final static GrenadeEffect BASIC_GRENADE_EFFECT = () -> "basic"; // todo visuals

	private final static List<BulletEffect> BULLET_EFFECTS_ORDERED = new ArrayList<>();
	private final static List<GrenadeEffect> GRENADE_EFFECTS_ORDERED = new ArrayList<>();

	static {
		addEffect(BASIC_BULLET_EFFECT);
	}

	public static void addEffect(BulletEffect... bulletEffects) {
		for (BulletEffect bulletEffect : bulletEffects)
			addEffect(bulletEffect);
	}

	public static void addEffect(BulletEffect bulletEffect) {
		assert !BULLET_EFFECTS.containsKey(bulletEffect.getID());
		BULLET_EFFECTS.put(bulletEffect.getID(), bulletEffect);
		BULLET_EFFECTS_ORDERED.add(bulletEffect);
	}

	public static void addEffect(GrenadeEffect... grenadeEffects) {
		for (GrenadeEffect grenadeEffect : grenadeEffects)
			addEffect(grenadeEffect);
	}

	public static void addEffect(GrenadeEffect grenadeEffect) {
		assert !GRENADE_EFFECTS.containsKey(grenadeEffect.getID());
		GRENADE_EFFECTS.put(grenadeEffect.getID(), grenadeEffect);
		GRENADE_EFFECTS_ORDERED.add(grenadeEffect);
	}

	public static List<BulletEffect> getBulletEffects() {
		return BULLET_EFFECTS_ORDERED;
	}

	public static List<GrenadeEffect> getGrenadeEffects() {
		return GRENADE_EFFECTS_ORDERED;
	}

	@NotNull
	public static BulletEffect getBulletEffectByID(String id) {
		return BULLET_EFFECTS.getOrDefault(id, BASIC_BULLET_EFFECT);
	}

	@NotNull
	public static GrenadeEffect getGrenadeEffectByID(String id) {
		return GRENADE_EFFECTS.getOrDefault(id, BASIC_GRENADE_EFFECT);
	}
}
