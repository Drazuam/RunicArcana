package com.drazuam.runicarcana.common.enchantment.Symbols;

import com.drazuam.runicarcana.common.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.common.enchantment.DustModelHandler;
import com.drazuam.runicarcana.common.enchantment.ModDust;
import com.drazuam.runicarcana.common.enchantment.ScriptExecuter;
import com.drazuam.runicarcana.common.enchantment.Signals.Signal;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import net.minecraft.item.ItemStack;

/**
 * Created by Joel on 2/20/2017.
 */
public class DustSymbolStone extends DefaultDustSymbol {


    public static final DustModelHandler.DustTypes curDustType = DustModelHandler.DustTypes.CONSTANT;

    public DustSymbolStone(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, curDustType);

        addSignals();



    }

    public DustSymbolStone()
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
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.IN, "Start", DustSymbolStone::startVeinMine,0));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT,"Done" , null,1));
        addSignal(new Signal(this, Signal.SignalType.NUMBER, Signal.SigFlow.IN, "Blocks per Tick", DustSymbolStone::startVeinMine,0));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.IN, "Radius", DustSymbolStone::startVeinMine,0));
    }


    public static Object startVeinMine(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)(args[0]);
        return executer.getVariable();
    }

    @Override
    public boolean willAccept(ItemStack stack) {
        return false;
    }

























}

