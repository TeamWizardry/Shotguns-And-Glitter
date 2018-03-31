package com.teamwizardry.shotgunsandglitter;

import com.teamwizardry.shotgunsandglitter.api.EffectRegistry;
import com.teamwizardry.shotgunsandglitter.common.effects.*;

/**
 * @author WireSegal
 * Created at 11:51 PM on 3/30/18.
 */
public class ModEffects {
	public static void init() {
		EffectRegistry.addEffect(new EffectFirework(),
				new EffectGraviton(true),
				new EffectGraviton(false),
				new EffectHookshot(),
				new EffectPiercing(),
				new EffectPsychic());
	}
}
