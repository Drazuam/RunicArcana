package com.latenighters.runicarcana.common.symbols.backend;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataType {

    public static final DataType ENTITY = new DataType("Entity", Color.orange, Entity.class);
    public static final DataType BOOLEAN = new DataType("Boolean", Color.white, Boolean.class);
    public static final DataType BLOCK_FACE = new DataType("Block Face", Color.cyan, SpecificBlockFace.class);

    public final String name;
    public final Color color;
    public final Class<?> type;
    private static final Map<String, DataType> dataTypes = new HashMap<>();

    public DataType(String name, Color color, Class<?> type) {
        this.name = name;
        this.color = color;
        this.type = type;
    }

    public static void registerDataTypes()
    {
        registerDataType(ENTITY);
        registerDataType(BOOLEAN);
        registerDataType(BLOCK_FACE);
    }

    public String getShortName(){
        return String.valueOf(name.charAt(0));
    }

    public static DataType getDataType(String name)
    {
        return dataTypes.getOrDefault(name, null);
    }

    public static void registerDataType(DataType dataType)
    {
        dataTypes.put(dataType.name, dataType);
    }

    public static class SpecificBlockFace{
        public final BlockPos blockPos;
        public final Direction blockFace;

        public SpecificBlockFace(BlockPos blockPos, Direction blockFace) {
            this.blockPos = blockPos;
            this.blockFace = blockFace;
        }
    }


}
