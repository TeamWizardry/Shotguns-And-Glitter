package com.teamwizardry.shotgunsandglitter.api;


import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Supplier;

public class EffectRegistry {

	private final static HashMap<String, Effect> effects = new HashMap<>();

	// Effects are SAM types, technically.
	private final static Effect BASIC_EFFECT = () -> "basic";


	static {
		addEffect(BASIC_EFFECT);
	}

	public static void addEffect(Effect... effects) {
		for (Effect effect : effects)
			addEffect(effect);
	}

	public static void addEffect(Supplier<Effect> effectSupplier) {
		Effect effect = effectSupplier.get();
		effects.put(effect.getID(), effect);
	}

	public static Collection<Effect> getEffects() {
		return effects.values();
	}

	@NotNull
	public static Effect getEffectByID(String id) {
		return effects.getOrDefault(id, BASIC_EFFECT);
	}
}
