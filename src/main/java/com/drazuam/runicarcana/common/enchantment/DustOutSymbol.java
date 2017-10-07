package com.drazuam.runicarcana.common.enchantment;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.api.enchantment.ModDust;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by Joel on 2/23/2017.
 */
public class DustOutSymbol extends DustIOSymbol {

    public static final String MODEL_LOCATION = "block/dust/"+"dustOut";
    public static final String TEXTURE_LOCATION = "textures/block/dustOut.png";
    public static final String DEFAULT_NAME = "dustOut";
    public static final int DUST_SIZE = 1;

    public DefaultDustSymbol parent;


    public DustOutSymbol(int X, int Z, DefaultDustSymbol newParent) {
        super(X, Z, newParent, ModDust.outSymbol.dustType);
        parent = newParent;

    }

    public DustOutSymbol()
    {
        super(0,0,ModDust.outSymbol.dustType);
    }

    public DustOutSymbol(short newID)
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

