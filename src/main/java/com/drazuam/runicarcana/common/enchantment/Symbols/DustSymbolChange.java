package com.drazuam.runicarcana.common.enchantment.Symbols;

import com.drazuam.runicarcana.common.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.common.enchantment.DustModelHandler;
import com.drazuam.runicarcana.common.enchantment.ModDust;
import com.drazuam.runicarcana.common.enchantment.ScriptExecuter;
import com.drazuam.runicarcana.common.enchantment.Signals.Signal;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceResult;

/**
 * Created by Joel on 2/20/2017.
 */
public class DustSymbolChange extends DefaultDustSymbol {


    public static final DustModelHandler.DustTypes curDustType = DustModelHandler.DustTypes.CHANGE;

    public DustSymbolChange(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, curDustType);

    }

    public DustSymbolChange()
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
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.IN, "Start in", DustSymbolChange::ChangeBlockToAir ,0));
        addSignal(new Signal(this, Signal.SignalType.ENITITY, Signal.SigFlow.IN, "Entity in",null,1));
        addSignal(new Signal(this, Signal.SignalType.NUMBER,  Signal.SigFlow.IN, "Length in",null,2));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "Done",null,3));

    }

    public static Object ChangeBlockToAir(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)args[0];

        if(!executer.player.worldObj.isRemote) {
            RayTraceResult ray = ((Entity)executer.resolveInput((short)1)).rayTrace(5,1.0F);
            Double length = ((Double)executer.resolveInput((short)2));
            if(length==null)length=1.0D;
            if(ray.typeOfHit== RayTraceResult.Type.BLOCK) {
                for(int i=0; i<length; i++)
                executer.player.worldObj.setBlockToAir(ray.getBlockPos().up(i));
            }

        }
        executer.addProcesses(5);
        executer.resolveOutput((short)3,true);

        return null;
    }



}

