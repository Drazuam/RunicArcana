package com.drazuam.runicarcana.common.enchantment;

import com.drazuam.runicarcana.common.enchantment.Signals.Signal;
import com.drazuam.runicarcana.common.tileentity.TileEntityChalkBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.DimensionManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Joel on 2/19/2017.
 */
public class DefaultDustSymbol implements IDustSymbol,Serializable {


    public int x; //location on current chalk base - between 0 and 2
    public int z;
    public int f; //Facing Direction
    public DustModelHandler.DustTypes dustType;
    public int blockX;
    public int blockY;
    public int blockZ;
    public int dim;


    public LinkedList<DustConnectionLine> connectionLines = new LinkedList<DustConnectionLine>();
    public LinkedList<DustIOSymbol> ioDusts = new LinkedList<DustIOSymbol>();
    private final ArrayList<Signal> signals = new ArrayList<Signal>();

    public void addSignal(Signal sig)
    {
        signals.add(sig);
    }

    public Signal getSignal(int index)
    {
        if(signals==null)return null;
        return signals.get(index);
    }


    public Signal getNextSignal(DustIOSymbol ioDust)
    {
        Signal currentSig = ioDust.getSignal();
        if(ioDust instanceof DustOutSymbol) {
            int i = 0;
            //if the current signal is null, that means we start from index 0
            if (currentSig != null)
                //get up past the current signal in the list
                while (signals.get(i) != currentSig && i < signals.size()) i = i + 1;
            //save this location
            int curSigInd = i;
            //keep going until we've looped back to this position, or we have found an appropirate signal
            while ((i - curSigInd) < signals.size()) {
                if (signals.get(i % signals.size()) != currentSig && signals.get(i % signals.size()).flow == Signal.SigFlow.OUT&& !isSignalInUse(signals.get(i % signals.size()))) {
                    ioDust.setSignal(signals.get(i % signals.size()));
                    return ioDust.getSignal();
                }
                i=i+1;
            }
        }
        else
        {
            int i = 0;
            //if the current signal is null, that means we start from index 0
            if (currentSig != null)
                //get up past the current signal in the list
                while (signals.get(i) != currentSig && i < signals.size()) i = i + 1;
            //save this location
            int curSigInd = i;
            //keep going until we've looped back to this position, or we have found an appropirate signal
            while ((i - curSigInd) < signals.size()) {
                if (signals.get(i % signals.size()) != currentSig && signals.get(i % signals.size()).flow == Signal.SigFlow.IN && !isSignalInUse(signals.get(i % signals.size()))) {
                    ioDust.setSignal(signals.get(i % signals.size()));
                    return ioDust.getSignal();
                }
                i=i+1;
            }
        }
        return null;
    }

    private Boolean isSignalInUse(Signal sig)
    {
        for(DustIOSymbol ioDust : ioDusts)
        {
            if(ioDust.getSignal()==sig)
                return true;
        }
        return false;

    }


    public void renderConnections( double x, double y, double z)
    {
        for(DustConnectionLine line : connectionLines)
        {
            if(line.parent==this)
            line.render(x,y,z);
        }
    }

    public boolean addConnectionLine(DustIOSymbol destination, ModDust.ConnectionType conType)
    {
        if(destination==null)return false;
        if(destination.getPos().getY()!=this.getPos().getY())return false;
        if(this instanceof DustIOSymbol)
        {
            if (((DustIOSymbol)this).getSignal().type!=destination.getSignal().type)return false;
            System.out.println("adding connection");
            Minecraft mc = Minecraft.getMinecraft();
            DustConnectionLine line = new DustConnectionLine((DustIOSymbol) this, destination, conType);
            connectionLines.add(line);
            line.child.connectionLines.add(line);
            //destination.connectionLines.add(line);
            destination.getParent().updateRendering();
            this.getParent().updateRendering();
            return true;
        }
        else {
            if(this.connectionLines.getFirst().parent.getSignal().type!=destination.getSignal().type)return false;
            System.out.println("adding connection");
            DustConnectionLine line = new DustConnectionLine((DustIOSymbol) this, (DustIOSymbol) destination, conType);
            connectionLines.add(line);
            destination.connectionLines.add(line);
            destination.getParent().updateRendering();
            this.getParent().updateRendering();
            return true;
        }
    }

