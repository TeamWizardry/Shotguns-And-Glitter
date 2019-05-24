package com.teamwizardry.shotgunsandglitter.common.config;

import com.teamwizardry.librarianlib.features.config.ConfigDoubleRange;
import com.teamwizardry.librarianlib.features.config.ConfigProperty;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;

public class ModConfig {


    /*
    Sound
     */

    @ConfigProperty(category = "sound")
    public static float masterVolume = 1f;

    @ConfigProperty(category = "sound")
    public static String soundCategory = "ambient";

    /*
    Sniper
     */

    @ConfigDoubleRange(min = 1, max = 50)
    @ConfigProperty(category = "sniper", comment = "The damage a sniper round will do.")
    public static double sniperDamage = 20;

    @ConfigDoubleRange(min = 0.0, max = 2.0)
    @ConfigProperty(category = "sniper", comment = "The knockback an entity will take from a sniper round.")
    public static double sniperKnockback = 0.3;

    @ConfigDoubleRange(min = 1.0, max = 10.0)
    @ConfigProperty(category = "sniper", comment = "The explosive radius of a sniper round.")
    public static double sniperExplosiveRadius = 2;

    /*
    Pistol
     */

    @ConfigDoubleRange(min = 1.0, max = 10.0)
    @ConfigProperty(category = "regular", comment = "The explosive radius of a regular round.")
    public static double basicExplosiveRadius = 3;

    @ConfigDoubleRange(min = 0.0, max = 2.0)
    @ConfigProperty(category = "regular", comment = "The knockback an entity will take from a regular round.")
    public static double basicKnockback = 0.15;

    @ConfigDoubleRange(min = 1, max = 50)
    @ConfigProperty(category = "regular", comment = "The damage a regular round will do.")
    public static double basicDamage = 5;

    /*
    Shotgun
     */

    @ConfigProperty(category = "shotgun", comment = "How many bullets a shotgun will fire per charge.")
    public static int shotgunFireCount = 5;

    @ConfigDoubleRange(min = 1, max = 50)
    @ConfigProperty(category = "shotgun", comment = "The damage a shotgun round will do.")
    public static double shotgunDamage = 10;

    @ConfigDoubleRange(min = 0.0, max = 2.0)
    @ConfigProperty(category = "shotgun", comment = "The knockback an entity will take from a shotgun round.")
    public static double shotgunKnockback = 0.6;

    @ConfigDoubleRange(min = 1.0, max = 10.0)
    @ConfigProperty(category = "shotgun", comment = "The explosive radius of a shotgun round.")
    public static double shotgunExplosiveRadius = 4;

    /*
    Bullet & Grenade Effects
     */

    @ConfigProperty(category = "effects", comment = "Bullet effects added here will be blacklisted and will not show up in game" + "\n" +
            "Effect names: balefire; biotic; draconic; firework; flash; frost; gravity_in; gravity_out; hookshot; impact; piercing; psychic; tainted; tranq")
    public static String[] bulletEffectBlacklist = {};

    @ConfigProperty(category = "effects", comment = "Grenade effects added here will be blacklisted and will not show up in game" + "\n" +
            "Effect names: blizzard; daylight; disco; repulsion")
    public static String[] grenadeEffectBlacklist = {};


    /*
    Turret
     */

    @ConfigProperty(category = "turret", comment = "Add the mobs below which are exempt from being killed by the turret" + "\n" +
            "Naming format: cow; zombie; modid:entityname.")
    public static String[] blacklistedEntityName = {};

    @ConfigDoubleRange(min = 1.0, max = 64.0)
    @ConfigProperty(category = "turret", comment = "Enter the max radius a turret can cover")
    public static double turretRadius = 64;

    @ConfigProperty(category = "turret", comment = "Disables the ability for the turret to target passive mobs")
    public static boolean turretTargetPassives = true;


    public static HashSet<Class<? extends Entity>> blacklistedEntities = new HashSet<>();

    public static void init() {
        if (blacklistedEntityName.length == 0) return;
        for (String entityName : blacklistedEntityName) {
            Class<? extends Entity> c = EntityList.getClass(new ResourceLocation(entityName));
            if (c == null)
                throw new RuntimeException("Invalid entity name: " + entityName + " at shotgunsandglitter.cfg.");

            blacklistedEntities.add(c);
        }
    }


    public static boolean isEntityBlacklisted(Entity entityIn) {
        if (blacklistedEntities.isEmpty()) return false;
        for (Class<? extends Entity> cl : blacklistedEntities) {
            if (cl.isInstance(entityIn))
                return true;
        }
        return false;
    }

    public static boolean isBulletEffectBlacklisted(String id) {
        if (bulletEffectBlacklist.length == 0) return false;
        for (String bEffect : bulletEffectBlacklist)
            if (bEffect.equals(id))
                return true;

        return false;
    }

    public static boolean isGrenadeEffectBlacklisted(String id) {
        if (grenadeEffectBlacklist.length == 0) return false;
        for (String gEffect : grenadeEffectBlacklist)
            if (gEffect.equals(id))
                return true;
        return false;
    }


}
