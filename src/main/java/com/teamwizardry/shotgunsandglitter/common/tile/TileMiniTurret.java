package com.teamwizardry.shotgunsandglitter.common.tile;

import com.teamwizardry.librarianlib.features.autoregister.TileRegister;
import com.teamwizardry.librarianlib.features.base.block.tile.TileModTickable;
import com.teamwizardry.librarianlib.features.saving.Save;
import com.teamwizardry.librarianlib.features.tesr.TileRenderer;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.EffectRegistry;
import com.teamwizardry.shotgunsandglitter.client.render.TESRMiniTurret;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.Constants;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@TileRegister(value = "mini_turret")
@TileRenderer(TESRMiniTurret.class)
public class TileMiniTurret extends TileModTickable {

	private final List<Effect> ammo = new ArrayList<>();

	@Save
	private int targetID = -1;

	@Save
	@Nullable
	private UUID owner = null;

	@Override
	public void writeCustomNBT(@NotNull NBTTagCompound cmp, boolean sync) {
		super.writeCustomNBT(cmp, sync);

		NBTTagList list = new NBTTagList();
		for (Effect effect : ammo) {
			list.appendTag(new NBTTagString(effect.getID()));
		}
		cmp.setTag("ammo", list);
	}

	@Override
	public void readCustomNBT(@NotNull NBTTagCompound cmp) {
		super.readCustomNBT(cmp);

		if (cmp.hasKey("ammo")) {
			ammo.clear();
			NBTTagList list = cmp.getTagList("ammo", Constants.NBT.TAG_STRING);
			for (NBTBase base : list) {
				if (!(base instanceof NBTTagString)) continue;
				Effect effect = EffectRegistry.getEffectByID(((NBTTagString) base).getString());
				ammo.add(effect);
			}
		}
	}

	@Override
	public void tick() {
		if (world.isRemote) return;

		if (getAmmo().isEmpty()) return;

		List<EntityLivingBase> entities = world.getEntities(EntityLivingBase.class, input -> input != null && !(input.getDistanceSqToCenter(getPos()) > 32 * 32) && (owner == null || input.getUniqueID().equals(owner)));
		entities.sort(Comparator.comparingDouble(o -> o.getDistanceSq(getPos())));

		if (entities.isEmpty()) return;

		EntityLivingBase target = entities.get(0);
		if (targetID != target.getEntityId()) {
			targetID = target.getEntityId();
			markDirty();
		}

		Effect effect = ammo.get(0);
		Vec3d normal = target.getPositionVector().subtract(new Vec3d(getPos()));
		EntityBullet bullet = new EntityBullet(world, normal, BulletType.SMALL, effect, 4f);

		Vec3d position = new Vec3d(getPos()).addVector(0.5, 0.5, 0.5).add(normal.scale(1.0 / 2.0));
		bullet.setPosition(position.x, position.y, position.z);
		world.spawnEntity(bullet);
	}

	@Nullable
	public UUID getOwner() {
		return owner;
	}

	public void setOwner(@Nullable UUID owner) {
		this.owner = owner;
	}

	public List<Effect> getAmmo() {
		return ammo;
	}
}
