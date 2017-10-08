package com.drazuam.runicarcana.common.enchantment.Symbols;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.common.RunicArcana;
import com.drazuam.runicarcana.api.enchantment.ModDust;
import com.drazuam.runicarcana.common.enchantment.ScriptExecutor;
import com.drazuam.runicarcana.api.enchantment.Signals.Signal;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.awt.*;

/**
 * Created by Joel on 2/20/2017.
 */
public class DustSymbolChange extends DefaultDustSymbol {


    public static final String MODEL_LOCATION = "block/dust/"+"dustChange";
    public static final String TEXTURE_LOCATION = "textures/block/dustChange.png";
    public static final String DEFAULT_NAME = "dustChange";
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(RunicArcana.MODID, TEXTURE_LOCATION);

    public DustSymbolChange(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, ModDust.changeSymbol.dustType);

    }

    public DustSymbolChange()
    {
        super(0,0,0,null,ModDust.changeSymbol.dustType);
        addMethods();

    }

    public DustSymbolChange(short newDustType) {
        super(newDustType);
        addMethods();
    }

    private void addMethods()
    {
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.IN, "Start in", DustSymbolChange::ChangeBlockToAir ,0));
        addSignal(new Signal(this, Signal.SignalType.ENTITY, Signal.SigFlow.IN, "Entity in",null,1));
        addSignal(new Signal(this, Signal.SignalType.NUMBER,  Signal.SigFlow.IN, "Length in",null,2));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "Done",null,3));

    }

    public static Object ChangeBlockToAir(Object... args)
    {
        ScriptExecutor executor = (ScriptExecutor)args[0];

        if(!executor.player.worldObj.isRemote) {
            RayTraceResult ray = ((Entity)executor.resolveInput((short)1)).rayTrace(5,1.0F);
            Double length = ((Double)executor.resolveInput((short)2));
            if(length==null)length=1.0D;
            if(ray.typeOfHit== RayTraceResult.Type.BLOCK) {
                for(int i=0; i<length; i++)
                executor.player.worldObj.setBlockToAir(ray.getBlockPos().up(i));
            }

        }
        executor.addProcesses(5);
        executor.resolveOutput((short)3,true);

        return null;
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

    @Override
    public Color getColor() {
        return Color.BLUE;
    }
}

