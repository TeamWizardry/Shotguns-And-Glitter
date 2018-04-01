package com.teamwizardry.shotgunsandglitter.api;

import com.teamwizardry.librarianlib.features.animator.Animation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

/**
 * @author WireSegal
 * Created at 8:32 AM on 4/1/18.
 */
public class InternalHandler {
	public static InternalHandler INTERNAL_HANDLER = new InternalHandler();

	public IBulletEntity newBulletEntity(@NotNull World world, @NotNull EntityLivingBase caster, @NotNull BulletType bulletType, @NotNull Effect effect, float inaccuracy) {
		return null;
	}

	@SideOnly(Side.CLIENT)
	public ResourceLocation getSparkle() {
		return null;
	}

	@SideOnly(Side.CLIENT)
	public void addTiltAnimation(Animation<?> animation) {
		// NO-OP
	}
}
