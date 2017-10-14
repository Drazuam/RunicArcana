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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.awt.*;

/**
 * Created by Joel on 2/20/2017.
 */
public class DustSymbolConstantNumber extends DefaultDustSymbol {

    public static final String MODEL_LOCATION = Reference.Model_Location + "dustConstant";
    public static final String TEXTURE_LOCATION = Reference.Texture_Location + "dustConstant.png";
    public static final String DEFAULT_NAME = "dustConstantNumber";
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(RunicArcana.MOD_ID, TEXTURE_LOCATION);

    public DustSymbolConstantNumber(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, ModDust.numberConstantSymbol.dustType);

        addSignals();



    }

    public DustSymbolConstantNumber()
    {
        super(0,0,0,null, ModDust.numberConstantSymbol.dustType);

        addSignals();

    }

    public DustSymbolConstantNumber(short newDustType) {
        super(newDustType);
        addSignals();
    }

    private void addSignals()
    {
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT,  "Done", null,0));
        addSignal(new Signal(this, Signal.SignalType.NUMBER, Signal.SigFlow.OUT,   "Number", DustSymbolConstantNumber::numberVariable,1));
        addSignal(new Signal(this, Signal.SignalType.NUMBER, Signal.SigFlow.IN,    "Number", null,2));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.IN,   "Set Number", DustSymbolConstantNumber::setNumber,3));
    }

    public static Object numberVariable(Object... args)
    {
        ScriptExecutor executor = (ScriptExecutor)(args[0]);
        return ModDust.parseNumber(executor.getVariable());
    }

    public static Object setNumber(Object... args)
    {
        ScriptExecutor executor = (ScriptExecutor)(args[0]);
        Double number = (Double)executor.resolveInput((short)2);

        if(number!=null)
        {
            executor.setVariable(number.toString());

        }

        executor.resolveOutput((short)(0),true);

        return true;
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
        return new Color(Signal.SignalType.NUMBER.color);
    }
}

