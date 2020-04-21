package com.latenighters.runicarcana.common.symbols.backend;

import net.minecraft.util.Tuple;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//defines a single function to execute
public interface IFunctional {
    public String getName();
    public List<Tuple<String,DataType>> getRequiredInputs();
    public Object executeInWorld(IFunctionalObject object, Chunk chunk, List<Tuple<String, Object>> args);
    public DataType getOutputType();
    public List<Tuple<String,DataType>> getTriggers();
}
