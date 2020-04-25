package com.latenighters.runicarcana.common.symbols.backend.capability;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.common.items.ChalkItem;
import com.latenighters.runicarcana.common.symbols.backend.*;
import com.latenighters.runicarcana.common.symbols.Symbols;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.RegistryManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

import static com.latenighters.runicarcana.RunicArcana.MODID;

public class SymbolSyncer
{

    private static final int DIRTY_RANGE = 15;

    private static final String PROTOCOL_VERSION = "1";
    private static final int MESSAGE_QUEUE_SIZE = 100;
    private static final int PACKETS_PER_TICK = 20;
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MODID, "symbol_synchronization"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static final Queue<SymbolSyncMessage> messageQueue = new LinkedList<SymbolSyncMessage>();

    public static boolean canAddMessage()
    {
        return messageQueue.size()<MESSAGE_QUEUE_SIZE;
    }

    public static void addMessageToQueue(SymbolSyncMessage msg)
    {
        if(messageQueue.size()<MESSAGE_QUEUE_SIZE) {
            boolean found = false;
            for(SymbolSyncMessage message : messageQueue)
            {
                if(msg.chunkPos.equals(message.chunkPos)) {
                    found = true;
                    break;
                }
            }
            if(!found)
                messageQueue.add(msg);
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onChunkTickEvent(TickEvent.ClientTickEvent evt) {
        int packets_per_tick = PACKETS_PER_TICK;
        while(messageQueue.size()>0 && packets_per_tick-->0)
            SymbolSyncer.INSTANCE.sendToServer(messageQueue.poll());
    }

    public static void registerPackets()
    {
        int ind = 0;
        INSTANCE.registerMessage(ind++, SymbolSyncMessage.class,
                SymbolSyncMessage::encode,
                SymbolSyncMessage::decode,
                SymbolSyncMessage::handle);

        INSTANCE.registerMessage(ind++, SymbolDirtyMessage.class,
                SymbolDirtyMessage::encode,
                SymbolDirtyMessage::decode,
                SymbolDirtyMessage::handle);

        INSTANCE.registerMessage(ind++, ChalkItem.ChalkSyncMessage.class,
                ChalkItem.ChalkSyncMessage::encode,
                ChalkItem.ChalkSyncMessage::decode,
                ChalkItem.ChalkSyncMessage::handle);

        INSTANCE.registerMessage(ind++, AddWorkMessage.class,
                AddWorkMessage::encode,
                AddWorkMessage::decode,
                AddWorkMessage::handle);

        INSTANCE.registerMessage(ind++, SymbolLinkMessage.class,
                SymbolLinkMessage::encode,
                SymbolLinkMessage::decode,
                SymbolLinkMessage::handle);

    }

    public static class AddWorkMessage
    {
        public int work;
        public DrawnSymbol symbol;
        public ChunkPos chunkPos;

        public AddWorkMessage(int work, DrawnSymbol symbol, ChunkPos chunkPos)
        {
            this.work = work;
            this.symbol = symbol;
            this.chunkPos = chunkPos;
        }

        public static void encode(final AddWorkMessage msg, final PacketBuffer buf)
        {
            buf.writeInt(msg.work);
            buf.writeInt(msg.chunkPos.x);
            buf.writeInt(msg.chunkPos.z);
            buf.writeBlockPos(msg.symbol.getDrawnOn());
            buf.writeInt(msg.symbol.getBlockFace().getIndex());
        }

        public static AddWorkMessage decode(final PacketBuffer buf)
        {
            int work = buf.readInt();
            ChunkPos chunkPos = new ChunkPos(buf.readInt(),buf.readInt());
            DrawnSymbol symbol = null;
            if(RunicArcana.proxy.getWorld()!=null)
                symbol = new DrawnSymbol(Symbols.DEBUG, buf.readBlockPos(), Direction.byIndex(buf.readInt()),RunicArcana.proxy.getWorld().getChunk(chunkPos.x, chunkPos.z));

            return new AddWorkMessage(work, symbol, chunkPos);
        }

        public static void handle(final AddWorkMessage msg, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            if (context.getDirection().equals(NetworkDirection.PLAY_TO_CLIENT)) {
                context.enqueueWork(() -> {
                    World world = RunicArcana.proxy.getWorld();
                    Chunk chunk = world.getChunkProvider().getChunk(msg.chunkPos.x, msg.chunkPos.z, true);

                    chunk.getCapability(RunicArcana.SYMBOL_CAP).ifPresent(symbolHandler -> {
                        DrawnSymbol symbol = symbolHandler.getSymbolAt(msg.symbol.getDrawnOn(),msg.symbol.getBlockFace());
                        if(symbol!=null)
                            symbol.applyWork(msg.work);
                    });
                });
                context.setPacketHandled(true);
            }
        }
    }

    public static class SymbolDirtyMessage
    {
        public ChunkPos chunkPos;

        public SymbolDirtyMessage(ChunkPos chunkPos)
        {
            this.chunkPos = chunkPos;
        }

        public SymbolDirtyMessage(Chunk chunk)
        {
            this.chunkPos = chunk.getPos();
        }

        public static void encode(final SymbolDirtyMessage msg, final PacketBuffer buf)
        {
            buf.writeInt(msg.chunkPos.x);
            buf.writeInt(msg.chunkPos.z);
        }

        public static SymbolDirtyMessage decode(final PacketBuffer buf)
        {
            ChunkPos chunkPos = new ChunkPos(buf.readInt(),buf.readInt());
            return new SymbolDirtyMessage(chunkPos);
        }

        public static void handle(final SymbolDirtyMessage msg, final Supplier<NetworkEvent.Context> contextSupplier)
        {
            final NetworkEvent.Context context = contextSupplier.get();
            if (context.getDirection().equals(NetworkDirection.PLAY_TO_CLIENT))
            {
                context.enqueueWork(() -> {
                    World world = RunicArcana.proxy.getWorld();
                    Chunk chunk = world.getChunkProvider().getChunk(msg.chunkPos.x, msg.chunkPos.z, true);

                    chunk.getCapability(RunicArcana.SYMBOL_CAP).ifPresent(symbolHandler ->{
                        symbolHandler.markDirty();
                    });
                });
                context.setPacketHandled(true);
            }
            else if (context.getDirection().equals(NetworkDirection.PLAY_TO_SERVER))
            {
                context.setPacketHandled(true);
            }
        }

    }

    public static class SymbolLinkMessage
    {
        public IFunctionalObject linkingFrom;
        public IFunctionalObject linkingTo;
        public HashableTuple<String, DataType> input;
        public String outputName;
        public String outputType;

        public SymbolLinkMessage(IFunctionalObject linkingFrom, IFunctionalObject linkingTo, HashableTuple<String, DataType> input, String outputName, String outputType) {
            this.linkingFrom = linkingFrom;
            this.linkingTo = linkingTo;
            this.input = input;
            this.outputName = outputName;
            this.outputType = outputType;
        }

        public SymbolLinkMessage(IFunctionalObject linkingFrom, IFunctionalObject linkingTo, HashableTuple<String, DataType> input, IFunctional output) {
            this.linkingFrom = linkingFrom;
            this.linkingTo = linkingTo;
            this.input = input;
            this.outputName = output.getName();
            this.outputType = output.getOutputType().name;
        }

        public static void encode(final SymbolLinkMessage msg, final PacketBuffer buf)
        {
            buf.writeString(msg.linkingFrom.getObjectType());
            buf.writeCompoundTag(msg.linkingFrom.basicSerializeNBT());
            buf.writeString(msg.linkingFrom.getObjectType());
            buf.writeCompoundTag(msg.linkingTo.basicSerializeNBT());
            buf.writeString(msg.input.getA());
            buf.writeString(msg.input.getB().name);
            buf.writeString(msg.outputName);
            buf.writeString(msg.outputType);
        }

        public static SymbolLinkMessage decode(final PacketBuffer buf)
        {
            IFunctionalObject linkingFrom = FunctionalTypeRegister.getFunctionalObject(buf.readString(100));
            linkingFrom.deserializeNBT(buf.readCompoundTag());
            IFunctionalObject linkingTo = FunctionalTypeRegister.getFunctionalObject(buf.readString(100));
            linkingTo.deserializeNBT(buf.readCompoundTag());

            HashableTuple<String, DataType> input = new HashableTuple<>(buf.readString(100),DataType.getDataType(buf.readString(100)));
            String outputName = buf.readString(100);
            String outputType = buf.readString(100);

            return new SymbolLinkMessage(linkingFrom,linkingTo,input,outputName,outputType);
        }

        public static void handle(final SymbolLinkMessage msg, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            if (context.getDirection().equals(NetworkDirection.PLAY_TO_SERVER)) {

                context.setPacketHandled(true);
                ServerPlayerEntity sender = context.getSender();

                IFunctionalObject realLinkingFrom = null;
                IFunctionalObject realLinkingTo = null;
                IChunk chunk = sender.world.getChunk(msg.linkingFrom.getBlockPos());
                if (chunk instanceof Chunk)
                    realLinkingFrom = msg.linkingFrom.findReal((Chunk) chunk);
                chunk = sender.world.getChunk(msg.linkingTo.getBlockPos());
                if (chunk instanceof Chunk)
                    realLinkingTo = msg.linkingTo.findReal((Chunk) chunk);

                if (realLinkingFrom == null || realLinkingTo == null) return;

                HashableTuple<String, DataType> realInput = null;
                for (HashableTuple<String, DataType> input : realLinkingFrom.getInputs()) {
                    if (input.getA().equals(msg.input.getA()) && input.getB() == msg.input.getB()) {
                        realInput = input;
                        break;
                    }
                }

                IFunctional realOutput = null;
                for (IFunctional output : realLinkingTo.getOutputs()) {
                    if (output.getName().equals(msg.outputName) && output.getOutputType().name.equals(msg.outputType)) {
                        realOutput = output;
                        break;
                    }
                }

                if (realInput == null || realOutput == null) return;

                //TODO inputs not linking properly with this
                realLinkingFrom.getInputLinks().put(realInput, new HashableTuple<>(realLinkingTo, realOutput));

                for(PlayerEntity player : ((Chunk)chunk).getWorld().getPlayers())
                {
                    if(player.chunkCoordX > chunk.getPos().x - DIRTY_RANGE && player.chunkCoordX < chunk.getPos().x + DIRTY_RANGE &&
                            player.chunkCoordZ > chunk.getPos().z - DIRTY_RANGE && player.chunkCoordZ < chunk.getPos().z + DIRTY_RANGE)
                    {
                        SymbolSyncer.INSTANCE.sendTo( msg, ((ServerPlayerEntity)(player)).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
                    }
                }

            }
            else if(context.getDirection().equals(NetworkDirection.PLAY_TO_CLIENT))
            {
                context.setPacketHandled(true);
                PlayerEntity sender = RunicArcana.proxy.getPlayer();

                IFunctionalObject realLinkingFrom = null;
                IFunctionalObject realLinkingTo = null;
                IChunk chunk = sender.world.getChunk(msg.linkingFrom.getBlockPos());
                if (chunk instanceof Chunk)
                    realLinkingFrom = msg.linkingFrom.findReal((Chunk) chunk);
                chunk = sender.world.getChunk(msg.linkingTo.getBlockPos());
                if (chunk instanceof Chunk)
                    realLinkingFrom = msg.linkingTo.findReal((Chunk) chunk);

                if (realLinkingFrom == null || realLinkingTo == null) return;

                HashableTuple<String, DataType> realInput = null;
                for (HashableTuple<String, DataType> input : realLinkingFrom.getInputs()) {
                    if (input.getA().equals(msg.input.getA()) && input.getB() == msg.input.getB()) {
                        realInput = input;
                    }
                }

                IFunctional realOutput = null;
                for (IFunctional output : realLinkingTo.getOutputs()) {
                    if (output.getName().equals(msg.outputName) && output.getOutputType().name.equals(msg.outputType))
                        realOutput = output;
                }

                if (realInput == null || realOutput == null) return;

                realLinkingFrom.getInputLinks().put(realInput, new HashableTuple<>(realLinkingTo, realOutput));
            }
        }
    }

    public static class SymbolSyncMessage
    {
        public ChunkPos chunkPos;
        public ArrayList<DrawnSymbol> symbols;

        public SymbolSyncMessage(Chunk chunk, ArrayList<DrawnSymbol> symbols) {
            this.chunkPos = chunk.getPos();
            this.symbols = symbols;
        }
        public SymbolSyncMessage(ChunkPos chunkPos, ArrayList<DrawnSymbol> symbols) {
            this.chunkPos = chunkPos;
            this.symbols = symbols;
        }

        public static void encode(final SymbolSyncMessage msg, final PacketBuffer buf)
        {
            buf.writeInt(msg.chunkPos.x);
            buf.writeInt(msg.chunkPos.z);
            buf.writeInt(msg.symbols.size());

            ArrayList<String> symbolDict = new ArrayList<String>();

            for(DrawnSymbol symbol : msg.symbols)
            {
                if(!symbolDict.contains(symbol.getSymbol().getRegistryName().toString())){
                    symbolDict.add(symbol.getSymbol().getRegistryName().toString());
                }
            }

            buf.writeInt(symbolDict.size());
            for(int i=0; i<symbolDict.size(); i++)
            {
                buf.writeString(symbolDict.get(i));
            }

            for(DrawnSymbol symbol : msg.symbols)
            {
                buf.writeInt(symbolDict.indexOf(symbol.getSymbol().getRegistryName().toString()));
                symbol.encode(buf);
            }

        }

        public static SymbolSyncMessage decode(final PacketBuffer buf)
        {
            ArrayList<DrawnSymbol> symbols = new ArrayList<DrawnSymbol>();
            ChunkPos chunkPos = new ChunkPos(buf.readInt(),buf.readInt());

            int size = buf.readInt();

            ArrayList<String> symbolDict = new ArrayList<String>();
            int dictSize = buf.readInt();

            for (int i=0; i<dictSize; i++)
            {
                symbolDict.add(buf.readString(100));
            }

            for(int i=0; i<size; i++)
            {
                Symbol symbol = RegistryManager.ACTIVE.getRegistry(Symbol.class).getValue(new ResourceLocation(symbolDict.get(buf.readInt())));
                symbols.add(new DrawnSymbol(buf, symbol));

            }

            return new SymbolSyncMessage(chunkPos, symbols);
        }

        public static void handle(final SymbolSyncMessage msg, final Supplier<NetworkEvent.Context> contextSupplier)
        {
            final NetworkEvent.Context context = contextSupplier.get();
            if (context.getDirection().equals(NetworkDirection.PLAY_TO_CLIENT))
            {
                context.enqueueWork(() -> {

                    //Chunk chunk = sender.getEntityWorld().getChunk(msg.chunkPos.x,  msg.chunkPos.z);
                    World world = RunicArcana.proxy.getWorld();
                    Chunk chunk = world.getChunkProvider().getChunk(msg.chunkPos.x, msg.chunkPos.z, true);

                    chunk.getCapability(RunicArcana.SYMBOL_CAP).ifPresent(symbolHandler ->{
                        symbolHandler.synchronizeSymbols(msg.symbols);
                        ((SymbolHandler)symbolHandler).setSynced();
                    });
                });
                context.setPacketHandled(true);
            }
            else if (context.getDirection().equals(NetworkDirection.PLAY_TO_SERVER))
            {
                context.enqueueWork(() -> {
                    final ServerPlayerEntity sender = context.getSender();
                    if (sender == null) {
                        return;
                    }
//                    Chunk chunk = sender.world.getChunk(msg.chunkPos.x, msg.chunkPos.z);
                    Chunk chunk = sender.world.getChunkProvider().getChunk(msg.chunkPos.x, msg.chunkPos.z,true);
                    chunk.getCapability(RunicArcana.SYMBOL_CAP).ifPresent(symbolHandler -> {
                        ArrayList<DrawnSymbol> symbols_ = null;
                        symbols_ = symbolHandler.getSymbols();
                        if(symbols_ == null)
                            symbols_ = new ArrayList<DrawnSymbol>();
                        SymbolSyncMessage reply_msg = new SymbolSyncMessage(chunk, symbols_);

                        INSTANCE.reply(reply_msg, context);
                    });


                });
                context.setPacketHandled(true);
            }
        }

    }
}
