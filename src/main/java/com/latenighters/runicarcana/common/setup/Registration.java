package com.latenighters.runicarcana.common.setup;

import com.latenighters.runicarcana.common.capabilities.ISymbolHandler;
import com.latenighters.runicarcana.common.capabilities.SymbolHandler;
import com.latenighters.runicarcana.common.capabilities.SymbolHandlerStorage;
import com.latenighters.runicarcana.common.items.ChalkItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

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

        CapabilityManager.INSTANCE.register(ISymbolHandler.class, new SymbolHandlerStorage(),new SymbolHandler.SymbolHandlerFactory());

    }

    // Item Registration
    public static final RegistryObject<ChalkItem> CHALK = ITEMS.register("chalk", ChalkItem::new);

}
