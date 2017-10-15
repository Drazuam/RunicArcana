package com.drazuam.runicarcana.common.enchantment.Symbols;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.api.enchantment.ModDust;
import com.drazuam.runicarcana.api.enchantment.Signals.Signal;
import com.drazuam.runicarcana.client.Particle.WatergunFX;
import com.drazuam.runicarcana.common.RunicArcana;
import com.drazuam.runicarcana.common.enchantment.ScriptExecutor;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import com.drazuam.runicarcana.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;

import java.util.Random;

/**
 * Created on 10/9/2017 by Matt
 */

public class DustSymbolWatergun extends DefaultDustSymbol {

    public static final String MODEL_LOCATION = Reference.Model_Location + "dustWatergun";
    public static final String TEXTURE_LOCATION = Reference.Texture_Location + "dustWatergun.png";
    public static final String DEFAULT_NAME = "dustWatergun";
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(RunicArcana.MOD_ID, TEXTURE_LOCATION);

    public DustSymbolWatergun(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, ModDust.watergunSymbol.dustType);
        //set up signals
        addSignals();
    }

    public DustSymbolWatergun() {
        super(0,0,0,null,ModDust.watergunSymbol.dustType);
        //set up signals
        addSignals();
    }

    public DustSymbolWatergun(short newDustType) {
        super(newDustType);
        addSignals();
    }

    private void addSignals() {

        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.IN, "Water Gun", DustSymbolWatergun::Watergun, 0));
        addSignal(new Signal(this, Signal.SignalType.ENTITY, Signal.SigFlow.IN, "Target", null, 1));
        addSignal(new Signal(this, Signal.SignalType.NUMBER, Signal.SigFlow.IN, "Damage", null, 2));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "Done",  null, 3));
        addSignal(new Signal(this, Signal.SignalType.NUMBER, Signal.SigFlow.IN, "Speed", null, 4));
    }

    public static Object Watergun(Object... args)
    {

        ScriptExecutor executor = (ScriptExecutor)args[0];

        Random rand = new Random();

        Entity target = (Entity)executor.resolveInput((short)1);
        Float damage = new Float((Double)executor.resolveInput((short)2));



        Vec3d look = executor.player.getLookVec();
        Double speed = (Double)executor.resolveInput((short)4);

        if (damage == null)
        {
            damage = 1.0F;
        }

        if (speed == null)
        {
            speed = 1.0D;
        }

        if (target == null)
        {
            return null;
        }

        look.scale(speed);

        //based on code from entityGuardian

        double d5 = 0.5F;
        double d0 = target.posX - executor.player.posX;
        double d1 = target.posY + (double)(target.height * 0.5F) - (executor.player.posY + (double)executor.player.getEyeHeight());
        double d2 = target.posZ - executor.player.posZ;
        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
        d0 = d0 / d3;
        d1 = d1 / d3;
        d2 = d2 / d3;
        double d4 = rand.nextDouble();


        look = look.rotateYaw((float)((rand.nextGaussian()) * 4.0D * Math.PI / 180.0D));
        look = look.rotatePitch((float)((rand.nextGaussian()) * 4.0D * Math.PI / 180.0D));


        Minecraft.getMinecraft().effectRenderer.addEffect(new WatergunFX(executor.player.worldObj,
                                                                executor.player.posX + d0 * d4,
                                                                executor.player.posY + d1 * d4 + (double)executor.player.getEyeHeight()*0.5F,
                                                                executor.player.posZ + d2 * d4,
                                                                 look.xCoord,
                                                                 look.yCoord,
                                                                 look.zCoord,
                                                                 damage,
                                                                 target));

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
