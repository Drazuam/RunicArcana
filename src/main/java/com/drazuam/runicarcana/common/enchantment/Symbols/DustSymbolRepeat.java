package com.drazuam.runicarcana.common.enchantment.Symbols;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.common.RunicArcana;
import com.drazuam.runicarcana.api.enchantment.ModDust;
import com.drazuam.runicarcana.common.enchantment.ScriptExecutor;
import com.drazuam.runicarcana.api.enchantment.Signals.Signal;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import com.drazuam.runicarcana.reference.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * created by Matt on 10/7/2017
 */

public class DustSymbolRepeat extends DefaultDustSymbol
{
    public static final String MODEL_LOCATION = Reference.Model_Location + "dustRepeat";
    public static final String TEXTURE_LOCATION = Reference.Texture_Location + "dustRepeat.png";
    public static final String DEFAULT_NAME = "dustRepeat";
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(RunicArcana.MOD_ID, TEXTURE_LOCATION);

    public DustSymbolRepeat(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, ModDust.repeatSymbol.dustType);
        //set up signals
        addSignals();
    }

    public DustSymbolRepeat()
    {
        super(0,0,0,null,ModDust.repeatSymbol.dustType);
        //set up signals
        addSignals();
    }

    public DustSymbolRepeat(short newDustType) {
        super(newDustType);
        addSignals();
    }

    private void addSignals()
    {
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.IN, "Repeat", DustSymbolRepeat::Repeat, 0));
        addSignal(new Signal(this, Signal.SignalType.NUMBER, Signal.SigFlow.IN, "Repetitions", null, 1));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "LoopOut", null,2));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.IN, "LoopIn", DustSymbolRepeat::LoopIn, 3));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "Done", null, 4));
        addSignal(new Signal(this, Signal.SignalType.NUMBER, Signal.SigFlow.OUT, "Cycle", DustSymbolRepeat::getCycles, 5));
    }

    public static Object Repeat(Object... args)
    {
        ScriptExecutor executor = (ScriptExecutor)args[0];
        Integer repetitions = 0;

        executor.setVariable(repetitions.toString());


        executor.resolveOutput((short)2, true);

        return true;
    }


    public static Object getCycles(Object... args) {
        ScriptExecutor executor = (ScriptExecutor) args[0];
        return Integer.parseInt(executor.getVariable());
    }

    public static Object LoopIn(Object... args){
        ScriptExecutor executor = (ScriptExecutor) args[0];
        String var = executor.getVariable();
        int cycles = Integer.parseInt(var);
        cycles++;
        if(cycles < (int)(double)((Double)executor.resolveInput((short)1)))
        {
            executor.setVariable(""+cycles);

            executor.resolveOutput((short)2, true);
        }
        else
            executor.resolveOutput((short)4, true);


        return true;
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
