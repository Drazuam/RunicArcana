package com.drazuam.runicarcana.common.enchantment.Symbols;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.common.RunicArcana;
import com.drazuam.runicarcana.api.enchantment.ModDust;
import com.drazuam.runicarcana.common.enchantment.ScriptExecutor;
import com.drazuam.runicarcana.common.enchantment.Signals.Signal;
import com.drazuam.runicarcana.common.item.ModItems;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import java.lang.reflect.Field;

/**
 * Created by Joel on 2/20/2017.
 */
public class DustSymbolName extends DefaultDustSymbol {


    public static final String MODEL_LOCATION = "block/dust/"+"dustName";
    public static final String TEXTURE_LOCATION = "textures/block/dustName.png";
    public static final String DEFAULT_NAME = "dustName";
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(RunicArcana.MODID, TEXTURE_LOCATION);


    public DustSymbolName(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, ModDust.nameSymbol.dustType);

        addSignals();



    }

    public DustSymbolName()
    {
        super(0,0,0,null,ModDust.nameSymbol.dustType);

        addSignals();

    }

    public DustSymbolName(short newDustType) {
        super(newDustType);
        addSignals();
    }

    private void addSignals()
    {
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.OUT,  "Done", null,0));
        addSignal(new Signal(this, Signal.SignalType.STRING, Signal.SigFlow.IN,   "Name", null,1));
        addSignal(new Signal(this, Signal.SignalType.CONTROL, Signal.SigFlow.IN,  "Set Name", DustSymbolName::setName,2));
    }


    public static Object setName(Object... args)
    {
        ScriptExecutor executer = (ScriptExecutor)(args[0]);
        String name = (String)executer.resolveInput((short)1);

        if(name!=null)
        {
            executer.setVariable(name.toString());
            executer.RunicItem.setStackDisplayName(TextFormatting.RESET+name);
            //executer.player.replaceItemInInventory(executer.player.inventory.currentItem, executer.player.inventory.getCurrentItem());
            if(executer.player.worldObj.isRemote)
            {
                Object myObj = Minecraft.getMinecraft().ingameGUI;
                Class myClass = myObj.getClass();
                try {
                    Field myField = getField(myClass, "remainingHighlightTicks");
                    myField.setAccessible(true);
                    myField.set(myObj,40);

                }
                catch(NoSuchFieldException|IllegalAccessException e) {
                    System.out.println("Draz is trying to be sneaky but forge hates him, ");
                    if(e instanceof NoSuchFieldException)
                        System.out.println("no such field");
                    if(e instanceof IllegalAccessException)
                        System.out.println("illegal access");
                }
            }

        }

        executer.resolveOutput((short)(0),true);

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

    private static Field getField(Class clazz, String fieldName)
            throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw e;
            } else {
                return getField(superClass, fieldName);
            }
        }
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

