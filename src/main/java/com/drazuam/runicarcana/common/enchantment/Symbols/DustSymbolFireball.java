package com.drazuam.runicarcana.common.enchantment.Symbols;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.common.RunicArcana;
import com.drazuam.runicarcana.api.enchantment.ModDust;
import com.drazuam.runicarcana.common.enchantment.ScriptExecutor;
import com.drazuam.runicarcana.api.enchantment.Signals.Signal;
import com.drazuam.runicarcana.common.entity.EntityLargeSettableFireball;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import com.drazuam.runicarcana.reference.Reference;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * created by Matt on 10/8/2017
 */


public class DustSymbolFireball extends DefaultDustSymbol {

    public static final String MODEL_LOCATION = Reference.Model_Location + "dustFireball";
    public static final String TEXTURE_LOCATION = Reference.Texture_Location + "dustFireball.png";
    public static final String DEFAULT_NAME = "dustFireball";
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(RunicArcana.MOD_ID, TEXTURE_LOCATION);

    public DustSymbolFireball(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, ModDust.fireballSymbol.dustType);
        //set up signals
        addSignals();
    }

    public DustSymbolFireball() {
        super(0,0,0,null,ModDust.fireballSymbol.dustType);
        //set up signals
        addSignals();
    }

    public DustSymbolFireball(short newDustType) {
        super(newDustType);
        addSignals();
    }

    private void addSignals() {

        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.IN, "FireBall", DustSymbolFireball::FireBall, 0));
        addSignal(new Signal(this, Signal.SignalType.VECTOR, Signal.SigFlow.IN, "Direction", null, 1));
        addSignal(new Signal(this, Signal.SignalType.NUMBER, Signal.SigFlow.IN, "Speed", null, 2));
        addSignal(new Signal(this, Signal.SignalType.VECTOR, Signal.SigFlow.IN, "Init Pos", null, 3));
        addSignal(new Signal(this, Signal.SignalType.VECTOR, Signal.SigFlow.IN, "Init Pos (rel)", null, 4));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "Done",  null, 5));
    }

    public static Object FireBall(Object... args) {

        ScriptExecutor executor = (ScriptExecutor)args[0];

        Vec3d look = (Vec3d)executor.resolveInput((short)1);
        Double speed = (Double)executor.resolveInput((short)2);
        Vec3d initPos = (Vec3d)executor.resolveInput((short)3);
        Vec3d initPosRel = (Vec3d)executor.resolveInput((short)4);

        if (look == null) {
            look = executor.player.getLookVec();
        }
        if (speed == null) {
            speed = 0.1;
        }
        if (initPos == null) {
            if(initPosRel==null)
                initPos = executor.player.getPositionEyes(1.0F).add(executor.player.getLookVec().scale(2));
            else
                initPos = initPosRel.add(executor.player.getPositionVector());
        }

        look = look.scale(speed);

        EntityLargeFireball fireball = new EntityLargeSettableFireball(executor.player.worldObj, initPos.xCoord,
                                                                        initPos.yCoord, initPos.zCoord,
                                                                        look.xCoord, look.yCoord,
                                                                        look.zCoord);

        executor.player.worldObj.spawnEntityInWorld(fireball);

        executor.resolveOutput((short)5, true);
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
