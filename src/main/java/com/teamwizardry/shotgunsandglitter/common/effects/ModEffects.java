package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.shotgunsandglitter.api.EffectRegistry;

/**
 * @author WireSegal
 * Created at 11:51 PM on 3/30/18.
 */
public class ModEffects {
	public static void init() {
		EffectRegistry.addEffect(new BulletEffectFirework(),
				new BulletEffectGraviton(true),
				new BulletEffectGraviton(false),
				new BulletEffectHookshot(),
				new BulletEffectPiercing(),
				new BulletEffectPsychic(),
				new BulletEffectBiotic(),
				new BulletEffectTranq(),
				new BulletEffectFrost(),
				new BulletEffectTainted(),
				new BulletEffectFlash(),
				new BulletEffectImpact(),
				new BulletEffectBalefire(),
				new BulletEffectDraconic());
	}
}
