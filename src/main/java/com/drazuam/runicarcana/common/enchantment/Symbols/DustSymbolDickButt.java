package com.drazuam.runicarcana.common.enchantment.Symbols;

import com.drazuam.runicarcana.common.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.common.enchantment.DustModelHandler;
import com.drazuam.runicarcana.common.enchantment.ModDust;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;

/**
 * Created by Joel on 2/20/2017.
 */
public class DustSymbolDickButt extends DefaultDustSymbol {


    public static final DustModelHandler.DustTypes curDustType = DustModelHandler.DustTypes.DICKBUTT;

    public DustSymbolDickButt(int X, int Z, int F, TileEntityChalkBase newParent) {
        super(X, Z, F,newParent, curDustType);
    }

    public static final short dustID = ModDust.getNextDustID();
    @Override
    public short getDustID() {
        return dustID;
    }

    public DustSymbolDickButt()
    {
        super(0,0,0,null,curDustType);
    }
}

