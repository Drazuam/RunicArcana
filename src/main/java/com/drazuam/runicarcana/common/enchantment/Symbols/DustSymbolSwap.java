package com.drazuam.runicarcana.common.enchantment.Symbols;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.common.RunicArcana;
import com.drazuam.runicarcana.api.enchantment.ModDust;
import com.drazuam.runicarcana.common.enchantment.ScriptExecutor;
import com.drazuam.runicarcana.api.enchantment.Signals.Signal;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import com.drazuam.runicarcana.reference.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.entity.Entity;

/**
 * created by Matt on 10/7/2017
 */

public class DustSymbolSwap extends DefaultDustSymbol {

    public static final String MODEL_LOCATION = Reference.MODEL_LOCATION + "dustSwap";
    public static final String TEXTURE_LOCATION = Reference.TEXTURE_LOCATION + "dustSwap.png";
    public static final String DEFAULT_NAME = "dustSwap";
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(RunicArcana.MOD_ID, TEXTURE_LOCATION);

    public DustSymbolSwap(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, ModDust.swapSymbol.dustType);
        //set up signals
        addSignals();
    }

    public DustSymbolSwap()
    {
        super(0,0,0,null,ModDust.swapSymbol.dustType);
        //set up signals
        addSignals();
    }

    public DustSymbolSwap(short newDustType) {
        super(newDustType);
        addSignals();
    }

    private void addSignals()
    {
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.IN, "Swap", DustSymbolSwap::Swap, 0));
        addSignal(new Signal(this, Signal.SignalType.ENTITY, Signal.SigFlow.IN, "Player Entity", null, 1));
        addSignal(new Signal(this, Signal.SignalType.ENTITY, Signal.SigFlow.IN, "Swap With", null, 2));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "Done", null, 3));
    }

    public static Object Swap(Object... args)
    {
        ScriptExecutor executor = (ScriptExecutor)args[0];
        EntityPlayer player;

        try {
            player = (EntityPlayer)executor.resolveInput((short)1);
        }
        catch (ClassCastException e)
        {
            return null;
        }

        Entity swapWith = (Entity)executor.resolveInput((short)2);

        if (player == null || swapWith == null) {

            return null;
        }

        double tempX, tempY, tempZ;

        tempX = player.posX;
        tempY = player.posY;
        tempZ = player.posZ;

        player.setPositionAndUpdate(swapWith.posX, swapWith.posY, swapWith.posZ);

        if (swapWith instanceof EntityPlayer)
        {
            swapWith.setPositionAndUpdate(tempX, tempY, tempZ);
        }
        else
        {
            swapWith.setPosition(tempX, tempY, tempZ);
        }

        executor.resolveOutput((short)3, true);
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
