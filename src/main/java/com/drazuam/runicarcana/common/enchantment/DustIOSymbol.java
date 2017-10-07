package com.drazuam.runicarcana.common.enchantment;

import com.drazuam.runicarcana.api.enchantment.DefaultDustSymbol;
import com.drazuam.runicarcana.common.enchantment.Signals.Signal;

/**
 * Created by Joel on 2/23/2017.
 */
public abstract class DustIOSymbol extends DefaultDustSymbol {

    public DefaultDustSymbol parent;
    public short curDustType;
    private Signal signal = null;

    public DustIOSymbol(int X, int Z, DefaultDustSymbol newParent, short newCurDustType) {
        super(X, Z, 0, newParent.getParent(), newCurDustType);
        parent = newParent;
        curDustType = newCurDustType;
        //try to get the next symbol for this dust.  If one doesn't exist, remove the dust before it's really created
        //if(parent.getNextSignal(this)==null)parent.ioDusts.remove(this);

    }

    public DustIOSymbol(int X, int Z, short newCurDustType) {
        super(X, Z, 0, null, newCurDustType);
        curDustType = newCurDustType;
        //if(parent.getNextSignal(this)==null)parent.ioDusts.remove(this);
    }

    public Signal getSignal()
    {
        return signal;
    }

    public boolean setSignal(Signal newSignal)
    {
        signal = newSignal;
        return true;
    }

    public void removeConnections()
    {
        for(DustConnectionLine line : connectionLines)
        {
            line.child.connectionLines.remove(line);
            line.child.parent.getParent().updateRendering();
            line.parent.connectionLines.remove(line);
            line.parent.parent.getParent().updateRendering();
        }
    }

    @Override
    public int getSize() {
        return 1;
    }
}
