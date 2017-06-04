package com.drazuam.omnimancy.common.enchantment.Symbols;

import com.drazuam.omnimancy.common.enchantment.DefaultDustSymbol;
import com.drazuam.omnimancy.common.enchantment.DustModelHandler;
import com.drazuam.omnimancy.common.enchantment.ModDust;
import com.drazuam.omnimancy.common.tileentity.TileEntityChalkBase;
import net.minecraft.util.math.BlockPos;

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

