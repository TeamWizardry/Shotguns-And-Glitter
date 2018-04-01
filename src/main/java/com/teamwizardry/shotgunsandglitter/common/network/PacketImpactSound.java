package com.teamwizardry.shotgunsandglitter.common.network;

import com.teamwizardry.librarianlib.core.LibrarianLib;
import com.teamwizardry.librarianlib.features.autoregister.PacketRegister;
import com.teamwizardry.librarianlib.features.network.PacketBase;
import com.teamwizardry.librarianlib.features.saving.Save;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.EffectRegistry;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;

@PacketRegister(Side.CLIENT)
public class PacketImpactSound extends PacketBase {

	@Save
	private Vec3d pos;
	@Save
	private String effectID;

	public PacketImpactSound() {
		// NO-OP
	}

	public PacketImpactSound(Vec3d pos, String effectID) {
		this.pos = pos;
		this.effectID = effectID;
	}

	@Override
	public void handle(@NotNull MessageContext ctx) {
		if (ctx.side.isServer()) return;
		World world = LibrarianLib.PROXY.getClientPlayer().world;
		if (world == null) return;

		Effect effect = EffectRegistry.getEffectByID(effectID);

		world.playSound(pos.x, pos.y, pos.z, ModSounds.BULLET_IMPACT, SoundCategory.HOSTILE, effect.getVolume(), RandUtil.nextFloat(0.95f, 1.1f), false);
		world.playSound(pos.x, pos.y, pos.z, ModSounds.DUST_SPARKLE, SoundCategory.HOSTILE, effect.getVolume(), RandUtil.nextFloat(0.95f, 1.1f), false);
		if (effect.getImpactSound() != null) {
			world.playSound(pos.x, pos.y, pos.z, effect.getImpactSound(), SoundCategory.HOSTILE, effect.getVolume(), RandUtil.nextFloat(0.95f, 1.1f), false);
		}
	}
}
