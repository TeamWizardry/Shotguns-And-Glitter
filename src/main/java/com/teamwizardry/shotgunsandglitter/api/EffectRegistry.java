package com.teamwizardry.shotgunsandglitter.api;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EffectRegistry {

	private final static HashMap<String, BulletEffect> effects = new HashMap<>();

	private final static BulletEffect BASIC_BULLET_EFFECT = new BulletEffectBasic();

	private final static List<BulletEffect> EFFECTS_ORDERED = new ArrayList<>();

	static {
		addEffect(BASIC_BULLET_EFFECT);
	}

	public static void addEffect(BulletEffect... bulletEffects) {
		for (BulletEffect bulletEffect : bulletEffects)
			addEffect(bulletEffect);
	}

	public static void addEffect(BulletEffect bulletEffect) {
		assert !effects.containsKey(bulletEffect.getID());
		effects.put(bulletEffect.getID(), bulletEffect);
		EFFECTS_ORDERED.add(bulletEffect);
	}

	public static List<BulletEffect> getEffects() {
		return EFFECTS_ORDERED;
	}

	@NotNull
	public static BulletEffect getEffectByID(String id) {
		return effects.getOrDefault(id, BASIC_BULLET_EFFECT);
	}
}
