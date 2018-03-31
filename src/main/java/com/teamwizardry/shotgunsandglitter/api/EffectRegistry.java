package com.teamwizardry.shotgunsandglitter.api;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

public class EffectRegistry {

	private final static HashMap<String, Effect> effects = new HashMap<>();

	// Effects are SAM types, technically.
	private final static Effect BASIC_EFFECT = () -> "basic";

	private final static ArrayList<Effect> indexedEffects = new ArrayList<>();

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
		indexedEffects.add(effect);
	}

	public static Iterable<Effect> getEffects() {
		return effects.values();
	}

	public static int getEffectIndex(@Nonnull Effect effect) {
		return indexedEffects.indexOf(effect);
	}

	@Nonnull
	public static Effect getEffectByID(String id) {
		return effects.getOrDefault(id, BASIC_EFFECT);
	}
}
