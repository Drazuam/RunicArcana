package com.drazuam.runicarcana.common.enchantment;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.api.enchantment.ModDust;
import com.drazuam.runicarcana.common.RunicArcana;
import com.drazuam.runicarcana.reference.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by Joel on 2/23/2017.
 */
public class DustInSymbol extends DustIOSymbol {


    public static final String MODEL_LOCATION = Reference.MODEL_LOCATION + "dustIn";
    public static final String TEXTURE_LOCATION = Reference.TEXTURE_LOCATION + "dustIn.png";
    public static final String DEFAULT_NAME = "dustIn";
    public static final int DUST_SIZE = 1;

    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(RunicArcana.MOD_ID, TEXTURE_LOCATION);

    public DefaultDustSymbol parent;


    public DustInSymbol(int X, int Z, DefaultDustSymbol newParent) {
        super(X, Z, newParent,  ModDust.inSymbol.dustType);
        parent = newParent;

    }

    public DustInSymbol()
    {
        super(0,0, ModDust.inSymbol.dustType);
    }

    public DustInSymbol(short newID)
    {
        super(0,0,newID);
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
