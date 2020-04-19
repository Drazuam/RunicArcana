package com.latenighters.runicarcana.common.symbols.backend;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryInternal;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import org.jline.utils.Log;

import javax.annotation.Nullable;

import static com.latenighters.runicarcana.RunicArcana.MODID;

public class SymbolRegistryHandler {

    public static final IForgeRegistry<Symbol> SYMBOLS = RegistryManager.ACTIVE.getRegistry(Symbol.class);
    public static final ResourceLocation SYMBOLS_RL = new ResourceLocation(MODID, "symbols");

    public static void onCreateRegistryEvent(final RegistryEvent.NewRegistry evt)
    {
        RegistryBuilder<Symbol> symbolRegistryBuilder = new RegistryBuilder<Symbol>();
        symbolRegistryBuilder.add(new IForgeRegistry.AddCallback<Symbol>() {
            @Override
            public void onAdd(IForgeRegistryInternal<Symbol> owner, RegistryManager stage, int id, Symbol obj, @Nullable Symbol oldObj) {
                Log.debug("Registering " + obj.getRegistryName() + "with index " + Integer.toString(id));
                obj.id = id;
            }
        });

        symbolRegistryBuilder.set(new IForgeRegistry.DummyFactory<Symbol>() {
            @Override
            public Symbol createDummy(ResourceLocation key) {
                return new Symbol.DummySymbol(key.toString());
            }
        });

        symbolRegistryBuilder.set(new IForgeRegistry.MissingFactory<Symbol>() {
            @Override
            public Symbol createMissing(ResourceLocation key, boolean isNetwork) {
                return new Symbol.DummySymbol(key.toString());
            }
        });

        symbolRegistryBuilder.setType(Symbol.class);

        symbolRegistryBuilder.allowModification();
        symbolRegistryBuilder.setName(SYMBOLS_RL);
        symbolRegistryBuilder.create();

    }


}
