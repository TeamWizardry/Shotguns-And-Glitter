package com.teamwizardry.shotgunsandglitter.api;

import com.teamwizardry.shotgunsandglitter.common.effects.EffectFirework;

import javax.annotation.Nullable;
import java.util.HashMap;

public class EffectRegistry {

	private final static HashMap<String, Effect> effects = new HashMap<>();

	static {
		addEffect(new EffectFirework());
	}

	public static void addEffect(Effect effect) {
		effects.put(effect.getID(), effect);
	}

	public static Iterable<Effect> getEffects() {
		return effects.values();
	}

	@Nullable
	public static Effect getEffectByID(String id) {
		return effects.get(id);
	}
}
