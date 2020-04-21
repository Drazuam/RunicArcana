package com.latenighters.runicarcana.common.symbols.backend;


import com.latenighters.runicarcana.common.symbols.DebugSymbol;
import com.latenighters.runicarcana.common.symbols.ExpulsionSymbol;
import com.latenighters.runicarcana.common.symbols.RedstoneSymbol;
import com.latenighters.runicarcana.common.symbols.SymbolTextures;
import net.minecraftforge.event.RegistryEvent;

import static com.latenighters.runicarcana.common.symbols.backend.DataType.registerDataTypes;

public class SymbolRegistration {

//    private static final DeferredRegister<Symbol> SYMBOLS = new DeferredRegister<>(SymbolRegistryHandler.SYMBOLS, MODID);

    public static void registerSymbols(final RegistryEvent.Register<Symbol> evt)
    {
        if(!evt.getRegistry().getRegistrySuperType().equals(Symbol.class))
            return;

        evt.getRegistry().register(new DebugSymbol());
        evt.getRegistry().register(new ExpulsionSymbol());
        evt.getRegistry().register(new RedstoneSymbol());

        //TODO move this out of here...
        FunctionalObjects.putObject("DrawnSymbol",DrawnSymbol.class);
        registerDataTypes();

        SymbolTextures.consolidateTextures(evt);
    }

//    public static void init() {
//        SYMBOLS.register(FMLJavaModLoadingContext.get().getModEventBus());
//    }

//    public static final RegistryObject<Symbol> DEBUG = SYMBOLS.register("symbol_debug", DebugSymbol::new);

}
