package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.shotgunsandglitter.api.EffectRegistry;

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
				new EffectPsychic(),
				new EffectBiotic(),
				new EffectTranq(),
				new EffectFrost(),
				new EffectTainted(),
				new EffectFlash(),
				new EffectImpact(),
				new EffectBalefire(),
				new EffectDraconic());
	}
}
