package chiefarug.mods.geysers_not_included;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.JigsawFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;

import java.util.Optional;
import java.util.function.Predicate;

public class GeyserFeature extends StructureFeature<JigsawConfiguration> {


	public GeyserFeature() {
		super(JigsawConfiguration.CODEC, GeyserFeature::createPiecesGenerator);
	}

	public static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
		// Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
		ChunkPos chunkPos = context.chunkPos();
		NoiseColumn column = context.chunkGenerator().getBaseColumn(chunkPos.getMiddleBlockX(), chunkPos.getMiddleBlockZ(), context.heightAccessor());
		int minY = context.heightAccessor().getMinBuildHeight();
		int maxY = 100;

		int currentY = minY;
		boolean caveFound = false;
		while (!caveFound) {
			if (currentY > maxY) break;
			caveFound = column.getBlock(currentY).isAir();
			currentY++;
		}

		BlockPos genPos = chunkPos.getMiddleBlockPosition(currentY);
		Optional<PieceGenerator<JigsawConfiguration>> structurePiecesGenerator = JigsawPlacement.addPieces(
				context,
				PoolElementStructurePiece::new,
				genPos.above(100),
				false,
				false);

		if (currentY > 60) {
			structurePiecesGenerator = Optional.empty();
			GeysersNotIncluded.LOGGER.debug("Tried to generate a geyser on ground level at {}", genPos);
		}

		if(structurePiecesGenerator.isPresent()) {
			// I use this to debug and quickly find out if the structure is spawning or not and where it is.
			// This is returning the coordinates of the center starting piece.
			GeysersNotIncluded.LOGGER.debug("Geyser located at {}", genPos);
		}

		// Return the pieces generator that is now set up so that the game runs it when it needs to create the layout of structure pieces.
		return structurePiecesGenerator;
	}

	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
	}
}
