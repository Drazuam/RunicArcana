package com.drazuam.omnimancy.common.enchantment;

/**
 * Created by Joel on 2/23/2017.
 */
public class DustOutSymbol extends DustIOSymbol {



    public final static DustModelHandler.DustTypes curDustType = DustModelHandler.DustTypes.OUT;
    public DefaultDustSymbol parent;


    public DustOutSymbol(int X, int Z, DefaultDustSymbol newParent) {
        super(X, Z, newParent, curDustType);
        parent = newParent;

    }

    public static final short dustID = ModDust.getNextDustID();
    @Override
    public short getDustID() {
        return dustID;
    }

    public DustOutSymbol()
    {
        super(0,0,curDustType);
    }

}

