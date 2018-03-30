package com.teamwizardry.shotgunsandglitter.api;

import com.teamwizardry.shotgunsandglitter.common.effects.EffectFirework;

import javax.annotation.Nullable;
import java.util.HashSet;

public class EffectRegistry {

	private final static HashSet<Effect> effects = new HashSet<>();

	static {
		addEffect(new EffectFirework());
	}

	public static void addEffect(Effect effect) {
		effects.add(effect);
	}

	public static HashSet<Effect> getEffects() {
		return effects;
	}

	@Nullable
	public static Effect getEffectByID(String id) {
		for (Effect effect : effects) {
			if (effect.getID().equals(id)) return effect;
		}
		return null;
	}
}
