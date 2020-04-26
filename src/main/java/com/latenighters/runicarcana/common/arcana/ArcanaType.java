package com.latenighters.runicarcana.common.arcana;

public class ArcanaType {

    public static ArcanaType AIR   = new ArcanaType("air");
    public static ArcanaType EARTH = new ArcanaType("earth");
    public static ArcanaType WATER = new ArcanaType("water");
    public static ArcanaType FIRE  = new ArcanaType("fire");

    public final String name;

    public ArcanaType(String name) {
        this.name = name;
    }
}
