package com.drazuam.runicarcana.common.enchantment.Symbols;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.common.RunicArcana;
import com.drazuam.runicarcana.api.enchantment.ModDust;
import com.drazuam.runicarcana.common.enchantment.ScriptExecutor;
import com.drazuam.runicarcana.api.enchantment.Signals.Signal;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import com.drazuam.runicarcana.reference.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by Joel on 2/20/2017.
 */
public class DustSymbolVelocity extends DefaultDustSymbol {

    public static final String MODEL_LOCATION   = Reference.MODEL_LOCATION + "dustMove";
    public static final String TEXTURE_LOCATION = Reference.TEXTURE_LOCATION + "dustMove.png";
    public static final String DEFAULT_NAME     = "dustVelocity";
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(RunicArcana.MOD_ID, TEXTURE_LOCATION);

    public DustSymbolVelocity(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, ModDust.velocitySymbol.dustType);
        //set up signals
        addSignals();
    }

    public DustSymbolVelocity()
    {
        super(0,0,0,null,ModDust.velocitySymbol.dustType);
        //set up signals
        addSignals();
    }

    public DustSymbolVelocity(short newDustType) {
        super(newDustType);
        addSignals();
    }

    private void addSignals()
    {
        addSignal(new Signal(this, Signal.SignalType.VECTOR, Signal.SigFlow.IN, "Velocity",  null, 0));
        addSignal(new Signal(this, Signal.SignalType.NUMBER, Signal.SigFlow.IN, "Magnitude", null, 1));
        addSignal(new Signal(this, Signal.SignalType.ENTITY, Signal.SigFlow.IN, "Entity",    null, 2));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.IN, "Add Vel",  DustSymbolVelocity::addVelocity, 3));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "Done",    null, 4));
        addSignal(new Signal(this, Signal.SignalType.VECTOR, Signal.SigFlow.OUT, "Velocity", DustSymbolVelocity::getVelocity, 5));

    }

    public static Object getVelocity(Object... args)
    {
        ScriptExecutor executor = (ScriptExecutor)(args[0]);

        Entity entity = (Entity)executor.resolveInput((short)2);
        if(entity==null)return null;

        return new Vec3d(entity.motionX, entity.motionY, entity.motionZ);
    }

    public static Object addVelocity(Object... args)
    {
        ScriptExecutor executor = (ScriptExecutor)(args[0]);

        Vec3d velocity   = (Vec3d)executor.resolveInput((short)0);
        Double magnitude = (Double)executor.resolveInput((short)1);
        Entity entity    = (Entity)executor.resolveInput((short)2);

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

        executor.addProcesses(8);
        //TODO: mana costs: should cost more if entity is a player.
        executor.resolveOutput((short)4,true);
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

