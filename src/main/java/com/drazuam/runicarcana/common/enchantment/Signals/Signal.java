package com.drazuam.runicarcana.common.enchantment.Signals;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Created by Joel on 2/23/2017.
 */
public class Signal implements ISignal,Serializable {


    public final DefaultDustSymbol parent;
    public final SignalType type;
    public final SigFlow flow;
    public final String name;
    public final int ID;
    public final transient Function<Object[],Object> method;

    public Signal(DefaultDustSymbol newParent, SignalType newSigType, SigFlow newSigFlow, String newName, Function<Object[],Object> newMethod, int newID)
    {
        parent = newParent;
        type = newSigType;
        flow = newSigFlow;
        name = newName;
        ID = newID;
        method = newMethod;
    }


    public enum SignalType
    {
        CONTROL(0xFFFFFF),
        BLOCKPOS(0x0000FF),
        VECTOR(0x00FF00),
        NUMBER(0x00FF00),
        ENITITY(0xFF00FF),
        STRING(0xFF8C00),
        ANGLE(0xFFD500);

        public final int color;

        SignalType(int newColor)
        {
            color = newColor;
        }
    }


    public enum SigFlow
    {
        IN,
        OUT;
    }

    @Override
    public Object doFunction(Object... args)throws NullPointerException {

        Object[] params = args;
        if(method==null)return null;
        try {
            Object got = method.apply(params);
            if(got==null)throw new NullPointerException();
            return got;
        }catch(NullPointerException e)
        {
            //ignore
        }
        return null;

    }
}
