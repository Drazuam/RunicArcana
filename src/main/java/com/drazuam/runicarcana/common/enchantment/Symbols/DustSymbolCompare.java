package com.drazuam.runicarcana.common.enchantment.Symbols;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.common.RunicArcana;
import com.drazuam.runicarcana.api.enchantment.ModDust;
import com.drazuam.runicarcana.common.enchantment.ScriptExecutor;
import com.drazuam.runicarcana.common.enchantment.Signals.Signal;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by Joel on 2/20/2017.
 */
public class DustSymbolCompare extends DefaultDustSymbol {

    public static final String MODEL_LOCATION = "block/dust/"+"dustCompare";
    public static final String TEXTURE_LOCATION = "textures/block/dustCompare.png";
    public static final String DEFAULT_NAME = "dustCompare";
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(RunicArcana.MODID, TEXTURE_LOCATION);


    public DustSymbolCompare(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, ModDust.compareSymbol.dustType);
        addSignals();

    }

    public DustSymbolCompare()
    {
        super(0,0,0,null,ModDust.compareSymbol.dustType);
        addSignals();

    }

    public DustSymbolCompare(short newDustType) {
        super(newDustType);
        addSignals();
    }

    private void addSignals()
    {
        addSignal(new Signal(this, Signal.SignalType.NUMBER, Signal.SigFlow.IN, "Number A",  null,0));
        addSignal(new Signal(this, Signal.SignalType.NUMBER, Signal.SigFlow.IN, "Number B",  null,1));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "Less", DustSymbolCompare::lessThan ,2));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "Less or Equal", DustSymbolCompare::lessThanOrEqual ,3));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "Equal", DustSymbolCompare::Equal ,4));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "Greater", DustSymbolCompare::greaterThan ,5));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "Greater or Equal", DustSymbolCompare::greaterThanOrEqual ,6));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.IN, "Compare", DustSymbolCompare::compare ,7));

    }


    public static Object compare(Object... args)
    {
        ScriptExecutor executer = (ScriptExecutor)(args[0]);
        Double A,B;
        try {
            A = ModDust.parseNumber(executer.resolveInput((short) 0));
            B = ModDust.parseNumber(executer.resolveInput((short) 1));
        }
        catch(NullPointerException e)
        {
            return null;
        }
        if(A==null||B==null)return false;

        if(A>B)
            executer.resolveOutput((short)5,true);

        if((A.intValue())==(B.intValue()))
            executer.resolveOutput((short)4,true);
        if(A<B)
            executer.resolveOutput((short)2,true);

        return true;
    }

    public static Object lessThan(Object... args)
    {
        ScriptExecutor executer = (ScriptExecutor)(args[0]);
        Double A,B;
        try {
            A = ModDust.parseNumber(executer.resolveInput((short) 0));
            B = ModDust.parseNumber(executer.resolveInput((short) 1));
        }
        catch(NullPointerException e)
        {
            return null;
        }
        if(A==null||B==null)return null;

        return (Boolean)(A < B);
    }

    public static Object lessThanOrEqual(Object... args) {
        ScriptExecutor executer = (ScriptExecutor)(args[0]);
        Double A,B;
        try {
            A = ModDust.parseNumber(executer.resolveInput((short) 0));
            B = ModDust.parseNumber(executer.resolveInput((short) 1));
        }
        catch(NullPointerException e)
        {
            return null;
        }
        if(A==null||B==null)return null;

        return (Boolean) (A.intValue() <= B.intValue());
    }

    public static Object Equal(Object... args) {
        ScriptExecutor executer = (ScriptExecutor)(args[0]);
        Double A,B;
        try {
            A = ModDust.parseNumber(executer.resolveInput((short) 0));
            B = ModDust.parseNumber(executer.resolveInput((short) 1));
        }
        catch(NullPointerException e)
        {
            return null;
        }
        if(A==null||B==null)return null;

        return (Boolean) (A.intValue() == B.intValue());
    }

    public static Object greaterThan(Object... args) {
        ScriptExecutor executer = (ScriptExecutor)(args[0]);
        Double A,B;
        try {
            A = ModDust.parseNumber(executer.resolveInput((short) 0));
            B = ModDust.parseNumber(executer.resolveInput((short) 1));
        }
        catch(NullPointerException e)
        {
            return null;
        }
        if(A==null||B==null)return null;

        return (Boolean) (A > B);
    }

    public static Object greaterThanOrEqual(Object... args) {
        ScriptExecutor executer = (ScriptExecutor)(args[0]);
        Double A,B;
        try {
            A = ModDust.parseNumber(executer.resolveInput((short) 0));
            B = ModDust.parseNumber(executer.resolveInput((short) 1));
        }
        catch(NullPointerException e)
        {
            return null;
        }
        if(A==null||B==null)return null;

        return (Boolean) (A.intValue() >= B.intValue());
    }

    @Override
    public String getDefaultName() {
        return DEFAULT_NAME;
    }

    @Override
    public String getTexture() {
        return TEXTURE_LOCATION;
    }

    @Override
    public String getModelLocation() {
        return MODEL_LOCATION;
    }

    @Override
    public ITextComponent getDisplayName(String name) {
        return name==null ? new TextComponentTranslation("dust."+DEFAULT_NAME+".name") : new TextComponentTranslation("dust."+name+".name");
    }

}

