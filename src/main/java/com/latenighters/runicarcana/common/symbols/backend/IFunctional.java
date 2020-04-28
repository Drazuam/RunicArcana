package com.latenighters.runicarcana.common.symbols.backend;

import net.minecraft.world.chunk.Chunk;

import java.util.List;

//defines a single function to execute
public interface IFunctional {
    public String getName();
    public List<HashableTuple<String,DataType>> getRequiredInputs();
    public String getOutputString(IFunctionalObject object, Chunk chunk, List<HashableTuple<String, Object>> args);
    public Object executeInWorld(IFunctionalObject object, Chunk chunk, List<HashableTuple<String, Object>> args);
    public DataType getOutputType();
    public List<HashableTuple<String,DataType>> getTriggers();
}
