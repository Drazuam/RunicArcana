package com.drazuam.runicarcana.common.enchantment.Symbols;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.common.RunicArcana;
import com.drazuam.runicarcana.api.enchantment.ModDust;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by Joel on 2/20/2017.
 */
public class DustSymbolDickButt extends DefaultDustSymbol {
    public static final String MODEL_LOCATION = "block/dust/"+"dustDickbutt";
    public static final String TEXTURE_LOCATION = "textures/block/dustDickbutt.png";
    public static final String DEFAULT_NAME = "dustDickbutt";
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(RunicArcana.MODID, TEXTURE_LOCATION);

    public DustSymbolDickButt(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, ModDust.dickbuttSymbol.dustType);
    }

    public DustSymbolDickButt()
    {
        super(0,0,0,null,ModDust.dickbuttSymbol.dustType);
    }

    public DustSymbolDickButt(short newDustType) {
        super(newDustType);
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

