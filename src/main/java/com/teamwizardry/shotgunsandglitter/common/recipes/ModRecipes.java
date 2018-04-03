package com.teamwizardry.shotgunsandglitter.common.recipes;

import com.teamwizardry.librarianlib.core.common.RecipeGeneratorHandler;
import com.teamwizardry.librarianlib.core.common.RegistrationHandler;
import com.teamwizardry.shotgunsandglitter.ShotgunsAndGlitter;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.common.blocks.ModBlocks;
import com.teamwizardry.shotgunsandglitter.common.items.ItemBullet;
import com.teamwizardry.shotgunsandglitter.common.items.ItemGrenade;
import com.teamwizardry.shotgunsandglitter.common.items.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author WireSegal
 * Created at 9:22 AM on 4/1/18.
 */
public class ModRecipes {

	private static final Map<String, Object> BULLET_INGREDIENTS = new HashMap<>();
	private static final Map<String, Object> GRENADE_INGREDIENTS = new HashMap<>();


	static {
		BULLET_INGREDIENTS.put("balefire", "netherStar");
		BULLET_INGREDIENTS.put("biotic", new ItemStack(Items.MELON));
		BULLET_INGREDIENTS.put("draconic", new ItemStack(Items.DRAGON_BREATH));
		BULLET_INGREDIENTS.put("firework", "paper");
		BULLET_INGREDIENTS.put("flash", "dustGlowstone");
		BULLET_INGREDIENTS.put("frost", new ItemStack(Blocks.ICE));
		BULLET_INGREDIENTS.put("gravity_in", new ItemStack(Items.SHULKER_SHELL));
		BULLET_INGREDIENTS.put("gravity_out", "gunpowder");
		BULLET_INGREDIENTS.put("hookshot", new ItemStack(Items.LEAD));
		BULLET_INGREDIENTS.put("impact", "obsidian");
		BULLET_INGREDIENTS.put("piercing", new ItemStack(Items.ARROW));
		BULLET_INGREDIENTS.put("psychic", "enderpearl");
		BULLET_INGREDIENTS.put("tainted", new ItemStack(Items.SPIDER_EYE));
		BULLET_INGREDIENTS.put("tranq", "dustRedstone");

		GRENADE_INGREDIENTS.put("disco", "dye");
		GRENADE_INGREDIENTS.put("blizzard", new ItemStack(Items.SNOWBALL));
		GRENADE_INGREDIENTS.put("daylight", new ItemStack(Items.BLAZE_POWDER));
		GRENADE_INGREDIENTS.put("repulsion", "gunpowder");
	}

	public static void init() {
		RegistrationHandler.register(new RecipeAmmoHolder(ModItems.MAGAZINE), new ResourceLocation(ShotgunsAndGlitter.MODID, "magazine"));
		RegistrationHandler.register(new RecipeAmmoHolder(ModItems.DRUM), new ResourceLocation(ShotgunsAndGlitter.MODID, "drum"));

		RecipeGeneratorHandler.addShapedRecipe("pistol",
				new ItemStack(ModItems.PISTOL),
				"RL ",
				"RII",
				"  I",
				'L', "gemLapis",
				'R', "dustRedstone",
				'I', "ingotIron");

		RecipeGeneratorHandler.addShapedRecipe("shotgun",
				new ItemStack(ModItems.SHOTGUN),
				"RRL",
				"RLI",
				" II",
				'L', "gemLapis",
				'R', "dustRedstone",
				'I', "ingotIron");

		RecipeGeneratorHandler.addShapedRecipe("sniper",
				new ItemStack(ModItems.SNIPER),
				"RBL",
				" II",
				'L', "gemLapis",
				'R', "dustRedstone",
				'I', "ingotIron",
				'B', "blockIron");

		RecipeGeneratorHandler.addShapedRecipe("minigun",
				new ItemStack(ModItems.MINIGUN),
				" LI",
				"RBB",
				'L', "gemLapis",
				'R', "dustRedstone",
				'I', "ingotIron",
				'B', "blockIron");

		RecipeGeneratorHandler.addShapedRecipe("turret",
				new ItemStack(Objects.requireNonNull(ModBlocks.MINI_TURRET.getItemForm())),
				"RRR",
				"LIL",
				"BBB",
				'L', "gemLapis",
				'R', "dustRedstone",
				'I', "ingotIron",
				'B', "blockIron");

		RecipeGeneratorHandler.addShapedRecipe("basic_bullet",
				ItemBullet.getStackOfEffect(BulletType.BASIC, "basic", 4),
				"IIG",
				'I', "ingotIron",
				'G', "gunpowder");

		RecipeGeneratorHandler.addShapedRecipe("shotgun_bullet",
				ItemBullet.getStackOfEffect(BulletType.SHOTGUN, "basic", 6),
				"IG ",
				"IIG",
				'I', "ingotIron",
				'G', "gunpowder");

		RecipeGeneratorHandler.addShapedRecipe("sniper_bullet",
				ItemBullet.getStackOfEffect(BulletType.SNIPER, "basic", 8),
				"IG ",
				"IIG",
				"IG ",
				'I', "ingotIron",
				'G', "gunpowder");

		RecipeGeneratorHandler.addShapedRecipe("grenade",
				ItemGrenade.getStackOfEffect("basic", 4),
				"NSN",
				"IGI",
				"NIN",
				'I', "ingotIron",
				'G', "gunpowder",
				'N', "nuggetIron",
				'S', "string");

		for (Map.Entry<String, Object> ingredient : BULLET_INGREDIENTS.entrySet())
			for (BulletType type : BulletType.values()) {
				RecipeGeneratorHandler.addShapelessRecipe(ingredient.getKey() + "_bullet_" + type.serializeName, "sng:bullet_" + ingredient.getKey(),
						ItemBullet.getStackOfEffect(type, ingredient.getKey()),
						ItemBullet.getStackOfEffect(type, "basic"),
						ingredient.getValue());
				RecipeGeneratorHandler.addShapedRecipe(ingredient.getKey() + "_bullet_" + type.serializeName + "_8x", "sng:bullet_" + ingredient.getKey(),
						ItemBullet.getStackOfEffect(type, ingredient.getKey(), 8),
						"BBB",
						"BIB",
						"BBB",
						'B', ItemBullet.getStackOfEffect(type, "basic"),
						'I', ingredient.getValue());
			}

		for (Map.Entry<String, Object> ingredient : GRENADE_INGREDIENTS.entrySet()) {
			RecipeGeneratorHandler.addShapelessRecipe(ingredient.getKey() + "_grenade", "sng:grenade_" + ingredient.getKey(),
					ItemGrenade.getStackOfEffect(ingredient.getKey()),
					ItemGrenade.getStackOfEffect("basic"),
					ingredient.getValue());
			RecipeGeneratorHandler.addShapedRecipe(ingredient.getKey() + "_grenade_8x", "sng:grenade_" + ingredient.getKey(),
					ItemGrenade.getStackOfEffect(ingredient.getKey(), 8),
					"BBB",
					"BIB",
					"BBB",
					'B', ItemGrenade.getStackOfEffect("basic"),
					'I', ingredient.getValue());
		}
	}
}
