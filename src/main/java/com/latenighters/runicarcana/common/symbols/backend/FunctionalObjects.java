package com.latenighters.runicarcana.common.symbols.backend;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class FunctionalObjects {
    private static Map<String, Class<? extends IFunctionalObject>> objects = new HashMap<>();

    public static void putObject(String name, Class<? extends IFunctionalObject> clazz){
        objects.put(name, clazz);
    }

    public static void getObject(String name)
    {
        objects.get(name);
    }

    public static IFunctionalObject getNewObject(String name)
    {
        IFunctionalObject retval = null;
        try {
            retval = objects.get(name).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return retval;
    }

    public static String getName(Class<? extends IFunctionalObject> clazz)
    {
        AtomicReference<String> retval = new AtomicReference<>(null);
        objects.forEach((key,value)->{
            if (value.isAssignableFrom(clazz))
                retval.set(key);
        });
        return retval.get();
    }
}
