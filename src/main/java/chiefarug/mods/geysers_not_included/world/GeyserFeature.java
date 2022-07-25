package chiefarug.mods.geysers_not_included.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;

import java.util.Optional;
import java.util.Random;

public class GeyserFeature extends StructureFeature<JigsawConfiguration> {

	public GeyserFeature() {
		super(JigsawConfiguration.CODEC, GeyserFeature::createPiecesGenerator);
	}

	public static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
		ChunkPos chunkPos = context.chunkPos();
		WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(0L));
		random.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);

		BlockPos.MutableBlockPos genPos = chunkPos.getWorldPosition().mutable();
		offset(genPos, random);

		NoiseColumn column = context.chunkGenerator().getBaseColumn(genPos.getX(), genPos.getZ(), context.heightAccessor());

		int minY = context.heightAccessor().getMinBuildHeight();
		int maxY = context.chunkGenerator().getBaseHeight(genPos.getX(), genPos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());

		int currentY = minY;
		boolean caveFound = false;
		while (!caveFound) {
			if (currentY > maxY) {
				return Optional.empty();
			}
			caveFound = column.getBlock(currentY).isAir();
			currentY++;
		}
		genPos.setY(currentY);

		// I use this to debug and quickly find out if the structure is spawning or not and where it is.
		// This is returning the coordinates of the center starting piece.
		//GeysersNotIncluded.LOGGER.debug("Geyser located at {}", genPos);

		// Return the pieces generator that is now set up so that the game runs it when it needs to create the layout of structure pieces.
		return JigsawPlacement.addPieces(
				context,
				PoolElementStructurePiece::new,
				genPos,
				false,
				false);
	}

	private static void offset(BlockPos.MutableBlockPos pos, Random random) {
		byte[] offsets = new byte[2];
		random.nextBytes(offsets);
		for (int i = 0;i < offsets.length; i++) {
			// divide by 8
			offsets[i] = (byte) (offsets[i] >> 3);
		}
		pos.offset(offsets[0], 0, offsets[1]);
	}

	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
	}
}
