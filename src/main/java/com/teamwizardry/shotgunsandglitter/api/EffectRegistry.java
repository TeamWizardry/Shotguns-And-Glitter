package com.teamwizardry.shotgunsandglitter.api;

import com.teamwizardry.shotgunsandglitter.common.effects.EffectFirework;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;

public class EffectRegistry {

	private final static HashMap<String, Effect> effects = new HashMap<>();

	private final static Effect BASIC_EFFECT = new Effect() {
		@Override
		public String getID() {
			return "basic";
		}

		@Override
		public boolean onCollideEntity(@NotNull World world, @NotNull EntityBullet bullet, @NotNull Entity entity, @NotNull RayTraceResult hit) {
			entity.attackEntityFrom(DamageSource.causeThrownDamage(bullet, entity), 5f);
			return false;
		}

		@Override
		public boolean onCollideBlock(@NotNull World world, @NotNull EntityBullet bullet, @NotNull RayTraceResult pos, @NotNull IBlockState state) {
			return false;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void renderUpdate(@NotNull World world, @NotNull EntityBullet bullet) {
			// NO-OP
		}
	};

	static {
		addEffect(BASIC_EFFECT);
		addEffect(new EffectFirework());
	}

	public static void addEffect(Effect effect) {
		effects.put(effect.getID(), effect);
	}

	public static Iterable<Effect> getEffects() {
		return effects.values();
	}

	@Nullable
	public static Effect getEffectByID(String id) {
		return effects.getOrDefault(id, BASIC_EFFECT);
	}
}
