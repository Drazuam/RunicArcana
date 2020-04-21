package com.latenighters.runicarcana.common.symbols.backend;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;
import java.util.Map;

//defines a object with a collection of inputs and outputs
public interface IFunctionalObject extends INBTSerializable<CompoundNBT> {

    public List<IFunctional> getOutputs();
    public List<Tuple<String,DataType>>   getInputs();
    public List<IFunctional>       getFunctions();
    public List<Tuple<Tuple<String,DataType>,IFunctional>> getTriggers();
    public Map<Tuple<String,DataType>, Tuple<IFunctionalObject,IFunctional>> getInputLinks();

    //serialization with no downward linking
    public CompoundNBT basicSerializeNBT();

    //must be a unique string.  used for serialization
    public String getObjectType();

}
