package com.latenighters.runicarcana.common.symbols;


import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;


import static com.latenighters.runicarcana.RunicArcana.MODID;

public class SymbolRegistration {

//    private static final DeferredRegister<Symbol> SYMBOLS = new DeferredRegister<>(SymbolRegistryHandler.SYMBOLS, MODID);

    public static void registerSymbols(final RegistryEvent.Register<Symbol> evt)
    {
        if(!evt.getRegistry().getRegistrySuperType().equals(Symbol.class))
            return;

        evt.getRegistry().register(new DebugSymbol());

        SymbolTextures.consolidateTextures(evt);
    }

//    public static void init() {
//        SYMBOLS.register(FMLJavaModLoadingContext.get().getModEventBus());
//    }

//    public static final RegistryObject<Symbol> DEBUG = SYMBOLS.register("symbol_debug", DebugSymbol::new);

}
