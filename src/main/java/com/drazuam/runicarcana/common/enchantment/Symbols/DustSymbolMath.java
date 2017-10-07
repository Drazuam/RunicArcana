package com.drazuam.runicarcana.common.enchantment.Symbols;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.common.RunicArcana;
import com.drazuam.runicarcana.api.enchantment.ModDust;
import com.drazuam.runicarcana.common.enchantment.ScriptExecuter;
import com.drazuam.runicarcana.common.enchantment.Signals.Signal;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by Joel on 2/20/2017.
 */
public class DustSymbolMath extends DefaultDustSymbol {


    public static final String MODEL_LOCATION = "block/dust/"+"dustMath";
    public static final String TEXTURE_LOCATION = "textures/block/dustMath.png";
    public static final String DEFAULT_NAME = "dustMath";
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(RunicArcana.MODID, TEXTURE_LOCATION);

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
        ScriptExecuter executer = (ScriptExecuter)(args[0]);
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

        return A+B;
    }

    public static Object multiplication(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)(args[0]);
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

        return A*B;
    }

    public static Object subtraction(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)(args[0]);
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

        return A-B;
    }

    public static Object division(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)(args[0]);
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

        return A/B;
    }


    public static Object modulus(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)(args[0]);
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

        return (double)(A.intValue()%B.intValue());
    }

    public static Object sneakRightClick(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)(args[0]);
        executer.resolveOutput((short)1,true);
        return true;
    }

    public static EntityPlayer playerEntity(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)(args[0]);
        return executer.player;
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

