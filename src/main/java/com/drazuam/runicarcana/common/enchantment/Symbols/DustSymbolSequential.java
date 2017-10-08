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
 */

public class DustSymbolSequential extends DefaultDustSymbol
{
    public static final String MODEL_LOCATION = "block/dust/"+"dustSequential";
    public static final String TEXTURE_LOCATION = "textures/block/dustSequential.png";
    public static final String DEFAULT_NAME = "dustSequential";
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(RunicArcana.MODID, TEXTURE_LOCATION);

    public DustSymbolSequential(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, ModDust.sequentialSymbol.dustType);
        //set up signals
        addSignals();
    }

    public DustSymbolSequential()
    {
        super(0,0,0,null,ModDust.sequentialSymbol.dustType);
        //set up signals
        addSignals();
    }

    public DustSymbolSequential(short newDustType) {
        super(newDustType);
        addSignals();
    }

    private void addSignals()
    {
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.IN, "Sequence", DustSymbolSequential::Sequence, 0));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "First", null, 1));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "Second", null, 2));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "Third", null, 3));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "Done", null, 4));
    }

    public static Object Sequence(Object... args)
    {
        ScriptExecutor executor = (ScriptExecutor)args[0];

        executor.resolveInput((short)1);
        executor.resolveInput((short)2);
        executor.resolveInput((short)3);

        executor.resolveOutput((short)4, true);

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
