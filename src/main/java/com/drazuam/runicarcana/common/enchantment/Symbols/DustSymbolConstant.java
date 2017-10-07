package com.drazuam.runicarcana.common.enchantment.Symbols;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.common.RunicArcana;
import com.drazuam.runicarcana.api.enchantment.ModDust;
import com.drazuam.runicarcana.common.enchantment.ScriptExecuter;
import com.drazuam.runicarcana.common.enchantment.Signals.Signal;
import com.drazuam.runicarcana.common.item.ModItems;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by Joel on 2/20/2017.
 */
public class DustSymbolConstant extends DefaultDustSymbol {

    public static final String MODEL_LOCATION = "block/dust/"+"dustConstant";
    public static final String TEXTURE_LOCATION = "textures/block/dustConstant.png";
    public static final String DEFAULT_NAME = "dustConstant";
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(RunicArcana.MODID, TEXTURE_LOCATION);

    public DustSymbolConstant(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, ModDust.constantSymbol.dustType);

        addSignals();



    }

    public DustSymbolConstant()
    {
        super(0,0,0,null, ModDust.constantSymbol.dustType);

        addSignals();

    }

    public DustSymbolConstant(short newDustType) {
        super(newDustType);
        addSignals();
    }

    private void addSignals()
    {
        addSignal(new Signal(this, Signal.SignalType.NUMBER, Signal.SigFlow.OUT,   "Number", DustSymbolConstant::numberVariable,0));
        addSignal(new Signal(this, Signal.SignalType.NUMBER, Signal.SigFlow.IN,    "Number", null,1));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.IN,   "Set Number", DustSymbolConstant::setNumber,2));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT,  "Done", null,3));
        addSignal(new Signal(this, Signal.SignalType.STRING, Signal.SigFlow.OUT,   "String", DustSymbolConstant::stringVariable,4));
        addSignal(new Signal(this, Signal.SignalType.BLOCKPOS, Signal.SigFlow.OUT,   "Block Pos", DustSymbolConstant::blockPosVariable,5));
        addSignal(new Signal(this, Signal.SignalType.BLOCKPOS, Signal.SigFlow.IN,    "Block Pos", null,6));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.IN,   "Set Block Pos", DustSymbolConstant::setBlockPos,7));
        addSignal(new Signal(this, Signal.SignalType.VECTOR, Signal.SigFlow.OUT,   "Vector", DustSymbolConstant::vectorVariable,8));
        addSignal(new Signal(this, Signal.SignalType.VECTOR, Signal.SigFlow.IN,    "Vector", null,9));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.IN,   "Set Vector", DustSymbolConstant::setVector,10));
    }


    public static Object stringVariable(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)(args[0]);
        return executer.getVariable();
    }

    public static Object numberVariable(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)(args[0]);
        return ModDust.parseNumber(executer.getVariable());
    }

    public static Object setNumber(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)(args[0]);
        Double number = (Double)executer.resolveInput((short)1);

        if(number!=null)
        {
            executer.setVariable(number.toString());
            executer.variablesChanged = true;
        }

        executer.resolveOutput((short)(3),true);

        return true;
    }

    public static Object setBlockPos(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)(args[0]);
        BlockPos pos = (BlockPos)executer.resolveInput((short)6);

        if(pos!=null)
        {
            executer.setVariable(ModDust.BlockPosToString(pos));
            executer.variablesChanged = true;
        }

        executer.resolveOutput((short)(3),true);

        return true;
    }

    public static Object blockPosVariable(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)(args[0]);
        return ModDust.parseBlockPos(executer.getVariable());
    }

    public static Object setVector(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)(args[0]);
        Vec3d vec = (Vec3d)executer.resolveInput((short)9);

        if(vec!=null)
        {
            executer.setVariable(ModDust.VectorToString(vec));
            executer.variablesChanged = true;
        }

        executer.resolveOutput((short)(3),true);

        return true;
    }

    public static Object vectorVariable(Object... args)
    {
        ScriptExecuter executer = (ScriptExecuter)(args[0]);
        return ModDust.parseVector(executer.getVariable());
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
}

