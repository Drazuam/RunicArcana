package com.drazuam.runicarcana.common.enchantment.Symbols;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.common.RunicArcana;
import com.drazuam.runicarcana.api.enchantment.ModDust;
import com.drazuam.runicarcana.common.enchantment.ScriptExecuter;
import com.drazuam.runicarcana.common.enchantment.Signals.Signal;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by Joel on 2/20/2017.
 */
public class DustSymbolOr extends DefaultDustSymbol {

    public static final String MODEL_LOCATION = "block/dust/"+"dustCombine";
    public static final String TEXTURE_LOCATION = "textures/block/dustCombine.png";
    public static final String DEFAULT_NAME = "dustCombine";
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(RunicArcana.MODID, TEXTURE_LOCATION);

    public DustSymbolOr(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, ModDust.orSymbol.dustType);
        addSignals();

    }

    public DustSymbolOr()
    {
        super(0,0,0,null,ModDust.orSymbol.dustType);
        addSignals();

    }

    public DustSymbolOr(short newDustType) {
        super(newDustType);
        addSignals();
    }

    private void addSignals()
    {
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.IN, "Control A",  DustSymbolOr::routeOutput,0));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.IN, "Control B",  DustSymbolOr::routeOutput,1));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.IN, "Control C",  DustSymbolOr::routeOutput,2));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.IN, "Control D",  DustSymbolOr::routeOutput,3));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "Combined",   null, 4));
    }


    public static Object routeOutput(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)(args[0]);
        executer.resolveOutput((short)4,true);
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

