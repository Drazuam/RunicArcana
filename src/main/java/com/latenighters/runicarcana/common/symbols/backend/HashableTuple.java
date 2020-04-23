package com.latenighters.runicarcana.common.symbols.backend;

import net.minecraft.util.Tuple;

public class HashableTuple<A,B> extends Tuple<A,B> {

    private static int globalIndex = 0;
    private final int index;

    public HashableTuple(A aIn, B bIn) {
        super(aIn, bIn);
        index = globalIndex++;
    }

    public HashableTuple(A aIn, B bIn, int index) {
        super(aIn, bIn);
        this.index = index;
    }

    @Override
    public A getA() {
        return super.getA();
    }

    @Override
    public B getB() {
        return super.getB();
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new HashableTuple<>(getA(),getB(),index);
    }

}
