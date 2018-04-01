package com.teamwizardry.shotgunsandglitter.api;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EffectRegistry {

	private final static HashMap<String, Effect> effects = new HashMap<>();

	private final static Effect BASIC_EFFECT = new EffectBasic();

	private final static List<Effect> effectsOrdered = new ArrayList<>();

	static {
		addEffect(BASIC_EFFECT);
	}

	public static void addEffect(Effect... effects) {
		for (Effect effect : effects)
			addEffect(effect);
	}

	public static void addEffect(Effect effect) {
		assert !effects.containsKey(effect.getID());
		effects.put(effect.getID(), effect);
		effectsOrdered.add(effect);
	}

	public static List<Effect> getEffects() {
		return effectsOrdered;
	}

	@NotNull
	public static Effect getEffectByID(String id) {
		return effects.getOrDefault(id, BASIC_EFFECT);
	}
}
