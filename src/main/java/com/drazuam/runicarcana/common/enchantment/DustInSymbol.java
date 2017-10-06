package com.drazuam.runicarcana.common.enchantment;

/**
 * Created by Joel on 2/23/2017.
 */
public class DustInSymbol extends DustIOSymbol {

    public final static DustModelHandler.DustTypes curDustType = DustModelHandler.DustTypes.IN;
    public DefaultDustSymbol parent;


    public DustInSymbol(int X, int Z, DefaultDustSymbol newParent) {
        super(X, Z, newParent, curDustType);
        parent = newParent;

    }

    public static final short dustID = ModDust.getNextDustID();
    @Override
    public short getDustID() {
        return dustID;
    }

    public DustInSymbol()
    {
        super(0,0,curDustType);
    }


}
