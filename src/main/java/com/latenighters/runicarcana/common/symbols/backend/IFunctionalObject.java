package com.latenighters.runicarcana.common.symbols.backend;

import net.minecraft.util.Tuple;

import java.util.List;
import java.util.Map;

//defines a object with a collection of inputs and outputs
public interface IFunctionalObject {

    public List<IFunctional> getOutputs();
    public List<Tuple<String,DataType>>   getInputs();
    public List<IFunctional>       getFunctions();
    public List<Tuple<Tuple<String,DataType>,IFunctional>> getTriggers();
    public Map<Tuple<String,DataType>, Tuple<IFunctionalObject,IFunctional>> getInputLinks();

}
