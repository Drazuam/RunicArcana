package com.latenighters.runicarcana.common.symbols.backend;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import java.awt.*;
import java.util.ArrayList;

public class DataType {

    public static final DataType ENTITY = new DataType("Entity", Color.orange, Entity.class);

    public final String name;
    public final Color color;
    public final Class<?> type;

    public DataType(String name, Color color, Class<?> type) {
        this.name = name;
        this.color = color;
        this.type = type;
    }


}
