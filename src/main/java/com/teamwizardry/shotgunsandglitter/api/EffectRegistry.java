package com.teamwizardry.shotgunsandglitter.api;

import com.teamwizardry.shotgunsandglitter.common.effects.EffectFirework;

import javax.annotation.Nullable;
import java.util.HashMap;

public class EffectRegistry {

	private final static HashMap<String, Effect> effects = new HashMap<>();

	// Effects are SAM types, technically.
	private final static Effect BASIC_EFFECT = () -> "basic";
	private final static Effect FIREWORK_EFFECT = new EffectFirework();

	static {
		addEffect(BASIC_EFFECT, FIREWORK_EFFECT);
	}

	public static void addEffect(Effect... effects) {
		for (Effect effect : effects)
			addEffect(effect);
	}

	public static void addEffect(Effect effect) {
		effects.put(effect.getID(), effect);
	}

	public static Iterable<Effect> getEffects() {
		return effects.values();
	}

	@Nullable
	public static Effect getEffectByID(String id) {
		return effects.getOrDefault(id, BASIC_EFFECT);
	}
}
