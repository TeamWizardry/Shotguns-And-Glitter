package com.teamwizardry.shotgunsandglitter.common.entity;

import com.teamwizardry.librarianlib.features.base.entity.EntityMod;
import com.teamwizardry.shotgunsandglitter.api.BulletType;
import com.teamwizardry.shotgunsandglitter.api.Effect;
import com.teamwizardry.shotgunsandglitter.api.EffectRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityBullet extends EntityMod {

	private BulletType bulletType = BulletType.SMALL;
	private Effect effect;

	public EntityBullet(@Nonnull World world) {
		super(world);
		setSize(0.1F, 0.1F);
		isAirBorne = true;
	}

	public EntityBullet(@Nonnull World world, @Nonnull Entity caster, @Nonnull BulletType bulletType, @Nonnull Effect effect) {
		super(world);
		setSize(0.1F, 0.1F);
		isAirBorne = true;

		this.bulletType = bulletType;
		this.effect = effect;

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
		return bulletType;
	}

	public Effect getEffect() {
		return effect;
	}

	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double distance) {
		return distance < 4096.0D;
	}

	@SideOnly(Side.CLIENT)
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

		compound.setString("bullet_type", bulletType.name());
		if (effect != null)
			compound.setString("effect", effect.getID());
	}

	@Override
	public void readCustomNBT(@NotNull NBTTagCompound compound) {
		super.readCustomNBT(compound);

		if (compound.hasKey("bullet_type")) {
			bulletType = BulletType.valueOf(compound.getString("bullet_type"));
		}
		if (compound.hasKey("effect")) {
			effect = EffectRegistry.getEffectByID(compound.getString("effect"));
		}
	}
}
