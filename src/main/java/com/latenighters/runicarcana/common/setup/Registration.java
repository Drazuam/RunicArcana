package com.latenighters.runicarcana.common.setup;

import com.latenighters.runicarcana.common.blocks.ArcanaCollector;
import com.latenighters.runicarcana.common.blocks.ArcanaPylon;
import com.latenighters.runicarcana.common.blocks.PrincipicBlock;
import com.latenighters.runicarcana.common.blocks.tile.TileArcanaCollector;
import com.latenighters.runicarcana.common.blocks.tile.TileArcanaPylon;
import com.latenighters.runicarcana.common.items.ChalkItem;
import com.latenighters.runicarcana.common.items.CrystalToolItem;
import com.latenighters.runicarcana.common.items.SoothLensItem;
import com.latenighters.runicarcana.common.items.TransportRodItem;
import com.latenighters.runicarcana.common.items.armor.PrincipicBootsItem;
import com.latenighters.runicarcana.common.items.armor.PrincipicChestplateItem;
import com.latenighters.runicarcana.common.items.armor.PrincipicHelmetItem;
import com.latenighters.runicarcana.common.items.armor.PrincipicLeggingsItem;
import com.latenighters.runicarcana.common.items.trinkets.EliminationRingItem;
import com.latenighters.runicarcana.common.items.trinkets.HearthstoneItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.latenighters.runicarcana.RunicArcana.MODID;

public class Registration {

    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<TileEntityType<?>> TILES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, MODID);
//    private static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, MODID);
//    private static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, MODID);
//    private static final DeferredRegister<ModDimension> DIMENSIONS = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, MODID);



    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
//        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
//        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
//        DIMENSIONS.register(FMLJavaModLoadingContext.get().getModEventBus());

    }

    //Common Properties
    public static Block.Properties arcanaMachineProps = Block.Properties
            .create(Material.ROCK)
            .hardnessAndResistance(1.5f)
            .harvestTool(ToolType.PICKAXE)
            .lightValue(5)
            .notSolid();


    // Item Registration
    public static final RegistryObject<ChalkItem> CHALK = ITEMS.register("chalk", ChalkItem::new);
    public static final RegistryObject<SoothLensItem> LENS_OF_SOOTH = ITEMS.register("lens_of_sooth", SoothLensItem::new);
    public static final RegistryObject<CrystalToolItem> CRYSTAL_TOOL = ITEMS.register("crystal_tool", CrystalToolItem::new);
    public static final RegistryObject<TransportRodItem> TRANSPORT_ROD = ITEMS.register("transport_rod", TransportRodItem::new);
    public static final RegistryObject<HearthstoneItem> HEARTHSTONE = ITEMS.register("hearthstone", HearthstoneItem::new);
    public static final RegistryObject<EliminationRingItem> ELIMINATION_RING = ITEMS.register("elimination_ring", EliminationRingItem::new);

    public static final RegistryObject<PrincipicHelmetItem> PRINCIPIC_HELMET = ITEMS.register("principic_helmet", PrincipicHelmetItem::new);
    public static final RegistryObject<PrincipicChestplateItem> PRINCIPIC_CHESTPLATE = ITEMS.register("principic_chestplate", PrincipicChestplateItem::new);
    public static final RegistryObject<PrincipicLeggingsItem> PRINCIPIC_LEGGINGS = ITEMS.register("principic_leggings", PrincipicLeggingsItem::new);
    public static final RegistryObject<PrincipicBootsItem> PRINCIPIC_BOOTS = ITEMS.register("principic_boots", PrincipicBootsItem::new);

    // Block Registration
    public static final RegistryObject<PrincipicBlock> PRINCIPIC_BLOCK = BLOCKS.register("principic_block", PrincipicBlock::new);
    public static final RegistryObject<Item> PRINCIPIC_BLOCK_ITEM = ITEMS.register("principic_block", () -> new BlockItem(PRINCIPIC_BLOCK.get(), new Item.Properties().group(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<ArcanaPylon> ARCANA_PYLON_BLOCK = BLOCKS.register("arcana_pylon_block", ()-> new ArcanaPylon(arcanaMachineProps));
    public static final RegistryObject<Item> ARCANA_PYLON_BLOCK_ITEM = ITEMS.register("arcana_pylon_block", () -> new BlockItem(ARCANA_PYLON_BLOCK.get(), new Item.Properties().group(ModSetup.ITEM_GROUP)));

    public static final RegistryObject<ArcanaCollector> ARCANA_COLLECTOR_BLOCK = BLOCKS.register("arcana_collector_block",() -> new ArcanaCollector(arcanaMachineProps));
    public static final RegistryObject<Item> ARCANA_COLLECTOR_BLOCK_ITEM = ITEMS.register("arcana_collector_block", () -> new BlockItem(ARCANA_COLLECTOR_BLOCK.get(), new Item.Properties().group(ModSetup.ITEM_GROUP)));

    //Tile Entity Registration
    public static final RegistryObject<TileEntityType<TileArcanaCollector>> ARCANA_COLLECTOR_TILE = TILES.register("arcana_collector_tile", ()->TileEntityType.Builder.create(TileArcanaCollector::new,ARCANA_COLLECTOR_BLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<TileArcanaPylon>> ARCANA_PYLON_TILE = TILES.register("arcana_pylon_tile", ()->TileEntityType.Builder.create(TileArcanaPylon::new,ARCANA_PYLON_BLOCK.get()).build(null));

}
