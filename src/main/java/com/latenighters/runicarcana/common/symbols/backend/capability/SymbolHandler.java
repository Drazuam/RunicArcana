package com.latenighters.runicarcana.common.symbols.backend.capability;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.common.event.ClientChunks;
import com.latenighters.runicarcana.common.symbols.backend.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private static final int DIRTY_RANGE = 15;

    private static HashMap<HashableTuple<IFunctionalObject,IFunctional>, Object> resolvedFunctions = new HashMap<>();

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

    public void synchronizeSymbols(ArrayList<DrawnSymbol> symbols)
    {
        //first try adding all the symbols in
        for(DrawnSymbol symbol : symbols)
        {
            this.addSymbol(symbol);
        }

        //if we're at the same size, then we're good.  we dont need to remove anything
        if(symbols.size()==this.symbols.size())return;

        //otherwise we need to trim down
        this.symbols.removeIf(symbol ->
        {
            boolean found = false;
            for(DrawnSymbol sym :symbols)
            {
                if(sym.getBlockFace()==symbol.getBlockFace() && sym.getDrawnOn().equals(symbol.getDrawnOn()))
                {
                    found = true;
                    break;
                }
            }
            return  !found;
        });

    }

    @SubscribeEvent
    public static void onBlockBreakEvent(BlockEvent.BreakEvent event)
    {
        if(!event.getWorld().isRemote()) {
            ChunkPos chunkPos = new ChunkPos(event.getPos());
            Chunk chunk = ((ServerWorld)event.getWorld()).getChunkProvider().getChunk(chunkPos.x, chunkPos.z, true);
            chunk.getCapability(RunicArcana.SYMBOL_CAP).ifPresent(symbolHandler -> {
                boolean broken = false;
                for(DrawnSymbol symbol : symbolHandler.getSymbolsAt(event.getPos()))
                {
                    broken = true;
                    symbolHandler.getSymbols().remove(symbol);
                    symbolHandler.markDirty();
                }
                if(broken)
                {
                    SymbolSyncer.SymbolSyncMessage msg = new SymbolSyncer.SymbolSyncMessage(chunk, symbolHandler.getSymbols());
                    for(PlayerEntity player : event.getWorld().getPlayers())
                    {
                        if(player.chunkCoordX > chunk.getPos().x - DIRTY_RANGE && player.chunkCoordX < chunk.getPos().x + DIRTY_RANGE &&
                                player.chunkCoordZ > chunk.getPos().z - DIRTY_RANGE && player.chunkCoordZ < chunk.getPos().z + DIRTY_RANGE)
                        {
                            SymbolSyncer.INSTANCE.sendTo( msg, ((ServerPlayerEntity)(player)).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
                        }
                    }
                }
            });
        }

    }

    @SubscribeEvent
    public static void onChunkTickEvent(TickEvent.ClientTickEvent evt)
    {
        if(Minecraft.getInstance().world == null) return;
        resolvedFunctions.clear();
        if(evt.side.isClient())
        {
            //request updates to chunk data
            ArrayList<Chunk> chunks = ClientChunks.getLoadedChunks();
            for(Chunk chunk : chunks)
            {
                if(!SymbolSyncer.canAddMessage())
                    break;
                if(chunk == null)continue;
                chunk.getCapability(RunicArcana.SYMBOL_CAP).ifPresent(symbolHandler ->{
                        symbolHandler.clientSync(chunk.getPos());
                        symbolHandler.tick(Minecraft.getInstance().world, chunk);
                });
            }
        }
    }

    @Override
    public void tick(World world, Chunk chunk) {

        if(chunk instanceof EmptyChunk) return;

        //tick each symbol individually
        for(DrawnSymbol symbol : symbols)
        {
            symbol.tick(world,chunk);

            symbol.getFunctions().forEach(function ->{
                resolveOutputInWorld(new HashableTuple<>(symbol, function), chunk);
            });
        }

    }

    public static Object resolveOutputInWorld(HashableTuple<IFunctionalObject,IFunctional> functionToRun, Chunk chunk)
    {
        List<HashableTuple<String, Object>> args = new ArrayList<>();
        Map<HashableTuple<String, DataType>, HashableTuple<IFunctionalObject,IFunctional>> inputLinks = functionToRun.getA().getInputLinks();

        functionToRun.getB().getRequiredInputs().forEach( input -> {
            if(inputLinks.containsKey(input)) {
                if(resolvedFunctions.containsKey(inputLinks.get(input)))
                    args.add(new HashableTuple<>(input.getA(), resolvedFunctions.get(inputLinks.get(input))));
                else {
                    Object resolution =  resolveOutputInWorld(inputLinks.get(input), chunk);
                    args.add(new HashableTuple<>(input.getA(),resolution));
                    resolvedFunctions.put(inputLinks.get(input),resolution);
                }
            }
        });

        return functionToRun.getB().executeInWorld(functionToRun.getA(), chunk, args);
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
