package chiefarug.mods.geysers_not_included.world;

import chiefarug.mods.geysers_not_included.GeysersNotIncluded;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

import static chiefarug.mods.geysers_not_included.GNIRegistry.GEYSER_BLOCK;
import static chiefarug.mods.geysers_not_included.blocks.GeyserBlockEntity.typeSerializedName;

public class GeyserProcessor extends StructureProcessor {

	public GeyserProcessor(List<ResourceLocation> types) {
		this.fluids = types;
	}
	public GeyserProcessor() {
		this.fluids = DEFAULT_FLUIDS;
	}

	public static final GeyserProcessor DEFAULT_INSTANCE = new GeyserProcessor();
	public static final List<ResourceLocation> DEFAULT_FLUIDS = List.of(new ResourceLocation("minecraft:lava"));
	public List<ResourceLocation> fluids;

	public static final Codec<GeyserProcessor> CODEC = ResourceLocation.CODEC.listOf().fieldOf("types").xmap(GeyserProcessor::new, (instance) -> instance.fluids).orElse(DEFAULT_INSTANCE).codec();

	@Nullable
	@Override
	public StructureTemplate.StructureBlockInfo process(LevelReader level, BlockPos p_74300_, BlockPos pos, StructureTemplate.StructureBlockInfo blockInfo, StructureTemplate.StructureBlockInfo relativeBlockInfo, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
		BlockState state = relativeBlockInfo.state;

		GeysersNotIncluded.LOGGER.debug("Processing {} ", state);
		if (state.is(GEYSER_BLOCK.get())) {
			CompoundTag nbt = relativeBlockInfo.nbt;
			nbt.putString(typeSerializedName, getFluid(settings.getRandom(relativeBlockInfo.pos)).toString());
			return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos, state, nbt);
		}
		return null;
	}

	public static StructureProcessorType<GeyserProcessor> TYPE = () -> CODEC;

	@Override
	protected StructureProcessorType<?> getType() {
		return TYPE;
	}

	private ResourceLocation getFluid(Random r) {
		return this.fluids.size() > 1?this.fluids.get(r.nextInt(fluids.size() - 1)):this.fluids.get(0);
	}
}
