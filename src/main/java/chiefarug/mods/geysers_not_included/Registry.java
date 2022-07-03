package chiefarug.mods.geysers_not_included;

import chiefarug.mods.geysers_not_included.blocks.GeyserBlock;
import chiefarug.mods.geysers_not_included.blocks.GeyserBlockEntity;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import static chiefarug.mods.geysers_not_included.GeysersNotIncluded.MODID;

public class Registry {

	public static final CreativeModeTab C_TAB = new CreativeModeTab("Geysers Not Included") {
		@Override
		public @NotNull ItemStack makeIcon() {
			return new ItemStack(GEYSER_BLOCK_ITEM.get());
		}
	};

	public static final BlockBehaviour.Properties BLOCK_PROPERTIES = BlockBehaviour.Properties.of(Material.STONE);
	public static final Item.Properties ITEM_PROPERTIES = new Item.Properties().tab(C_TAB);

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);


	public static final RegistryObject<GeyserBlock> GEYSER_BLOCK = BLOCKS.register("powergen", () -> new GeyserBlock(BLOCK_PROPERTIES));
	public static final RegistryObject<GeyserBlockEntity> GEYSER_BLOCK_ENTITY = BLOCK_ENTITIES.register("powergen", () -> BlockEntityType.Builder.of(, GEYSER_BLOCK.get()).build(null));
	public static final RegistryObject<Item> GEYSER_BLOCK_ITEM = ITEMS.register("geyser", () -> new Item(ITEM_PROPERTIES));

	public static void init() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		BLOCKS.register(bus);
		ITEMS.register(bus);
	}

}
