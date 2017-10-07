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
public class DustSymbolConnector extends DefaultDustSymbol {

    public static final String MODEL_LOCATION = "block/dust/"+"dustConnector";
    public static final String TEXTURE_LOCATION = "textures/block/dustConnector.png";
    public static final String DEFAULT_NAME = "dustConnector";
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(RunicArcana.MODID, TEXTURE_LOCATION);

    public DustSymbolConnector(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, ModDust.inSymbol.dustType);
    }

    public DustSymbolConnector()
    {
        super(0,0,0,null,ModDust.inSymbol.dustType);
    }

    public DustSymbolConnector(short newDustType) {
        super(newDustType);
    }

    @Override
    public DefaultDustSymbol getIODust(int X, int Z) {
        return this;
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
