package com.drazuam.omnimancy.common.enchantment.Symbols;

import com.drazuam.omnimancy.common.enchantment.DefaultDustSymbol;
import com.drazuam.omnimancy.common.enchantment.DustModelHandler;
import com.drazuam.omnimancy.common.enchantment.ModDust;
import com.drazuam.omnimancy.common.enchantment.ScriptExecuter;
import com.drazuam.omnimancy.common.enchantment.Signals.Signal;
import com.drazuam.omnimancy.common.tileentity.TileEntityChalkBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Joel on 2/20/2017.
 */
public class DustSymbolOr extends DefaultDustSymbol {


    public static final DustModelHandler.DustTypes curDustType = DustModelHandler.DustTypes.COMBINE;


    public DustSymbolOr(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, curDustType);
        addSignals();

    }

    public DustSymbolOr()
    {
        super(0,0,0,null,curDustType);
        addSignals();

    }

    public static final short dustID = ModDust.getNextDustID();
    @Override
    public short getDustID() {
        return dustID;
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

}

