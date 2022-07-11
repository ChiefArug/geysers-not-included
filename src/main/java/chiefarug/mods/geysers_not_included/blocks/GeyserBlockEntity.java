package chiefarug.mods.geysers_not_included.blocks;

import chiefarug.mods.geysers_not_included.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import static chiefarug.mods.geysers_not_included.Registry.GEYSER_BLOCK_ENTITY;

public class GeyserBlockEntity extends BlockEntity {

	public GeyserBlockEntity(BlockPos pPos, BlockState pState) {
		super(GEYSER_BLOCK_ENTITY.get(), pPos, pState);
	}

	// Game time that the last cycle change happened (it activated/deactivated)
	private long lastCycleSwap;
	private static final String lastCycleSwapSerializedName = "Swap";
	// Length of the inactive period, a delay between active periods where it does nothing
	private int inactiveCycle = 1200;
	private static final String inactiveCycleSerializedName = "Inactive";
	// Length of the active period, where every emitCycle ticks it will try to emit fluid
	private int activeCycle = 600;
	private static final String activeCycleSerializedName = "Active";
	// Length of the emit-cycle, every this ticks it will try to emit fluid
	private int emitCycle = 200;
	private static final String emitCycleSerializedName = "Emit";
	// Last time it successfully emitted fluid
	private long lastEmitted;
	private static final String lastEmittedSerializedName = "LastEmitted";
	// Type, based on a TODO datapack registry
	private ResourceLocation type;
	private static final String typeSerializedName = "Type";

	public void tickServer() {
		assert level != null;
		BlockPos pos = getBlockPos();
		boolean overPressured = false;
		if (isActive()) {
			if (level.getGameTime() >= lastEmitted + emitCycle) {
				boolean emitted = emitFluid();
				if (emitted) {
					lastEmitted = level.getGameTime();
				} else {
					overPressured = true;
					ServerLevel sLevel = (ServerLevel)level;
					sLevel.sendParticles(ParticleTypes.ANGRY_VILLAGER, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.4, 0.4, 0.4, 0.01);
				}
			}
			if (!overPressured && level.getGameTime() >= lastCycleSwap + activeCycle) {
				changeActive(false);
			}
		} else if (level.getGameTime() >= lastCycleSwap + inactiveCycle) {
			changeActive(true);
		}
	}

	private void changeActive(boolean b) {
		level.setBlock(getBlockPos(), getBlockState().setValue(Registry.ACTIVE, b), 3);
		lastCycleSwap = level.getGameTime();
	}

	public boolean emitFluid() {
		BlockPos emitPos = getBlockPos().above();
		Fluid fluid = Fluids.WATER;
		if (level.getBlockState(emitPos).canBeReplaced(fluid)) {
			level.setBlock(emitPos, fluid.defaultFluidState().createLegacyBlock(), 3);
			return true;
		}
		return false;
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);

		tag.putLong(lastCycleSwapSerializedName, lastCycleSwap);
		tag.putInt(inactiveCycleSerializedName, inactiveCycle);
		tag.putInt(activeCycleSerializedName, activeCycle);
		tag.putInt(emitCycleSerializedName, emitCycle);
		tag.putLong(lastEmittedSerializedName, lastEmitted);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains(lastCycleSwapSerializedName)) {
			lastCycleSwap = tag.getLong(lastCycleSwapSerializedName);
		}
		if (tag.contains(inactiveCycleSerializedName)) {
			inactiveCycle = tag.getInt(inactiveCycleSerializedName);
		}
		if (tag.contains(activeCycleSerializedName)) {
			activeCycle = tag.getInt(activeCycleSerializedName);
		}
		if (tag.contains(emitCycleSerializedName)) {
			emitCycle = tag.getInt(emitCycleSerializedName);
		}
		if (tag.contains(lastEmittedSerializedName)) {
			lastEmitted = tag.getLong(lastEmittedSerializedName);
		}
	}

	private boolean isActive() {
		return getBlockState().getValue(Registry.ACTIVE);
	}
}
