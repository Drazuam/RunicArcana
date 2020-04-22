package com.latenighters.runicarcana.common.symbols.backend;

import javax.xml.ws.Provider;
import java.util.HashMap;
import java.util.function.Supplier;

public class FunctionalTypeRegister {
    static HashMap<String, Supplier<IFunctionalObject>> functionalObjectProviders = new HashMap<>();

    public static void registerFunctionalObjectProvider(String name, Supplier<IFunctionalObject> provider)
    {
        functionalObjectProviders.put(name,provider);
    }

    public static IFunctionalObject getFunctionalObject(String name)
    {
        return functionalObjectProviders.get(name).get();
    }

    public static void registerBaseProviders()
    {
        registerFunctionalObjectProvider("DrawnSymbol",DrawnSymbol::new);
    }
}
