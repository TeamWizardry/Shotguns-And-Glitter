package com.teamwizardry.shotgunsandglitter.common.entity;

import com.google.common.base.Optional;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

/**
 * @author WireSegal
 * Created at 2:46 PM on 3/31/18.
 */
@SuppressWarnings("Guava")
public class EntityDroppingBlock extends Entity {
	private static final DataParameter<BlockPos> ORIGIN =
			EntityDataManager.createKey(EntityDroppingBlock.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<Optional<IBlockState>> STATE =
			EntityDataManager.createKey(EntityDroppingBlock.class, DataSerializers.OPTIONAL_BLOCK_STATE);

	public int fallTime = 0;
	public boolean shouldDropItem = true;
	public NBTTagCompound tileEntityData = null;

	public EntityDroppingBlock(World world) {
		super(world);
	}

	public EntityDroppingBlock(World world, double x, double y, double z, IBlockState fallingBlockState) {
		super(world);
		setBlock(fallingBlockState);
		preventEntitySpawning = true;
		setSize(0.98f, 0.98f);
		setPosition(x, y + ((1.0f - height) / 2.0f), z);
		motionX = 0.0;
		motionY = 0.0;
		motionZ = 0.0;
		prevPosX = x;
		prevPosY = y;
		prevPosZ = z;
		setOrigin(new BlockPos(this));
	}

	public BlockPos getOrigin() {
		return dataManager.get(ORIGIN);
	}

	public void setOrigin(BlockPos origin) {
		dataManager.set(ORIGIN, origin);
	}

	public IBlockState getBlock() {
		return dataManager.get(STATE).or(Blocks.AIR.getDefaultState());
	}

	public void setBlock(IBlockState state) {
		dataManager.set(STATE, Optional.of(state));
	}

	public EntityDroppingBlock withDrop(boolean should) {
		shouldDropItem = should;
		return this;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected void entityInit() {
		dataManager.register(ORIGIN, BlockPos.ORIGIN);
		dataManager.register(STATE, Optional.absent());
	}

	@Override
	public boolean canBeCollidedWith() {
		return !isDead;
	}

	@Override
	public void onUpdate() {
		Block block = getBlock().getBlock();

		if (getBlock().getMaterial() == Material.AIR) setDead();
		else {
			prevPosX = posX;
			prevPosY = posY;
			prevPosZ = posZ;

			fallTime++;

			if (!hasNoGravity())
				motionY -= 0.04;

			move(MoverType.SELF, motionX, motionY, motionZ);
			motionX *= 0.98;
			motionY *= 0.98;
			motionZ *= 0.98;

			if (!world.isRemote) {
				BlockPos selfPos = new BlockPos(this);

				if (onGround) {
					IBlockState stateInWorld = world.getBlockState(selfPos);

					BlockPos slightlyDown = new BlockPos(posX, posY - 0.001, posZ);
					if (world.isAirBlock(slightlyDown) &&
							BlockFalling.canFallThrough(world.getBlockState(slightlyDown))) {
						onGround = false;
						return;
					}

					motionX *= 0.7;
					motionZ *= 0.7;
					motionY *= -0.5;

					if (stateInWorld.getBlock() != Blocks.PISTON_EXTENSION) {
						setDead();

						if (block instanceof BlockFalling)
							((BlockFalling) block).onBroken(world, selfPos);

						die();
					}

				} else if (fallTime > 100 && (selfPos.getY() < 1 || selfPos.getY() > 256) || fallTime > 600) {
					die();

					setDead();
				}
			}
		}
	}

	public void die() {
		Block block = getBlock().getBlock();
		if (shouldDropItem && world.getGameRules().getBoolean("doTileDrops"))
			entityDropItem(new ItemStack(block, 1, block.damageDropped(getBlock())), 0.0f);

		world.playEvent(2001, getPosition(), Block.getStateId(getBlock()));
	}

	@Override
	public boolean canRenderOnFire() {
		return false;
	}

	@Override
	public boolean ignoreItemEntityData() {
		return true;
	}

	protected void writeEntityToNBT(@NotNull NBTTagCompound compound) {
		Block block = getBlock().getBlock();
		ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(block);
		compound.setString("Block", resourcelocation.toString());
		compound.setByte("Data", (byte) block.getMetaFromState(getBlock()));
		compound.setInteger("Time", fallTime);
		compound.setBoolean("DropItem", shouldDropItem);

		if (tileEntityData != null)
			compound.setTag("TileEntityData", tileEntityData);
	}

	@SuppressWarnings("deprecation")
	protected void readEntityFromNBT(@NotNull NBTTagCompound compound) {
		int data = compound.getByte("Data") & 255;

		Block block = Block.getBlockFromName(compound.getString("Block"));
		if (block == null) block = Blocks.AIR;

		setBlock(block.getStateFromMeta(data));

		fallTime = compound.getInteger("Time");

		if (compound.hasKey("DropItem", 99))
			shouldDropItem = compound.getBoolean("DropItem");

		if (compound.hasKey("TileEntityData", 10))
			tileEntityData = compound.getCompoundTag("TileEntityData");

		if (block.getDefaultState().getMaterial() == Material.AIR)
			setBlock(Blocks.SAND.getDefaultState());
	}

	public void addEntityCrashInfo(CrashReportCategory category) {
		super.addEntityCrashInfo(category);

		category.addCrashSection("Imitating block ID", Block.getIdFromBlock(getBlock().getBlock()));
		category.addCrashSection("Imitating block data", getBlock().getBlock().getMetaFromState(getBlock()));
	}
}

