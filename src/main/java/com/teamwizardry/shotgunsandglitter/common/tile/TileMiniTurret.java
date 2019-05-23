package com.teamwizardry.shotgunsandglitter.common.tile;

import com.teamwizardry.librarianlib.features.autoregister.TileRegister;
import com.teamwizardry.librarianlib.features.base.block.tile.TileModTickable;
import com.teamwizardry.librarianlib.features.base.block.tile.module.ModuleInventory;
import com.teamwizardry.librarianlib.features.config.ConfigDoubleRange;
import com.teamwizardry.librarianlib.features.config.ConfigIntRange;
import com.teamwizardry.librarianlib.features.config.ConfigProperty;
import com.teamwizardry.librarianlib.features.saving.Module;
import com.teamwizardry.librarianlib.features.saving.Save;
import com.teamwizardry.librarianlib.features.tesr.TileRenderer;
import com.teamwizardry.shotgunsandglitter.api.BulletEffect;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.SoundSystem;
import com.teamwizardry.shotgunsandglitter.client.render.TESRMiniTurret;
import com.teamwizardry.shotgunsandglitter.common.core.MiniTurretHelper;
import com.teamwizardry.shotgunsandglitter.common.core.ModSounds;
import com.teamwizardry.shotgunsandglitter.common.entity.EntityBullet;
import com.teamwizardry.shotgunsandglitter.common.items.ItemBullet;
import com.teamwizardry.shotgunsandglitter.common.items.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.IMob;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
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

	@ConfigDoubleRange(min =  1.0, max = 64.0)
	@ConfigProperty(category = "turret", comment = "Enter the max radius a turret can cover")
	public static double radius = 64;

	@ConfigProperty(category = "turret", comment = "Disables the ability for the turret to target passive mobs")
	public static boolean targetPassives = true;


	@Save
	private int fueledTime = 0;

	@Save
	private int targetID = -1;
	@Save
	private int cooldown = 0;

	@Save
	private boolean rightBarrel = false;

	@Save
	public boolean firing = false;

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
			if (stack.getItem() instanceof ItemBullet && BulletType.byOrdinal(stack.getItemDamage()) == BulletType.BASIC)
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
			if(fueledTime > 0){
				fueledTime--;
				markDirty();
			}


			if (!world.isBlockPowered(getPos())) {
				if (firing) {
					firing = false;
					markDirty();
				}
				return;
			}

			boolean emptyAmmo = true;
			int fullSlot = -1;
			for (int i = 0; i < inventory.getHandler().getSlots(); i++) {
				if (!inventory.getHandler().getStackInSlot(i).isEmpty()) {
					emptyAmmo = false;
					fullSlot = i;
					break;
				}
			}

			if (emptyAmmo) {
				if (firing) {
					firing = false;
					markDirty();
				}
				return;
			}

			List<EntityLivingBase> entities = world.getEntities(EntityLivingBase.class, input -> {
				if (input == null) return false;
				double dist = input.getDistanceSq(getPos());
				return !(dist > radius * radius) && !(dist < 4 * 4) && (owner == null || !owner.equals(input.getUniqueID())) && !MiniTurretHelper.isEntityBlacklisted(input) && !(targetPassives && input.isCreatureType(EnumCreatureType.CREATURE, false)) ;
			});

			entities.sort(Comparator.comparingDouble(o -> o.getDistanceSq(getPos())));

			if (entities.isEmpty()) {
				boolean mark = false;
				if (firing) {
					firing = false;
					mark = true;
				}
				if (targetID != -1) {
					targetID = -1;
					mark = true;
				}
				if (mark) markDirty();
				return;
			}

			for (int i = 0; i < fuelInv.getHandler().getSlots(); i++) {
				if (!fuelInv.getHandler().getStackInSlot(i).isEmpty()) {
					fueledTime = TileEntityFurnace.getItemBurnTime(fuelInv.getHandler().extractItem(i, 1, false));
					markDirty();
					break;
				}
			}
			if (fueledTime == 0) {
				if (firing) {
					firing = false;
					markDirty();
				}
				return;
			}

			EntityLivingBase target = entities.get(0);
			if (targetID != target.getEntityId()) {
				targetID = target.getEntityId();
				markDirty();
			}

			ItemStack ammo = inventory.getHandler().extractItem(fullSlot, 1, false);

			BulletEffect bulletEffect = ModItems.BULLET.getEffectFromItem(ammo);
			Vec3d normal = target.getPositionVector().add(0, target.getEyeHeight(), 0)
					.subtract(new Vec3d(getPos()).add(0.5, 0.5, 0.5))
					.add(new Vec3d(target.motionX, 0, target.motionZ).normalize())
					.normalize();
			Vec3d position = new Vec3d(getPos()).add(0.5, 0.5, 0.5).add(normal);

			if (!world.isRemote) {
				EntityBullet bullet = new EntityBullet(world, normal, BulletType.BASIC, bulletEffect, 0f, 1f); // Todo: potency

				bullet.setPosition(position.x, position.y, position.z);
				world.spawnEntity(bullet);
			}

			SoundSystem.playSounds(world, pos.getX(), pos.getY(), pos.getZ(), ModSounds.SHOT_PISTOL, ModSounds.MAGIC_SPARKLE, bulletEffect.getFireSound());

			if (!firing) firing = true;
			cooldown = 40;
			rightBarrel = !rightBarrel;
			markDirty();
		}
	}

	public ModuleInventory getFuelInv() {
		return fuelInv;
	}

	public ModuleInventory getInventory() {
		return inventory;
	}

	public boolean isRightBarrel() {
		return rightBarrel;
	}

	public int getCooldown() {
		return cooldown;
	}

	public int getTargetID() {
		return targetID;
	}

	public int getFueledTime() {
		return  fueledTime;
	}

	@Nullable
	public UUID getOwner() {
		return owner;
	}

	public void setOwner(@Nullable UUID owner) {
		this.owner = owner;
	}
}
