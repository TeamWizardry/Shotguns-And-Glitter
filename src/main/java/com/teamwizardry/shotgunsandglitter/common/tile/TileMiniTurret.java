package com.teamwizardry.shotgunsandglitter.common.tile;

import com.teamwizardry.librarianlib.features.autoregister.TileRegister;
import com.teamwizardry.librarianlib.features.base.block.tile.TileModTickable;
import com.teamwizardry.librarianlib.features.base.block.tile.module.ModuleInventory;
import com.teamwizardry.librarianlib.features.saving.Module;
import com.teamwizardry.librarianlib.features.saving.Save;
import com.teamwizardry.librarianlib.features.tesr.TileRenderer;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.util.RandUtil;
import com.teamwizardry.shotgunsandglitter.client.render.TESRMiniTurret;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import com.teamwizardry.shotgunsandglitter.common.items.ItemBullet;
import com.teamwizardry.shotgunsandglitter.common.items.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@TileRegister(value = "mini_turret")
@TileRenderer(TESRMiniTurret.class)
public class TileMiniTurret extends TileModTickable {

	@Save
	private int targetID = -1;
	@Save
	private int cooldown = 0;

	@Save
	@Nullable
	private UUID owner = null;

	@Module
	public ModuleInventory fuelInv = new ModuleInventory(new ItemStackHandler() {
		@Override
		protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
			if (TileEntityFurnace.getItemBurnTime(stack) > 0)
				return super.getStackLimit(slot, stack);
			else return 0;
		}
	});

	@Module
	public ModuleInventory inventory = new ModuleInventory(new ItemStackHandler() {
		@Override
		protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
			if (stack.getItem() instanceof ItemBullet && BulletType.byOrdinal(stack.getItemDamage()) == BulletType.PISTOL)
				return super.getStackLimit(slot, stack);
			else return 0;
		}
	});

	public TileMiniTurret() {
		inventory.getHandler().setSize(1000);
		inventory.setSides(EnumFacing.UP, EnumFacing.DOWN);

		fuelInv.setSides(EnumFacing.HORIZONTALS);
	}

	@Override
	public void tick() {
		if (cooldown > 0) {
			cooldown--;
			markDirty();
		} else {

			if (!world.isBlockPowered(getPos())) return;

			boolean emptyAmmo = true;
			int fullSlot = -1;
			for (int i = 0; i < inventory.getHandler().getSlots(); i++) {
				if (!inventory.getHandler().getStackInSlot(i).isEmpty()) {
					emptyAmmo = false;
					fullSlot = i;
					break;
				}
			}

			if (emptyAmmo) return;

			List<EntityLivingBase> entities = world.getEntities(EntityLivingBase.class, input -> {
				if (input == null) return false;
				double dist = input.getDistanceSq(getPos());
				if (dist > 64 * 64) return false;
				if (dist < 4 * 4) return false;
				return owner == null || !owner.equals(input.getUniqueID());
			});

			entities.sort(Comparator.comparingDouble(o -> o.getDistanceSq(getPos())));

			if (entities.isEmpty()) {
				if (targetID != -1) {
					targetID = -1;
					markDirty();
				}
				return;
			}

			boolean fueled = false;
			for (int i = 0; i < fuelInv.getHandler().getSlots(); i++) {
				if (!fuelInv.getHandler().getStackInSlot(i).isEmpty()) {
					fuelInv.getHandler().extractItem(i, 1, false);
					fueled = true;
					break;
				}
			}
			if (!fueled) return;

			EntityLivingBase target = entities.get(0);
			if (targetID != target.getEntityId()) {
				targetID = target.getEntityId();
				markDirty();
			}

			ItemStack ammo = inventory.getHandler().extractItem(fullSlot, 1, false);

			Effect effect = ModItems.BULLET.getEffectFromItem(ammo);
			Vec3d normal = target.getPositionVector().addVector(0, target.getEyeHeight(), 0)
					.subtract(new Vec3d(getPos()).addVector(0.5, 0.5, 0.5))
					.add(new Vec3d(target.motionX, target.motionY, target.motionZ).normalize())
					.normalize();
			Vec3d position = new Vec3d(getPos()).addVector(0.5, 0.5, 0.5).add(normal);

			if (!world.isRemote) {
				EntityBullet bullet = new EntityBullet(world, normal, BulletType.PISTOL, effect, 0f, 1f); // Todo: potency

				bullet.setPosition(position.x, position.y, position.z);
				world.spawnEntity(bullet);
			} else {
				if (effect.getFireSound() != null)
					world.playSound(pos.getX(), pos.getY(), pos.getZ(), effect.getFireSound(), SoundCategory.PLAYERS, RandUtil.nextFloat(3f, 4f), RandUtil.nextFloat(0.95f, 1.1f), false);
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), ModSounds.SHOT_PISTOL, SoundCategory.PLAYERS, RandUtil.nextFloat(3f, 4f), RandUtil.nextFloat(0.95f, 1.1f), false);
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), ModSounds.MAGIC_SPARKLE, SoundCategory.PLAYERS, RandUtil.nextFloat(3f, 4f), RandUtil.nextFloat(0.95f, 1.1f), false);
			}

			cooldown = 40;
			markDirty();
		}
	}

	@Nullable
	public UUID getOwner() {
		return owner;
	}

	public void setOwner(@Nullable UUID owner) {
		this.owner = owner;
	}
}
