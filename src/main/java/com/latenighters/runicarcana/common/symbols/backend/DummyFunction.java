package com.latenighters.runicarcana.common.symbols.backend;

import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

public class DummyFunction implements IFunctional {

    private final String name;

    @Override
    public String getName() {
        return name;
    }

    public DummyFunction(String name) {
        this.name = name;
    }

    @Override
    public List<HashableTuple<String, DataType>> getRequiredInputs() {
        return new ArrayList<>();
    }

    @Override
    public String getOutputString(IFunctionalObject object, Chunk chunk, List<HashableTuple<String, Object>> args) {
        return "!!!not implemented!!!";
    }

    @Override
    public Object executeInWorld(IFunctionalObject object, Chunk chunk, List<HashableTuple<String, Object>> args) {
        return null;
    }

    @Override
    public DataType getOutputType() {
        return null;
    }

    @Override
    public List<HashableTuple<String, DataType>> getTriggers() {
        return new ArrayList<>();
    }
}
