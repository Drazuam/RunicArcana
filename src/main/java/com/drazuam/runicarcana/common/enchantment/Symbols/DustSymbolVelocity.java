package com.drazuam.runicarcana.common.enchantment.Symbols;

import com.drazuam.runicarcana.common.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.common.enchantment.DustModelHandler;
import com.drazuam.runicarcana.common.enchantment.ModDust;
import com.drazuam.runicarcana.common.enchantment.ScriptExecuter;
import com.drazuam.runicarcana.common.enchantment.Signals.Signal;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

/**
 * Created by Joel on 2/20/2017.
 */
public class DustSymbolVelocity extends DefaultDustSymbol {


    public static final DustModelHandler.DustTypes curDustType = DustModelHandler.DustTypes.MOVE;

    public DustSymbolVelocity(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, curDustType);
        //set up signals
        addSignals();
    }

    public DustSymbolVelocity()
    {
        super(0,0,0,null,curDustType);
        //set up signals
        addSignals();
    }

    public static final short dustID = ModDust.getNextDustID();
    @Override
    public short getDustID() {
        return dustID;
    }

    private void addSignals()
    {
        addSignal(new Signal(this, Signal.SignalType.VECTOR, Signal.SigFlow.IN, "Velocity",  null, 0));
        addSignal(new Signal(this, Signal.SignalType.NUMBER, Signal.SigFlow.IN, "Magnitude", null, 1));
        addSignal(new Signal(this, Signal.SignalType.ENITITY, Signal.SigFlow.IN, "Entity",    null, 2));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.IN, "Add Vel",  DustSymbolVelocity::addVelocity, 3));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "Done",    null, 4));

    }

    public static Object addVelocity(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)(args[0]);

        Vec3d velocity   = (Vec3d)executer.resolveInput((short)0);
        Double magnitude = (Double)executer.resolveInput((short)1);
        Entity entity    = (Entity)executer.resolveInput((short)2);

        if(velocity==null||entity==null)return false;

        if(magnitude!=null)
        {
            velocity = velocity.normalize();
            velocity = new Vec3d(velocity.xCoord*magnitude,velocity.yCoord*magnitude,velocity.zCoord*magnitude);
        }

        entity.addVelocity(velocity.xCoord, velocity.yCoord, velocity.zCoord);
        entity.velocityChanged = true;

        //keep fall damage from building up; make this usable as a jetpack or a parachute.  But only if velocity is positive upwards
        if(velocity.yCoord>0)
            entity.fallDistance=0;

        executer.addProcesses(8);
        //TODO: mana costs: should cost more if entity is a player.
        executer.resolveOutput((short)4,true);
        return true;

    }






}

