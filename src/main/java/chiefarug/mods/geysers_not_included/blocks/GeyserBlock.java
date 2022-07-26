package chiefarug.mods.geysers_not_included.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

import static chiefarug.mods.geysers_not_included.GNIRegistry.ACTIVE;


public class GeyserBlock extends Block implements EntityBlock {
	public GeyserBlock(Properties properties) {
		super(properties);
	}


	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new GeyserBlockEntity(pPos, pState);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
		if (!pLevel.isClientSide()) {
			return (level, pos, state, blockEntity) -> {
				if (blockEntity instanceof GeyserBlockEntity geyser) {
					geyser.tickServer();
				}
			};
		}
		return null;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
		if (isActive(state)) {
			level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, false, pos.getX(), pos.getY(), pos.getZ(), 0, 0.5, 0);
		}
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
		if (random.nextDouble() > 0.99) {
			BlockEntity entity = level.getBlockEntity(pos);
			if (entity instanceof GeyserBlockEntity geyser) {
				geyser.emitFluid();
			}
		}
	}

	@Override
	public boolean isRandomlyTicking(BlockState pState) {
		return pState.getValue(ACTIVE);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(ACTIVE);
	}


	public boolean isActive(BlockState state) {
		return state.getValue(ACTIVE);
	}
}