    public void addIODust(boolean input,int newX, int newZ)
    {
        if (input)
            ioDusts.add(new DustInSymbol(newX, newZ, this));
        else
            ioDusts.add(new DustOutSymbol(newX, newZ, this));

        if(this.getNextSignal(ioDusts.getLast())==null)this.ioDusts.remove(ioDusts.getLast());
    }

    public DefaultDustSymbol getIODust(int X, int Z)
    {
        for(DefaultDustSymbol dust: ioDusts)
        {
            if(dust.x==X&&dust.z==Z)return dust;
        }
        return null;


    }

    public DefaultDustSymbol(int newX, int newZ, int newF, DustModelHandler.DustTypes newDustType, int newBlockX, int newBlockY, int newBlockZ, int newDim)
    {
        //dustID = ModDust.getNextDustID();
        x = newX;
        z = newZ;
        blockX = newBlockX;
        blockY = newBlockY;
        blockZ = newBlockZ;
        dustType = newDustType;
        dim = newDim;


    }

    public DefaultDustSymbol(int X, int Z, int F, TileEntityChalkBase newParent, DustModelHandler.DustTypes newDustType)
    {
        //dustID = ModDust.getNextDustID();
        dustType = newDustType;
        x = X;
        z = Z;
        f = F;
        if(newParent!=null) {
            blockX = newParent.getPos().getX();
            blockY = newParent.getPos().getY();
            blockZ = newParent.getPos().getZ();
            dim = newParent.getWorld().provider.getDimension();
        }
    }

    public TileEntityChalkBase getParent()
    {
        return (TileEntityChalkBase)DimensionManager.getWorld(dim).getTileEntity(new BlockPos(blockX,blockY,blockZ));
    }

    public BlockPos getPos()
    {
        return new BlockPos(blockX,blockY,blockZ);
    }

    public DefaultDustSymbol(int newX, int newZ, int newF, int dim, BlockPos newBlockPos, DustModelHandler.DustTypes newDustType) {
        //dustID = ModDust.getNextDustID();
        x = newX;
        z = newZ;
        f = newF;
        this.dim = dim;
        blockX = newBlockPos.getX();
        blockY = newBlockPos.getY();
        blockZ = newBlockPos.getZ();
        dustType = newDustType;
    }

    @Override
    public DefaultDustSymbol setXZFB(int newX, int newZ, int newF, int dim, BlockPos newBlockPos) {

        x = newX;
        z = newZ;
        f = newF;
        this.dim = dim;
        blockX = newBlockPos.getX();
        blockY = newBlockPos.getY();
        blockZ = newBlockPos.getZ();
        return this;
    }


    public DefaultDustSymbol setXZFB(int newX, int newZ, int newF, TileEntityChalkBase newParent) {
        x = newX;
        z = newZ;
        f = newF;
        if(newParent!=null) {
            blockX = newParent.getPos().getX();
            blockY = newParent.getPos().getY();
            blockZ = newParent.getPos().getZ();
            dim = newParent.getWorld().provider.getDimension();
        }
        return this;
    }


    public boolean checkOccupied(int checkX, int checkZ)
    {
        int xRadius = (dustType.getSize()-1)/2;
        int zRadius = (dustType.getSize()-1)/2;
        if (Math.abs(checkX-x)<=xRadius&&Math.abs(checkZ-z)<=zRadius)
            return true;
        return false;
    }

    public IBakedModel getBakedModel()
    {
        return dustType.getBakedModel();
    }

    public boolean willAccept(ItemStack stack)
    {
        return false;
    }

    @Override
    public ITextComponent getDisplayName(String name) {
        return name==null ? new TextComponentTranslation("dust."+dustType.defaultName+".name") : new TextComponentTranslation("dust."+name+".name");
    }

    public ITextComponent getDisplayName()
    {
        return getDisplayName(null);
    }

    public void doAnimation()
    {

    }

    @Override
    public short getDustID() {
        return 0;
    }

    @Override
    public String getVariable() {
        return null;
    }
}
