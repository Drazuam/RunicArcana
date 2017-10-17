package com.drazuam.runicarcana.common.enchantment.Symbols;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.common.RunicArcana;
import com.drazuam.runicarcana.api.enchantment.ModDust;
import com.drazuam.runicarcana.common.enchantment.ScriptExecutor;
import com.drazuam.runicarcana.api.enchantment.Signals.Signal;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import com.drazuam.runicarcana.reference.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by Joel on 2/20/2017.
 */
public class DustSymbolMath extends DefaultDustSymbol {


    public static final String MODEL_LOCATION = Reference.MODEL_LOCATION + "dustMath";
    public static final String TEXTURE_LOCATION = Reference.TEXTURE_LOCATION + "dustMath.png";
    public static final String DEFAULT_NAME = "dustMath";
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(RunicArcana.MOD_ID, TEXTURE_LOCATION);

    public DustSymbolMath(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, ModDust.mathSymbol.dustType);
        addSignals();

    }

    public DustSymbolMath()
    {
        super(0,0,0,null,ModDust.mathSymbol.dustType);
        addSignals();

    }

    public DustSymbolMath(short newDustType) {
        super(newDustType);
        addSignals();
    }

    private void addSignals()
    {
        addSignal(new Signal(this, Signal.SignalType.NUMBER, Signal.SigFlow.IN, "Number A",  null,0));
        addSignal(new Signal(this, Signal.SignalType.NUMBER, Signal.SigFlow.IN, "Number B",  null,1));
        addSignal(new Signal(this, Signal.SignalType.NUMBER, Signal.SigFlow.OUT, "Add",         DustSymbolMath::addition,2));
        addSignal(new Signal(this, Signal.SignalType.NUMBER, Signal.SigFlow.OUT, "Subtract",    DustSymbolMath::subtraction,3));
        addSignal(new Signal(this, Signal.SignalType.NUMBER, Signal.SigFlow.OUT, "Multiply",    DustSymbolMath::multiplication,4));
        addSignal(new Signal(this, Signal.SignalType.NUMBER, Signal.SigFlow.OUT, "Divide",      DustSymbolMath::division,5));
        addSignal(new Signal(this, Signal.SignalType.NUMBER, Signal.SigFlow.OUT, "Remainder",   DustSymbolMath::modulus,6));
    }


    public static Object addition(Object... args)
    {
        ScriptExecutor executor = (ScriptExecutor)(args[0]);
        Double A,B;
        try {
            A = ModDust.parseNumber(executor.resolveInput((short) 0));
            B = ModDust.parseNumber(executor.resolveInput((short) 1));
        }
        catch(NullPointerException e)
        {
            return null;
        }
        if(A==null||B==null)return null;

        return A+B;
    }

    public static Object multiplication(Object... args)
    {
        ScriptExecutor executor = (ScriptExecutor)(args[0]);
        Double A,B;
        try {
            A = ModDust.parseNumber(executor.resolveInput((short) 0));
            B = ModDust.parseNumber(executor.resolveInput((short) 1));
        }
        catch(NullPointerException e)
        {
            return null;
        }
        if(A==null||B==null)return null;

        return A*B;
    }

    public static Object subtraction(Object... args)
    {
        ScriptExecutor executor = (ScriptExecutor)(args[0]);
        Double A,B;
        try {
            A = ModDust.parseNumber(executor.resolveInput((short) 0));
            B = ModDust.parseNumber(executor.resolveInput((short) 1));
        }
        catch(NullPointerException e)
        {
            return null;
        }
        if(A==null||B==null)return null;

        return A-B;
    }

    public static Object division(Object... args)
    {
        ScriptExecutor executor = (ScriptExecutor)(args[0]);
        Double A,B;
        try {
            A = ModDust.parseNumber(executor.resolveInput((short) 0));
            B = ModDust.parseNumber(executor.resolveInput((short) 1));
        }
        catch(NullPointerException e)
        {
            return null;
        }
        if(A==null||B==null)return null;

        return A/B;
    }


    public static Object modulus(Object... args)
    {
        ScriptExecutor executor = (ScriptExecutor)(args[0]);
        Double A,B;
        try {
            A = ModDust.parseNumber(executor.resolveInput((short) 0));
            B = ModDust.parseNumber(executor.resolveInput((short) 1));
        }
        catch(NullPointerException e)
        {
            return null;
        }
        if(A==null||B==null)return null;

        return (double)(A.intValue()%B.intValue());
    }

    public static Object sneakRightClick(Object... args)
    {
        ScriptExecutor executor = (ScriptExecutor)(args[0]);
        executor.resolveOutput((short)1,true);
        return true;
    }

    public static EntityPlayer playerEntity(Object... args)
    {
        ScriptExecutor executor = (ScriptExecutor)(args[0]);
        return executor.player;
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

