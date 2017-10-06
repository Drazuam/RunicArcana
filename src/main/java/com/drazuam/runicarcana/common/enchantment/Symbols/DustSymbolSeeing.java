package com.drazuam.runicarcana.common.enchantment.Symbols;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.common.enchantment.DustModelHandler;
import com.drazuam.runicarcana.common.enchantment.ModDust;
import com.drazuam.runicarcana.common.enchantment.ScriptExecuter;
import com.drazuam.runicarcana.common.enchantment.Signals.Signal;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Joel on 2/20/2017.
 */
public class DustSymbolSeeing extends DefaultDustSymbol {


    public static final DustModelHandler.DustTypes curDustType = DustModelHandler.DustTypes.SIGHT;

    public DustSymbolSeeing(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, curDustType);
        //set up signals
        addSignals();
    }

    public DustSymbolSeeing()
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
        addSignal(new Signal(this, Signal.SignalType.ENITITY, Signal.SigFlow.IN, "Entity",null,0));
        addSignal(new Signal(this, Signal.SignalType.BLOCKPOS, Signal.SigFlow.OUT, "Raytraced Block",DustSymbolSeeing::rayBlock,1));
        addSignal(new Signal(this, Signal.SignalType.NUMBER, Signal.SigFlow.IN, "Ray Dist",null,2));
        addSignal(new Signal(this, Signal.SignalType.VECTOR, Signal.SigFlow.OUT, "Look Vector", DustSymbolSeeing::lookVector, 3));
        addSignal(new Signal(this, Signal.SignalType.ANGLE, Signal.SigFlow.OUT, "Look Angle", DustSymbolSeeing::lookAngle, 4));
        addSignal(new Signal(this, Signal.SignalType.ENITITY, Signal.SigFlow.OUT, "Raytraced Entity", DustSymbolSeeing::rayEntity, 5));

    }


    public static Object rayBlock(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)args[0];
        Entity entity   = (Entity)executer.resolveInput((short)0);
        Double dist     = (Double)ModDust.parseNumber(executer.resolveInput((short)2));
        if(dist==null)dist=5.0D;

        executer.addProcesses(3);
        if(entity==null)return null;

        return entity.rayTrace(dist,1.0F).getBlockPos();
    }

    public static Object lookVector(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)args[0];
        Entity entity = (Entity)executer.resolveInput((short)0);
        if(entity==null)return new Vec3d(1,0,0);
        return entity.getLookVec();
    }

    public static Object lookAngle(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)args[0];
        Entity entity = (Entity)executer.resolveInput((short)0);
        if(entity==null)return new Vec3d(0,0,0);
        return new Vec3d(0,entity.rotationPitch, entity.rotationYaw);
    }

    public static Object rayEntity(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)args[0];
        Entity entity   = (Entity)executer.resolveInput((short)0);
        Double dist     = (Double)ModDust.parseNumber(executer.resolveInput((short)2));
        if(dist==null)dist=5.0D;

        executer.addProcesses(3);
        if(entity==null)return null;




        return actualRayTrace(entity,dist);
    }


    public static Entity actualRayTrace(Entity entity, double dist)
    {
        Vec3d vec3d = entity.getPositionEyes(1.0F);
        Vec3d vec3d1 = entity.getLook(1.0F);
        Vec3d vec3d2 = vec3d.addVector(vec3d1.xCoord * dist, vec3d1.yCoord * dist, vec3d1.zCoord * dist);

        Entity pointedEntity = null;
        List<Entity> list = entity.worldObj.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec3d1.xCoord * dist, vec3d1.yCoord * dist, vec3d1.zCoord * dist).expand(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>()
        {
            public boolean apply(@Nullable Entity p_apply_1_)
            {
                return p_apply_1_ != null && p_apply_1_.canBeCollidedWith();
            }
        }));


        for (int j = 0; j < list.size(); ++j)
        {
            Entity entity1 = (Entity)list.get(j);
            AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expandXyz((double)entity1.getCollisionBorderSize());
            RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);

            if (axisalignedbb.isVecInside(vec3d))
            {
                if (dist >= 0.0D)
                {
                    pointedEntity = entity1;
                    dist = 0.0D;
                }
            }
            else if (raytraceresult != null)
            {
                double d3 = vec3d.distanceTo(raytraceresult.hitVec);

                if (d3 < dist || dist == 0.0D)
                {
                    if (entity1.getLowestRidingEntity() == entity.getLowestRidingEntity() && !entity.canRiderInteract())
                    {
                        if (dist == 0.0D)
                        {
                            pointedEntity = entity1;
                        }
                    }
                    else
                    {
                        pointedEntity = entity1;
                        dist = d3;
                    }
                }
            }
        }

        return pointedEntity;


    }

}

