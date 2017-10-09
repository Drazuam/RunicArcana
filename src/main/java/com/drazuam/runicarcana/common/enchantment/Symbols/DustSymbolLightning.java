package com.drazuam.runicarcana.common.enchantment.Symbols;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.api.enchantment.ModDust;
import com.drazuam.runicarcana.api.enchantment.Signals.Signal;
import com.drazuam.runicarcana.common.RunicArcana;
import com.drazuam.runicarcana.common.enchantment.ScriptExecutor;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created on 10/8/2017 by Matt
 */

public class DustSymbolLightning extends DefaultDustSymbol{

    public static final String MODEL_LOCATION = "block/dust/"+"dustLightning";
    public static final String TEXTURE_LOCATION = "textures/block/dustLightning.png";
    public static final String DEFAULT_NAME = "dustLightning";
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(RunicArcana.MODID, TEXTURE_LOCATION);

    public DustSymbolLightning(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, ModDust.lightningSymbol.dustType);
        //set up signals
        addSignals();
    }

    public DustSymbolLightning() {
        super(0,0,0,null,ModDust.lightningSymbol.dustType);
        //set up signals
        addSignals();
    }

    public DustSymbolLightning(short newDustType) {
        super(newDustType);
        addSignals();
    }

    private void addSignals() {

        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.IN, "Lightning", DustSymbolLightning::Lightning, 0));
        addSignal(new Signal(this, Signal.SignalType.ENTITY, Signal.SigFlow.IN, "Target", null, 1));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "Done", null, 2));
    }

    public static Object Lightning(Object... args) {

        ScriptExecutor executor = (ScriptExecutor)args[0];

        Entity target = (Entity)executor.resolveInput((short)1);

        if (target == null) {

            return null;
        }

        executor.player.worldObj.addWeatherEffect(new EntityLightningBolt(executor.player.worldObj, target.posX, target.posY, target.posZ, false));

        executor.resolveOutput((short)2, true);
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
