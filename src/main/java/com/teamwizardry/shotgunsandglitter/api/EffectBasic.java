package com.teamwizardry.shotgunsandglitter.api;

import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner;
import com.teamwizardry.librarianlib.features.particle.functions.InterpFadeInOut;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

public class EffectBasic implements Effect {

	@Override
	public String getID() {
		return "basic";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderUpdate(@NotNull World world, @NotNull IBulletEntity bullet) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(InternalHandler.INTERNAL_HANDLER.getSparkle());
		glitter.setCollision(true);
		glitter.setScale(0.3f);
		glitter.setAlphaFunction(new InterpFadeInOut(0f, 1f));

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(bullet.getPositionVector()), 1);
	}
}
