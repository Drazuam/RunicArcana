package com.drazuam.runicarcana.common.enchantment.Symbols;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.common.RunicArcana;
import com.drazuam.runicarcana.api.enchantment.ModDust;
import com.drazuam.runicarcana.common.enchantment.ScriptExecutor;
import com.drazuam.runicarcana.api.enchantment.Signals.Signal;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * created by Matt on 10/7/2017
 * (WIP)
 */

public class DustSymbolRepeat extends DefaultDustSymbol
{
    public static final String MODEL_LOCATION = "block/dust/"+"dustRepeat";
    public static final String TEXTURE_LOCATION = "textures/block/dustRepeat.png";
    public static final String DEFAULT_NAME = "dustRepeat";
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(RunicArcana.MODID, TEXTURE_LOCATION);

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
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "LoopIn", null,2));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.IN, "LoopOut", null, 3));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "Done", null, 4));
    }

    public static Object Repeat(Object... args)
    {
        ScriptExecutor executor = (ScriptExecutor)args[0];
        Double repetitions = (Double)executor.resolveInput((short)1);

        for (Double i = 0.0 ;i < Math.abs(repetitions); i++)
        {
            executor.resolveInput((short)2);
            System.out.println("Repeat: " + i);

            executor.resolveOutput((short)3, true);


        }

        executor.resolveOutput((short)3, true);

        return null;
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
