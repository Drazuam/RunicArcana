package com.latenighters.runicarcana.common.symbols.backend;

import net.minecraft.util.Tuple;
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
    public List<Tuple<String, DataType>> getRequiredInputs() {
        return new ArrayList<>();
    }

    @Override
    public Object executeInWorld(IFunctionalObject object, Chunk chunk, List<Tuple<String, Object>> args) {
        return null;
    }

    @Override
    public DataType getOutputType() {
        return null;
    }

    @Override
    public List<Tuple<String, DataType>> getTriggers() {
        return new ArrayList<>();
    }
}
