package com.latenighters.runicarcana.common.capabilities;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.common.event.ClientChunks;
import com.latenighters.runicarcana.common.symbols.DrawnSymbol;
import com.latenighters.runicarcana.common.symbols.Symbol;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandler;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;
import static com.latenighters.runicarcana.RunicArcana.MODID;

@Mod.EventBusSubscriber
public class SymbolHandler implements ISymbolHandler, ICapabilitySerializable<CompoundNBT> {

    private ArrayList<DrawnSymbol> symbols = new ArrayList<DrawnSymbol>();
    public boolean init = false;
    public boolean dirty = false;
    private boolean remote;
    private int ticks_since_last_request = REQUEST_COOLDOWN;
    private static final int REQUEST_COOLDOWN = 20;

    public boolean addSymbol(DrawnSymbol toadd, Chunk addingTo)
    {
        if(toadd==null)return false;
        this.init = true;
        if(!isSymbolAt(toadd.getDrawnOn(), toadd.getBlockFace())) {
            symbols.add(toadd);
            if(addingTo!=null) {
                addingTo.markDirty();
            }
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public static void onChunkTickEvent(TickEvent.ClientTickEvent evt)
    {
        if(Minecraft.getInstance().world == null) return;
        if(evt.side.isClient())
        {
            //request updates to chunk data
            for(int i=0; i<ClientChunks.list.size(); i++)
            {
                if(!SymbolSyncer.canAddMessage())
                    break;
                IChunk chunk = ClientChunks.list.get(i);
                Minecraft.getInstance().world.getChunkProvider().getChunk(chunk.getPos().x,chunk.getPos().z,true).getCapability(RunicArcana.SYMBOL_CAP).ifPresent(symbolHandler ->{
                        symbolHandler.clientSync(chunk.getPos());
                });
            }
        }
    }


    @Override
    public boolean isInit() {
        return init;
    }

    @Override
    public void markDirty() {
        this.dirty = true;
    }

    public void addSymbol(DrawnSymbol symbol) {
        this.init = true;
        addSymbol(symbol,null);
    }

    public ArrayList<DrawnSymbol> getSymbols()
    {
        return symbols;
    }

    public void clientSync(ChunkPos chunkPos)
    {
        if(!this.init || this.dirty)
        {
            if(++ticks_since_last_request>REQUEST_COOLDOWN) {
                SymbolSyncer.addMessageToQueue(new SymbolSyncer.SymbolSyncMessage(chunkPos, new ArrayList<DrawnSymbol>()));
                ticks_since_last_request = 0;
            }
        }
    }

    public void setSynced()
    {
        this.init = true;
        this.dirty = false;
        this.ticks_since_last_request = REQUEST_COOLDOWN;
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
        instance.ifPresent(it ->
        {
            ListNBT symbolList = new ListNBT();
            retVal.putInt("tag_type", symbolList.getTagType());

            int ind = 0;
            for(DrawnSymbol symbol : it.getSymbols())
            {
                symbolList.add(ind++, symbol.serializeNBT());
            }

            retVal.put("symbols", symbolList);

        });
        return retVal;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

        instance.ifPresent(it ->
        {
            if (nbt.contains("tag_type") && nbt.contains("symbols")) {
                ListNBT symbolList = nbt.getList("symbols", 10);

                for (INBT lnbt : symbolList) {
                    DrawnSymbol symbol = new DrawnSymbol((CompoundNBT) lnbt);
                    it.addSymbol(symbol);
                }
            }

        });

    }

    public static class SymbolHandlerFactory implements Callable<ISymbolHandler>{
        @Override
        public ISymbolHandler call() throws Exception {
            return new SymbolHandler();
        }
    }

    public static final ResourceLocation NAME = new ResourceLocation(MODID, "symbolhandler");
    private LazyOptional<ISymbolHandler> instance = LazyOptional.of(RunicArcana.SYMBOL_CAP::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return RunicArcana.SYMBOL_CAP.orEmpty(cap,instance);
    }

    //    public static class Provider implements ICapabilityProvider
//    {
//
//        public static final ResourceLocation NAME = new ResourceLocation(MODID, "symbolhandler");
//        private LazyOptional<ISymbolHandler> instance = LazyOptional.of(RunicArcana.SYMBOL_CAP::getDefaultInstance);
//        @Nonnull
//        @Override
//        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
//
//            return RunicArcana.SYMBOL_CAP.orEmpty(cap,instance);
//
//        }
//
//    }
}
