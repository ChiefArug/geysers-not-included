package chiefarug.mods.geysers_not_included.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static chiefarug.mods.geysers_not_included.Registry.GEYSER_BLOCK_ENTITY;

public interface GeyserBlockEntitySupplier extends BlockEntityType.BlockEntitySupplier<GeyserBlockEntity>{
	@Override
	default GeyserBlockEntity create(BlockPos pPos, BlockState pState){
		return new GeyserBlockEntity(GEYSER_BLOCK_ENTITY.get(), pPos, pState);
	};
}
