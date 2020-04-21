package com.latenighters.runicarcana.common.symbols.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
            objects.get(name).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return retval;
    }

    public static String getName(Class<? extends IFunctionalObject> clazz)
    {
        String retval = "";
        objects.forEach((key,value)->{
            if (retval.isEmpty() && value.equals(clazz))
                retval.concat(key);
        });
        if (retval.isEmpty())return null;
        return retval;
    }
}
