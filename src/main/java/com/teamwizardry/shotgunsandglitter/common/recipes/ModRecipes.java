package com.teamwizardry.shotgunsandglitter.common.recipes;

import com.teamwizardry.librarianlib.core.common.RecipeGeneratorHandler;
import com.teamwizardry.librarianlib.core.common.RegistrationHandler;
import com.teamwizardry.shotgunsandglitter.ShotgunsAndGlitter;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.common.items.ItemBullet;
import com.teamwizardry.shotgunsandglitter.common.items.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WireSegal
 * Created at 9:22 AM on 4/1/18.
 */
public class ModRecipes {

	private static final Map<String, Object> INGREDIENTS = new HashMap<>();

	static {
		INGREDIENTS.put("balefire", "netherStar");
		INGREDIENTS.put("biotic", new ItemStack(Items.MELON));
		INGREDIENTS.put("draconic", new ItemStack(Items.DRAGON_BREATH));
		INGREDIENTS.put("firework", "paper");
		INGREDIENTS.put("flash", "dustGlowstone");
		INGREDIENTS.put("frost", new ItemStack(Blocks.ICE));
		INGREDIENTS.put("gravity_in", new ItemStack(Items.SHULKER_SHELL));
		INGREDIENTS.put("gravity_out", "gunpowder");
		INGREDIENTS.put("hookshot", new ItemStack(Items.LEAD));
		INGREDIENTS.put("impact", "obsidian");
		INGREDIENTS.put("piercing", new ItemStack(Items.ARROW));
		INGREDIENTS.put("psychic", "enderpearl");
		INGREDIENTS.put("tainted", new ItemStack(Items.SPIDER_EYE));
		INGREDIENTS.put("tranq", "dustRedstone");
	}

	public static void init() {
		RegistrationHandler.register(new RecipeAmmoHolder(ModItems.MAGAZINE), new ResourceLocation(ShotgunsAndGlitter.MODID, "magazine"));
		RegistrationHandler.register(new RecipeAmmoHolder(ModItems.DRUM), new ResourceLocation(ShotgunsAndGlitter.MODID, "drum"));

		RecipeGeneratorHandler.addShapedRecipe("basic_light",
				ItemBullet.getStackOfEffect(BulletType.BASIC, "basic", 4),
				"IIG",
				'I', "ingotIron",
				'G', "gunpowder");
		RecipeGeneratorHandler.addShapedRecipe("basic_medium",
				ItemBullet.getStackOfEffect(BulletType.SHOTGUN, "basic", 6),
				"IG ",
				"IIG",
				'I', "ingotIron",
				'G', "gunpowder");
		RecipeGeneratorHandler.addShapedRecipe("basic_heavy",
				ItemBullet.getStackOfEffect(BulletType.SNIPER, "basic", 8),
				"IG ",
				"IIG",
				"IG ",
				'I', "ingotIron",
				'G', "gunpowder");

		for (Map.Entry<String, Object> ingredient : INGREDIENTS.entrySet())
			for (BulletType type : BulletType.values()) {
				RecipeGeneratorHandler.addShapelessRecipe(ingredient.getKey() + "_" + type.serializeName, "sng:" + ingredient.getKey(),
						ItemBullet.getStackOfEffect(type, ingredient.getKey()),
						ItemBullet.getStackOfEffect(type, "basic"),
						ingredient.getValue());
				RecipeGeneratorHandler.addShapedRecipe(ingredient.getKey() + "_" + type.serializeName + "_8x", "sng:" + ingredient.getKey(),
						ItemBullet.getStackOfEffect(type, ingredient.getKey(), 8),
						"BBB",
						"BIB",
						"BBB",
						'B', ItemBullet.getStackOfEffect(type, "basic"),
						'I', ingredient.getValue());
			}
	}
}
