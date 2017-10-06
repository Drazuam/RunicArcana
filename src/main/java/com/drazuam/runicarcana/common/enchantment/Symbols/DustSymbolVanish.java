package com.drazuam.runicarcana.common.enchantment.Symbols;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.common.enchantment.DustModelHandler;
import com.drazuam.runicarcana.common.enchantment.ModDust;
import com.drazuam.runicarcana.common.enchantment.ScriptExecuter;
import com.drazuam.runicarcana.common.enchantment.Signals.Signal;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Joel on 2/20/2017.
 */
public class DustSymbolVanish extends DefaultDustSymbol {


    public static final DustModelHandler.DustTypes curDustType = DustModelHandler.DustTypes.BREAK;

    public DustSymbolVanish(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, curDustType);

    }

    public DustSymbolVanish()
    {
        super(0,0,0,null,curDustType);
        addMethods();

    }

    public static final short dustID = ModDust.getNextDustID();
    @Override
    public short getDustID() {
        return dustID;
    }

    private void addMethods()
    {
        addSignal(new Signal(this, Signal.SignalType.CONTROL,  Signal.SigFlow.IN, "Vanish", DustSymbolVanish::VanishBlock ,0));
        addSignal(new Signal(this, Signal.SignalType.BLOCKPOS, Signal.SigFlow.IN, "Block Position",null,1));
        addSignal(new Signal(this, Signal.SignalType.NUMBER, Signal.SigFlow.IN,   "Time",null,2));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "Done",null,3));

    }

    public static Object VanishBlock(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)args[0];
        BlockPos pos = ModDust.parseBlockPos(executer.resolveInput((short)1));
        Double time = ModDust.parseNumber(executer.resolveInput((short)2));
        if(!executer.player.worldObj.isRemote) {

        }
        executer.addProcesses(5);
        executer.resolveOutput((short)2,true);

        //TODO: seperate mana costs for breaking a block vs right clicking

        return null;
    }

}

