package com.drazuam.runicarcana.common.enchantment.Symbols;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.api.enchantment.ModDust;
import com.drazuam.runicarcana.api.enchantment.Signals.Signal;
import com.drazuam.runicarcana.common.RunicArcana;
import com.drazuam.runicarcana.common.enchantment.ScriptExecutor;
import com.drazuam.runicarcana.common.item.ModItems;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import com.drazuam.runicarcana.reference.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.awt.*;

/**
 * Created by Joel on 2/20/2017.
 */
public class DustSymbolConstantVector extends DefaultDustSymbol {

    public static final String MODEL_LOCATION = Reference.MODEL_LOCATION + "dustConstant";
    public static final String TEXTURE_LOCATION = Reference.TEXTURE_LOCATION + "dustConstant.png";
    public static final String DEFAULT_NAME = "dustConstantVector";
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(RunicArcana.MOD_ID, TEXTURE_LOCATION);

    public DustSymbolConstantVector(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, ModDust.vectorConstantSymbol.dustType);
        addSignals();

    }

    public DustSymbolConstantVector()
    {
        super(0,0,0,null, ModDust.vectorConstantSymbol.dustType);

        addSignals();

    }

    public DustSymbolConstantVector(short newDustType) {
        super(newDustType);
        addSignals();
    }

    private void addSignals()
    {
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT,  "Done", null,0));
        addSignal(new Signal(this, Signal.SignalType.VECTOR, Signal.SigFlow.OUT,   "Vector", DustSymbolConstantVector::vectorVariable,1));
        addSignal(new Signal(this, Signal.SignalType.VECTOR, Signal.SigFlow.IN,    "Vector", null,2));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.IN,   "Set Vector", DustSymbolConstantVector::setVector,3));
    }


    public static Object setVector(Object... args)
    {
        ScriptExecutor executor = (ScriptExecutor)(args[0]);
        Vec3d vec = (Vec3d)executor.resolveInput((short)2);

        if(vec!=null)
        {
            executor.setVariable(ModDust.VectorToString(vec));

        }

        executor.resolveOutput((short)(0),true);

        return true;
    }

    public static Object vectorVariable(Object... args)
    {
        ScriptExecutor executor = (ScriptExecutor)(args[0]);
        return ModDust.parseVector(executor.getVariable());
    }

    @Override
    public String getVariable() {
        TileEntityChalkBase te = this.getParent();
        if(te.hasItem()&&te.getItem().getTagCompound()!=null&&te.getItem().getTagCompound().hasKey("text"))
        {
            return te.getItem().getTagCompound().getString("text");
        }

        return super.getVariable();
    }

    @Override
    public boolean willAccept(ItemStack stack) {
        return stack.getItem()== ModItems.paperScrapItem;
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
        return new Color(Signal.SignalType.VECTOR.color);
    }
}

