package com.teamwizardry.shotgunsandglitter.common.core;

import com.teamwizardry.librarianlib.features.config.ConfigProperty;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;

public class MiniTurretHelper {
    @ConfigProperty(category = "turret", comment = "Add the mobs below which are exempt from being killed by the turret")
    public static String[] blacklistedEntityName = {"cow", "sheep", "pig", "chicken", "rabbit"};

    public static HashSet<Class<? extends Entity>> blacklistedEntities = new HashSet<>();

    public static void init(){
        for (String entityName : blacklistedEntityName) {
            Class<? extends Entity> c = EntityList.getClass(new ResourceLocation(entityName));
            if (c == null)
                throw new RuntimeException("Invalid entity name: " + entityName + " at shotgunsandglitter.cfg.");

            blacklistedEntities.add(c);
        }
    }

    public static boolean isEntityBlacklisted(Entity entityIn){
        if(blacklistedEntities.isEmpty()) return false;
        for (Class<? extends Entity> cl : blacklistedEntities) {
            if (cl.isInstance(entityIn))
                return true;
        }
        return false;
    }
}
