package com.drazuam.runicarcana.common.enchantment.Symbols;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.common.RunicArcana;
import com.drazuam.runicarcana.api.enchantment.ModDust;
import com.drazuam.runicarcana.common.enchantment.ScriptExecutor;
import com.drazuam.runicarcana.api.enchantment.Signals.Signal;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import com.drazuam.runicarcana.reference.Reference;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Joel on 2/20/2017.
 */
public class DustSymbolSight extends DefaultDustSymbol {

    public static final String MODEL_LOCATION = Reference.MODEL_LOCATION + "dustSight";
    public static final String TEXTURE_LOCATION = Reference.TEXTURE_LOCATION + "dustSight.png";
    public static final String DEFAULT_NAME = "dustSight";
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(RunicArcana.MOD_ID, TEXTURE_LOCATION);

    public DustSymbolSight(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, ModDust.sightSymbol.dustType);
        //set up signals
        addSignals();
    }

    public DustSymbolSight()
    {
        super(0,0,0,null,ModDust.sightSymbol.dustType);
        //set up signals
        addSignals();
    }

    public DustSymbolSight(short newDustType) {
        super(newDustType);
        addSignals();
    }

    private void addSignals()
    {
        addSignal(new Signal(this, Signal.SignalType.ENTITY, Signal.SigFlow.IN, "Entity",null,0));
        addSignal(new Signal(this, Signal.SignalType.VECTOR, Signal.SigFlow.OUT, "Raytraced Block", DustSymbolSight::rayBlock,1));
        addSignal(new Signal(this, Signal.SignalType.NUMBER, Signal.SigFlow.IN, "Ray Dist",null,2));
        addSignal(new Signal(this, Signal.SignalType.VECTOR, Signal.SigFlow.OUT, "Look Vector", DustSymbolSight::lookVector, 3));
        addSignal(new Signal(this, Signal.SignalType.ANGLE, Signal.SigFlow.OUT, "Look Angle", DustSymbolSight::lookAngle, 4));
        addSignal(new Signal(this, Signal.SignalType.ENTITY, Signal.SigFlow.OUT, "Raytraced Entity", DustSymbolSight::rayEntity, 5));

    }


    public static Object rayBlock(Object... args)
    {
        ScriptExecutor executor = (ScriptExecutor)args[0];
        Entity entity   = (Entity)executor.resolveInput((short)0);
        Double dist     = (Double)ModDust.parseNumber(executor.resolveInput((short)2));
        if(dist==null)dist=5.0D;

        executor.addProcesses(3);
        if(entity==null)return null;

        return entity.rayTrace(dist,1.0F).getBlockPos();
    }

    public static Object lookVector(Object... args)
    {
        ScriptExecutor executor = (ScriptExecutor)args[0];
        Entity entity = (Entity)executor.resolveInput((short)0);
        if(entity==null)return new Vec3d(1,0,0);
        return entity.getLookVec();
    }

    public static Object lookAngle(Object... args)
    {
        ScriptExecutor executor = (ScriptExecutor)args[0];
        Entity entity = (Entity)executor.resolveInput((short)0);
        if(entity==null)return new Vec3d(0,0,0);
        return new Vec3d(0,entity.rotationPitch, entity.rotationYaw);
    }

    public static Object rayEntity(Object... args)
    {
        ScriptExecutor executor = (ScriptExecutor)args[0];
        Entity entity   = (Entity)executor.resolveInput((short)0);
        Double dist     = (Double)ModDust.parseNumber(executor.resolveInput((short)2));
        if(dist==null)dist=32.0D;

        executor.addProcesses(3);
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

