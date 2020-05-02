package com.latenighters.runicarcana.common.symbols.backend;


import com.latenighters.runicarcana.common.arcana.ArcanaType;
import com.latenighters.runicarcana.common.symbols.*;
import net.minecraftforge.event.RegistryEvent;

import static com.latenighters.runicarcana.common.symbols.backend.DataType.registerDataTypes;
import static com.latenighters.runicarcana.common.symbols.backend.FunctionalTypeRegister.registerBaseProviders;

public class SymbolRegistration {

//    private static final DeferredRegister<Symbol> SYMBOLS = new DeferredRegister<>(SymbolRegistryHandler.SYMBOLS, MODID);

    public static void registerSymbols(final RegistryEvent.Register<Symbol> evt)
    {
        if(!evt.getRegistry().getRegistrySuperType().equals(Symbol.class))
            return;

        evt.getRegistry().register(new DebugSymbol());
        evt.getRegistry().register(new ExpulsionSymbol());
        evt.getRegistry().register(new InsertionSymbol());
        evt.getRegistry().register(new RedstoneSymbol());
        evt.getRegistry().register(new BooleanLogicSymbol());
        evt.getRegistry().register(new DetectSymbol());

        //TODO move this out of here...
        FunctionalObjects.putObject("DrawnSymbol",DrawnSymbol.class);
        registerDataTypes();
        registerBaseProviders();
        ArcanaType.registerArcanaTypes();

        SymbolTextures.consolidateTextures(evt);
    }

//    public static void init() {
//        SYMBOLS.register(FMLJavaModLoadingContext.get().getModEventBus());
//    }

//    public static final RegistryObject<Symbol> DEBUG = SYMBOLS.register("symbol_debug", DebugSymbol::new);

}
