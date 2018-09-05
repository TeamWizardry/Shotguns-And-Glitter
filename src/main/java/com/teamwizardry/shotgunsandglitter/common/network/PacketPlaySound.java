package com.teamwizardry.shotgunsandglitter.common.network;

import com.teamwizardry.librarianlib.features.autoregister.PacketRegister;
import com.teamwizardry.librarianlib.features.network.PacketBase;
import com.teamwizardry.librarianlib.features.saving.Save;
import com.teamwizardry.librarianlib.features.utilities.client.ClientRunnable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@PacketRegister(Side.CLIENT)
public class PacketPlaySound extends PacketBase {

	@Save
	public ResourceLocation sound;
	@Save
	public String category;
	@Save
	public double x;
	@Save
	public double y;
	@Save
	public double z;
	@Save
	public float volume;
	@Save
	public float pitch;
	@Save
	public double distance;

	public PacketPlaySound() {
	}

	public PacketPlaySound(SoundEvent sound, SoundCategory category, double x, double y, double z, double distance, float volume, float pitch) {
		this.sound = sound.getRegistryName();
		this.category = category.getName();
		this.x = x;
		this.y = y;
		this.z = z;
		this.volume = volume;
		this.pitch = pitch;
		this.distance = distance;
	}

	@Override
	public void handle(@NotNull MessageContext ctx) {
		if (ctx.side.isServer()) return;

		SoundEvent event = SoundEvent.REGISTRY.getObject(sound);
		if (event == null) return;
		SoundCategory cat = SoundCategory.getByName(category);

		ClientRunnable.run(new ClientRunnable() {
			@SideOnly(Side.CLIENT)
			@Override
			public void runIfClient() {
				Minecraft mc = Minecraft.getMinecraft();

				double dist = mc.player.getDistance(x, y, z);
				if (dist > distance) return;
				//		mc.player.sendChatMessage(volume + " - " + MathHelper.clamp((dist / 40.0), 0.1f, 1f) + " - " + (volume * MathHelper.clamp((dist / 40.0), 0.1f, 1f)) + "");

				PositionedSoundRecord positionedsoundrecord = new PositionedSoundRecord(event, cat, (float) (volume * MathHelper.clamp((dist / 40.0), 0.1f, 1f)), pitch, (float) x, (float) y, (float) z);

				mc.getSoundHandler().playSound(positionedsoundrecord);
			}
		});
	}
}
