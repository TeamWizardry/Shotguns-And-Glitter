package com.teamwizardry.shotgunsandglitter.client.jei;

import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.common.items.ItemBullet;
import com.teamwizardry.shotgunsandglitter.common.items.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;

/**
 * @author WireSegal
 * Created at 10:51 AM on 4/1/18.
 */
@JEIPlugin
public class ShotgunJEIPlugin implements IModPlugin {
	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
		subtypeRegistry.registerSubtypeInterpreter(ModItems.BULLET,
				(stack) -> BulletType.byOrdinal(stack.getItemDamage()).serializeName +
						"_" + ItemBullet.getEffectFromItem(stack).getID());
	}
}
