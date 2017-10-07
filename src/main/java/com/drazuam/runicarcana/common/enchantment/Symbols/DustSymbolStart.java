package com.drazuam.runicarcana.common.enchantment.Symbols;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.common.RunicArcana;
import com.drazuam.runicarcana.api.enchantment.ModDust;
import com.drazuam.runicarcana.common.enchantment.ScriptExecuter;
import com.drazuam.runicarcana.common.enchantment.Signals.Signal;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by Joel on 2/20/2017.
 */
public class DustSymbolStart extends DefaultDustSymbol {

    public static final String MODEL_LOCATION = "block/dust/"+"dustStart";
    public static final String TEXTURE_LOCATION = "textures/block/dustStart.png";
    public static final String DEFAULT_NAME = "dustStart";
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(RunicArcana.MODID, TEXTURE_LOCATION);


    public DustSymbolStart(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, ModDust.startSymbol.dustType);

        addSignals();



    }

    public DustSymbolStart()
    {
        super(0,0,0,null,ModDust.startSymbol.dustType);

        addSignals();

    }

    public DustSymbolStart(short newDustType) {
        super(newDustType);
        addSignals();
    }

    private void addSignals()
    {
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "Right Clicked",DustSymbolStart::rightClick,0));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "Sneak Right Clicked",DustSymbolStart::sneakRightClick,1));
        addSignal(new Signal(this, Signal.SignalType.ENTITY, Signal.SigFlow.OUT, "Player Entity",DustSymbolStart::playerEntity,2));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT, "Block Break",DustSymbolStart::blockBreak,3));
        addSignal(new Signal(this, Signal.SignalType.BLOCKPOS, Signal.SigFlow.OUT, "Init Block",DustSymbolStart::initBlockPos,4));
    }


    public static Object rightClick(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)(args[0]);
        executer.resolveOutput((short)0,true);
        return true;
    }

    public static Object sneakRightClick(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)(args[0]);
        executer.resolveOutput((short)1,true);
        return true;
    }

    public static Object blockBreak(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)(args[0]);
        executer.resolveOutput((short)3,true);
        return true;
    }

    public static EntityPlayer playerEntity(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)(args[0]);
        return executer.player;
    }

    public static Object initBlockPos(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)(args[0]);

        return executer.initPos;
    }

    @Override
    public boolean willAccept(ItemStack stack) {
        return stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemSword;
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
