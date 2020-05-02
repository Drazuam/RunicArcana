package com.latenighters.runicarcana.common.arcana;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class ArcanaType {

    public static ArcanaType AIR   = new ArcanaType("air");
    public static ArcanaType EARTH = new ArcanaType("earth");
    public static ArcanaType WATER = new ArcanaType("water");
    public static ArcanaType FIRE  = new ArcanaType("fire");

    private static HashMap<String, ArcanaType> arcanaTypes = new HashMap<>();

    public final String name;

    public ArcanaType(String name) {
        this.name = name;
    }

    public static void registerType(ArcanaType arcanaType)
    {
        arcanaTypes.put(arcanaType.name, arcanaType);
    }

    public static void registerArcanaTypes()
    {
        registerType(AIR);
        registerType(EARTH);
        registerType(WATER);
        registerType(FIRE);
    }

    public static ArcanaType getArcanaType(String name)
    {
        AtomicReference<ArcanaType> retval = new AtomicReference<>();
        retval.set(AIR);
        arcanaTypes.forEach((tname,type)->{
            if(tname.equals(name))
                retval.set(arcanaTypes.get(tname));
        });

        return retval.get();
    }
}
