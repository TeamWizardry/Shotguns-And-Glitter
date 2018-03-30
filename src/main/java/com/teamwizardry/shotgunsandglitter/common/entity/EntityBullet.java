package com.teamwizardry.shotgunsandglitter.common.entity;

import com.teamwizardry.librarianlib.features.base.entity.EntityMod;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.EffectRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityBullet extends EntityMod {

	private static final DataParameter<Byte> BULLET_TYPE = EntityDataManager.createKey(EntityBullet.class, DataSerializers.BYTE);
	private static final DataParameter<String> BULLET_EFFECT = EntityDataManager.createKey(EntityBullet.class, DataSerializers.STRING);


	public EntityBullet(@Nonnull World world) {
		super(world);
		setSize(0.1F, 0.1F);
		isAirBorne = true;
	}

	public EntityBullet(@Nonnull World world, @Nonnull Entity caster, @Nonnull BulletType bulletType, @Nonnull Effect effect) {
		super(world);
		setSize(0.1F, 0.1F);
		isAirBorne = true;

		setBulletType(bulletType);
		setEffect(effect);

		rotationPitch = caster.rotationPitch;
		rotationYaw = caster.rotationYaw;
	}

	@Nullable
	@Override
	public AxisAlignedBB getCollisionBox(Entity entityIn) {
		return getEntityBoundingBox();
	}

	@Override
	protected void entityInit() {
		dataManager.register(BULLET_TYPE, (byte) 0);
		dataManager.register(BULLET_EFFECT, "");
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (world.isRemote) return;
		if (isDead) return;

		if (ticksExisted > 1000) world.removeEntity(this);

		motionX = getLook(0).x * 0.35;
		motionY = getLook(0).y * 0.35;
		motionZ = getLook(0).z * 0.35;

		move(MoverType.SELF, motionX, motionY, motionZ);
	}

	public BulletType getBulletType() {
		return BulletType.values()[dataManager.get(BULLET_TYPE) % BulletType.values().length];
	}

	public void setBulletType(BulletType type) {
		dataManager.set(BULLET_TYPE, (byte) type.ordinal());
	}

	@Nullable
	public Effect getEffect() {
		return EffectRegistry.getEffectByID(dataManager.get(BULLET_EFFECT));
	}

	public void setEffect(Effect effect) {
		dataManager.set(BULLET_EFFECT, effect.getID());
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean isInRangeToRenderDist(double distance) {
		return distance < 4096.0D;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean isInRangeToRender3d(double x, double y, double z) {
		return super.isInRangeToRender3d(x, y, z);
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public void writeCustomNBT(@NotNull NBTTagCompound compound) {
		super.writeCustomNBT(compound);

		compound.setByte("bullet_type", dataManager.get(BULLET_TYPE));
		compound.setString("effect", dataManager.get(BULLET_EFFECT));
	}

	@Override
	public void readCustomNBT(@NotNull NBTTagCompound compound) {
		super.readCustomNBT(compound);

		if (compound.hasKey("bullet_type"))
			dataManager.set(BULLET_TYPE, compound.getByte("bullet_type"));
		if (compound.hasKey("effect"))
			dataManager.set(BULLET_EFFECT, compound.getString("effect"));
	}
}
