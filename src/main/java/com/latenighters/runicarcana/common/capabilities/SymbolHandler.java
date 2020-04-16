package com.latenighters.runicarcana.common.capabilities;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.common.symbols.DrawnSymbol;
import com.latenighters.runicarcana.common.symbols.Symbol;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;

import static com.latenighters.runicarcana.RunicArcana.MODID;

public class SymbolHandler implements ISymbolHandler, INBTSerializable<CompoundNBT> {

    private ArrayList<DrawnSymbol> symbols = new ArrayList<DrawnSymbol>();

    public boolean addSymbol(DrawnSymbol toadd)
    {
        if(!isSymbolAt(toadd.getDrawnOn(), toadd.getBlockFace())) {
            symbols.add(toadd);
            return true;
        }
        return false;
    }

    public ArrayList<DrawnSymbol> getSymbols()
    {
        return symbols;
    }

    public ArrayList<DrawnSymbol> getSymbolsAt(BlockPos position)
    {
        ArrayList<DrawnSymbol> ret = new ArrayList<DrawnSymbol>();
        for(DrawnSymbol symbol : symbols)
        {
            if(symbol.getDrawnOn().equals(position))
            {
                ret.add(symbol);
            }
        }
        return ret;
    }

    public DrawnSymbol getSymbolAt(BlockPos position, Direction blockFace)
    {
        ArrayList<DrawnSymbol> filtered = this.getSymbolsAt(position);
        for(DrawnSymbol symbol : filtered)
        {
            if (symbol.getBlockFace().getIndex() == blockFace.getIndex())
            {
                return symbol;
            }
        }
        return null;
    }

    public boolean isSymbolAt(BlockPos position)
    {
        return getSymbolsAt(position).size()>0;
    }

    public boolean isSymbolAt(BlockPos position, Direction blockFace)
    {
        return getSymbolAt(position, blockFace)!=null;
    }

    public SymbolHandler() {

    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT retVal = new CompoundNBT();
        ListNBT symbolList = new ListNBT();
        retVal.putInt("tag_type", symbolList.getTagType());

        int ind = 0;
        for(DrawnSymbol symbol : symbols)
        {
            symbolList.add(ind++, symbol.serializeNBT());
        }

        retVal.put("symbols", symbolList);

        return retVal;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

        if (nbt.hasUniqueId("tag_type") && nbt.hasUniqueId("symbols")) {
            ListNBT symbolList = nbt.getList("symbols", nbt.getInt("tag_type"));

            for (INBT lnbt : symbolList)
            {
                DrawnSymbol symbol = new DrawnSymbol((CompoundNBT)lnbt);
                this.addSymbol(symbol);
            }
        }

    }

    public static class SymbolHandlerFactory implements Callable<ISymbolHandler>{
        @Override
        public ISymbolHandler call() throws Exception {
            return new SymbolHandler();
        }
    }

    public static class Provider implements ICapabilityProvider
    {

        public static final ResourceLocation NAME = new ResourceLocation(MODID, "symbolhandler");
        private LazyOptional<ISymbolHandler> instance = LazyOptional.of(RunicArcana.SYMBOL_CAP::getDefaultInstance);
        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

            return RunicArcana.SYMBOL_CAP.orEmpty(cap,instance);

        }
    }
}
