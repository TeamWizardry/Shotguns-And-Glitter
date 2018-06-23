package com.teamwizardry.shotgunsandglitter.common.effects;

import com.teamwizardry.librarianlib.features.math.interpolate.StaticInterp;
import com.teamwizardry.librarianlib.features.network.PacketHandler;
import com.teamwizardry.librarianlib.features.particle.ParticleBuilder;
import com.teamwizardry.librarianlib.features.particle.ParticleSpawner;
import com.teamwizardry.librarianlib.features.particle.functions.InterpFadeInOut;
import com.teamwizardry.shotgunsandglitter.api.*;
import com.teamwizardry.shotgunsandglitter.api.capability.SAGWorld;
import com.teamwizardry.shotgunsandglitter.api.capability.SAGWorldCapability;
import com.teamwizardry.shotgunsandglitter.api.util.InterpScale;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.client.core.ClientEventHandler;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import com.teamwizardry.shotgunsandglitter.common.network.PacketSyncSAGWorld;
import com.teamwizardry.shotgunsandglitter.common.potions.ModPotions;
import net.minecraft.block.BlockSnow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

/**
 * @author WireSegal
 * Created at 9:41 AM on 4/2/18.
 */
public class GrenadeEffectBlizzard implements GrenadeEffect, ILingeringEffect {
	@Override
	public String getID() {
		return "blizzard";
	}

	@Override
	public float range(@NotNull World world, @NotNull IGrenadeEntity grenade) {
		return 30;
	}

	@Override
	public float damage(@NotNull World world, @NotNull IGrenadeEntity grenade, float intensity) {
		return intensity * 2f;
	}

	@Override
	public void onImpact(@NotNull World world, @NotNull IGrenadeEntity grenade) {
		GrenadeEffect.super.onImpact(world, grenade);

		if (!world.isRemote) {
			SAGWorld worldCap = SAGWorldCapability.get(world);
			if (worldCap != null) {
				worldCap.addLingeringObject(new LingeringObject(world, grenade.getPositionAsVector(), 50, this));
				PacketHandler.NETWORK.sendToDimension(new PacketSyncSAGWorld(worldCap.serializeNBT()), world.provider.getDimension());
			}
		}
	}

	@Override
	public void runLingeringEffect(@NotNull LingeringObject lingeringObject) {
		for (int i = 0; i < 3; i++) {
			EntityFallingBlock droppingBlock = new EntityFallingBlock(lingeringObject.world, lingeringObject.pos.x, lingeringObject.pos.y, lingeringObject.pos.z, Blocks.SNOW_LAYER.getDefaultState().withProperty(BlockSnow.LAYERS, RandUtil.nextInt(1, 6)));
			droppingBlock.fallTime = 1;
			droppingBlock.motionX = RandUtil.nextDouble(-1, 1);
			droppingBlock.motionY = RandUtil.nextDouble(0.3, 1);
			droppingBlock.motionZ = RandUtil.nextDouble(-1, 1);
			droppingBlock.velocityChanged = true;
			lingeringObject.world.spawnEntity(droppingBlock);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderUpdate(@NotNull World world, @NotNull IGrenadeEntity grenade) {
		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setCollision(true);
		glitter.setCanBounce(true);

		ParticleSpawner.spawn(glitter, world, new StaticInterp<>(grenade.getPositionAsVector()), 1, 0, (i, build) -> {
			build.setLifetime(RandUtil.nextInt(50, 100));
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(0.5f, 2f), 0));
			build.setAlphaFunction(new InterpFadeInOut(0, 1f));

			build.setDeceleration(new Vec3d(0.9, 0.9, 0.9));

			double radius = RandUtil.nextDouble(0.1, 0.3);
			double theta = 2.0f * (float) Math.PI * RandUtil.nextFloat();
			double r = radius * RandUtil.nextFloat();
			double x = r * MathHelper.cos((float) theta);
			double z = r * MathHelper.sin((float) theta);
			build.setMotion(new Vec3d(x, RandUtil.nextDouble(-radius, radius), z));
		});
	}


	@SideOnly(Side.CLIENT)
	@Override
	public void renderLingeringEffect(@NotNull LingeringObject lingeringObject) {
		if (lingeringObject.world.getTotalWorldTime() % 4 == 0)
			SoundSystem.playSounds(lingeringObject.world, lingeringObject.pos, ModSounds.COLD_WIND);

		ParticleBuilder glitter = new ParticleBuilder(10);
		glitter.setRender(ClientEventHandler.SPARKLE);
		glitter.setCollision(true);
		glitter.setCanBounce(true);

		ParticleSpawner.spawn(glitter, lingeringObject.world, new StaticInterp<>(lingeringObject.pos), 10, 0, (i, build) -> {
			build.setLifetime(RandUtil.nextInt(50, 100));
			build.setScaleFunction(new InterpScale(RandUtil.nextFloat(0.5f, 4f), 0));
			build.setAcceleration(new Vec3d(0, RandUtil.nextDouble(-0.05, -0.1), 0));
			build.setAlphaFunction(new InterpFadeInOut(0, 1f));

			build.setAcceleration(new Vec3d(0, RandUtil.nextDouble(-0.01, -0.05), 0));

			double radius = RandUtil.nextDouble(1, 4);
			double theta = 2.0f * (float) Math.PI * RandUtil.nextFloat();
			double r = radius * RandUtil.nextFloat();
			double x = r * MathHelper.cos((float) theta);
			double z = r * MathHelper.sin((float) theta);
			build.setMotion(new Vec3d(x, RandUtil.nextDouble(0, radius), z));
		});
	}

	@Override
	public void hitEntity(@NotNull World world, @NotNull IGrenadeEntity grenade, @NotNull Entity entity, float intensity) {
		GrenadeEffect.super.hitEntity(world, grenade, entity, intensity);

		if (!world.isRemote && entity instanceof EntityLivingBase)
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(ModPotions.FROST,
					(int) (300 * intensity), (int) (3 * intensity)));

	}
}
