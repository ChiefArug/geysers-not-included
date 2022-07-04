package chiefarug.mods.geysers_not_included.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GeyserBlockEntity extends BlockEntity {

	public GeyserBlockEntity(BlockEntityType<GeyserBlockEntity> pType, BlockPos pWorldPosition, BlockState pBlockState) {
		super(pType, pWorldPosition, pBlockState);
	}
}
