package com.latenighters.runicarcana.common.symbols.backend;

import net.minecraft.util.Tuple;

public class HashableTuple<A,B> extends Tuple<A,B> {

    public HashableTuple(A aIn, B bIn) {
        super(aIn, bIn);
    }

    public HashableTuple(A aIn, B bIn, int index) {
        super(aIn, bIn);
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
        return getA().hashCode() ^ getB().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Tuple)
            return ((Tuple) obj).getA().equals(this.getA()) && ((Tuple) obj).getB().equals(this.getB());
        return false;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new HashableTuple<>(getA(),getB());
    }

}
