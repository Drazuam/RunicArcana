package com.drazuam.omnimancy.common.enchantment.Symbols;

import com.drazuam.omnimancy.common.enchantment.DefaultDustSymbol;
import com.drazuam.omnimancy.common.enchantment.DustModelHandler;
import com.drazuam.omnimancy.common.enchantment.ModDust;
import com.drazuam.omnimancy.common.enchantment.ScriptExecuter;
import com.drazuam.omnimancy.common.enchantment.Signals.Signal;
import com.drazuam.omnimancy.common.tileentity.TileEntityChalkBase;
import com.sun.jna.platform.win32.WinBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import static java.lang.Math.sqrt;

/**
 * Created by Joel on 2/20/2017.
 */
public class DustSymbolProjection extends DefaultDustSymbol {


    public static final DustModelHandler.DustTypes curDustType = DustModelHandler.DustTypes.PROJECTION;

    public DustSymbolProjection(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, curDustType);
        //set up signals
        addSignals();
    }

    public DustSymbolProjection()
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
        addSignal(new Signal(this, Signal.SignalType.BLOCKPOS, Signal.SigFlow.IN,  "Reference Pos",null,0));
        addSignal(new Signal(this, Signal.SignalType.ANGLE, Signal.SigFlow.IN,    "Reference Angle",null,1));
        addSignal(new Signal(this, Signal.SignalType.NUMBER, Signal.SigFlow.IN,    "Length",null,2));
        addSignal(new Signal(this, Signal.SignalType.BLOCKPOS, Signal.SigFlow.OUT, "Up"  ,DustSymbolProjection::blockUp,3));
        addSignal(new Signal(this, Signal.SignalType.BLOCKPOS, Signal.SigFlow.OUT, "Down",DustSymbolProjection::blockDown,4));
        addSignal(new Signal(this, Signal.SignalType.BLOCKPOS, Signal.SigFlow.OUT, "Left",DustSymbolProjection::blockLeft,5));
        addSignal(new Signal(this, Signal.SignalType.BLOCKPOS, Signal.SigFlow.OUT, "Right",DustSymbolProjection::blockRight,6));
        addSignal(new Signal(this, Signal.SignalType.BLOCKPOS, Signal.SigFlow.OUT, "Forward",DustSymbolProjection::blockForward,7));
        addSignal(new Signal(this, Signal.SignalType.BLOCKPOS, Signal.SigFlow.OUT, "Back",DustSymbolProjection::blockBack,8));
    }


    public static Object blockUp(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)args[0];
        BlockPos refBlock = (BlockPos)executer.resolveInput((short)0);
        Vec3d refAngle   = (Vec3d)executer.resolveInput((short)1);
        Double length     = (Double)executer.resolveInput((short)2);


        if(refBlock==null) refBlock=executer.player.getPosition();
        if(refAngle==null) refAngle=new Vec3d(0,0,0);
        if(length==null||length==0) length = 1.0D;
        double yaw = refAngle.zCoord+90;
        double pitch = -refAngle.yCoord;

        pitch = pitch+90;

        if(pitch>90)
        {
            pitch = 90-(pitch-90);
            yaw = yaw+180;
        }

        double newY = Math.sin(pitch*Math.PI/180);
        double newXZ = Math.cos(pitch*Math.PI/180);
        double newX = newXZ*Math.cos(yaw*Math.PI/180);
        double newZ = newXZ*Math.sin(yaw*Math.PI/180);

        Vec3d refVector = new Vec3d(newX, newY, newZ);

        return refBlock.add(viewToFaceVec(refVector, length));

    }

    public static Object blockDown(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)args[0];
        BlockPos refBlock = (BlockPos)executer.resolveInput((short)0);
        Vec3d refAngle   = (Vec3d)executer.resolveInput((short)1);
        Double length     = (Double)executer.resolveInput((short)2);


        if(refBlock==null) refBlock=executer.player.getPosition();
        if(refAngle==null) refAngle=new Vec3d(0,0,0);
        if(length==null||length==0) length = 1.0D;
        double yaw = refAngle.zCoord+90;
        double pitch = -refAngle.yCoord;

        pitch = pitch-90;

        if(pitch<-90)
        {
            pitch = -(pitch+90)-90;
            yaw = yaw+180;
        }

        double newY = Math.sin(pitch*Math.PI/180);
        double newXZ = Math.cos(pitch*Math.PI/180);
        double newX = newXZ*Math.cos(yaw*Math.PI/180);
        double newZ = newXZ*Math.sin(yaw*Math.PI/180);

        Vec3d refVector = new Vec3d(newX, newY, newZ);

        return refBlock.add(viewToFaceVec(refVector, length));

    }

    public static Object blockLeft(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)args[0];
        BlockPos refBlock = (BlockPos)executer.resolveInput((short)0);
        Vec3d refAngle   = (Vec3d)executer.resolveInput((short)1);
        Double length     = (Double)executer.resolveInput((short)2);


        if(refBlock==null) refBlock=executer.player.getPosition();
        if(refAngle==null) refAngle=new Vec3d(0,0,0);
        if(length==null||length==0) length = 1.0D;
        double yaw = refAngle.zCoord+90;
        double pitch = -refAngle.yCoord;
        pitch = 0;
        yaw = yaw-90;


        double newY = Math.sin(pitch*Math.PI/180);
        double newXZ = Math.cos(pitch*Math.PI/180);
        double newX = newXZ*Math.cos(yaw*Math.PI/180);
        double newZ = newXZ*Math.sin(yaw*Math.PI/180);

        Vec3d refVector = new Vec3d(newX, newY, newZ);

        return refBlock.add(viewToFaceVec(refVector, length));

    }

    public static Object blockRight(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)args[0];
        BlockPos refBlock = (BlockPos)executer.resolveInput((short)0);
        Vec3d refAngle   = (Vec3d)executer.resolveInput((short)1);
        Double length     = (Double)executer.resolveInput((short)2);


        if(refBlock==null) refBlock=executer.player.getPosition();
        if(refAngle==null) refAngle=new Vec3d(0,0,0);
        if(length==null||length==0) length = 1.0D;
        double yaw = refAngle.zCoord+90;
        double pitch = -refAngle.yCoord;
        pitch=0;
        yaw = yaw+90;

        double newY = Math.sin(pitch*Math.PI/180);
        double newXZ = Math.cos(pitch*Math.PI/180);
        double newX = newXZ*Math.cos(yaw*Math.PI/180);
        double newZ = newXZ*Math.sin(yaw*Math.PI/180);

        Vec3d refVector = new Vec3d(newX, newY, newZ);

        return refBlock.add(viewToFaceVec(refVector, length));

    }

    public static Object blockForward(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)args[0];
        BlockPos refBlock = (BlockPos)executer.resolveInput((short)0);
        Vec3d refAngle   = (Vec3d)executer.resolveInput((short)1);
        Double length     = (Double)executer.resolveInput((short)2);


        if(refBlock==null) refBlock=executer.player.getPosition();
        if(refAngle==null) refAngle=new Vec3d(0,0,0);
        if(length==null||length==0) length = 1.0D;
        double yaw = refAngle.zCoord+90;
        double pitch = -refAngle.yCoord;

        double newY = Math.sin(pitch*Math.PI/180);
        double newXZ = Math.cos(pitch*Math.PI/180);
        double newX = newXZ*Math.cos(yaw*Math.PI/180);
        double newZ = newXZ*Math.sin(yaw*Math.PI/180);

        Vec3d refVector = new Vec3d(newX, newY, newZ);

        return refBlock.add(viewToFaceVec(refVector, length));

    }

    public static Object blockBack(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)args[0];
        BlockPos refBlock = (BlockPos)executer.resolveInput((short)0);
        Vec3d refAngle   = (Vec3d)executer.resolveInput((short)1);
        Double length     = (Double)executer.resolveInput((short)2);


        if(refBlock==null) refBlock=executer.player.getPosition();
        if(refAngle==null) refAngle=new Vec3d(0,0,0);
        if(length==null||length==0) length = 1.0D;
        double yaw = refAngle.zCoord+90;
        double pitch = -refAngle.yCoord;

        yaw = yaw+180;

        double newY = Math.sin(pitch*Math.PI/180);
        double newXZ = Math.cos(pitch*Math.PI/180);
        double newX = newXZ*Math.cos(yaw*Math.PI/180);
        double newZ = newXZ*Math.sin(yaw*Math.PI/180);

        Vec3d refVector = new Vec3d(newX, newY, newZ);

        return refBlock.add(viewToFaceVec(refVector, length));

    }

    //turns a vector into an adjacent block position, ie 1,0,0 0,-1,0
    public static Vec3i viewToFaceVec(Vec3d viewVector, double length)
    {
        //normalize vector so we can work with it better
        viewVector = viewVector.normalize();
        int majorAxis = 0;
        if(Math.abs(viewVector.yCoord)>=Math.abs(viewVector.xCoord) && Math.abs(viewVector.yCoord)>=Math.abs(viewVector.zCoord)) majorAxis=1;
        else if(Math.abs(viewVector.zCoord)>=Math.abs(viewVector.xCoord) && Math.abs(viewVector.zCoord)>=Math.abs(viewVector.yCoord)) majorAxis=2;
        length = Math.round(length);

        switch(majorAxis)
        {
            case 0:
                return new Vec3i(Math.signum(viewVector.xCoord)*length,0,0);
            case 1:
                return new Vec3i(0,Math.signum(viewVector.yCoord)*length,0);
            case 2:
                return new Vec3i(0,0,Math.signum(viewVector.zCoord)*length);
            default:
                break;

        }

        return new Vec3i(1,0,0);
    }


}

