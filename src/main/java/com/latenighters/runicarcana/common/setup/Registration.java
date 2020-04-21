package com.latenighters.runicarcana.common.setup;

import com.latenighters.runicarcana.common.blocks.PrincipicBlock;
import com.latenighters.runicarcana.common.items.ChalkItem;
import com.latenighters.runicarcana.common.items.TransportRodItem;
import com.latenighters.runicarcana.common.items.trinkets.EliminationRingItem;
import com.latenighters.runicarcana.common.items.trinkets.HearthstoneItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
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

    // Item Registration
    public static final RegistryObject<ChalkItem> CHALK = ITEMS.register("chalk", ChalkItem::new);
    public static final RegistryObject<TransportRodItem> TRANSPORT_ROD = ITEMS.register("transport_rod", TransportRodItem::new);
    public static final RegistryObject<HearthstoneItem> HEARTHSTONE = ITEMS.register("hearthstone", HearthstoneItem::new);
    public static final RegistryObject<EliminationRingItem> ELIMINATION_RING = ITEMS.register("elimination_ring", EliminationRingItem::new);

    // Block Registration
    public static final RegistryObject<PrincipicBlock> PRINCIPIC_BLOCK = BLOCKS.register("principic_block", PrincipicBlock::new);
    public static final RegistryObject<Item> PRINCIPIC_BLOCK_ITEM = ITEMS.register("principic_block", () -> new BlockItem(PRINCIPIC_BLOCK.get(), new Item.Properties().group(ModSetup.ITEM_GROUP)));

}
