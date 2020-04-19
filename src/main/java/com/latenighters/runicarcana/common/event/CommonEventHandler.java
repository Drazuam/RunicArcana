package com.latenighters.runicarcana.common.event;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.common.capabilities.SymbolHandler;
import com.latenighters.runicarcana.common.capabilities.SymbolSyncer;
import com.latenighters.runicarcana.common.symbols.Symbol;
import com.latenighters.runicarcana.common.symbols.SymbolRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.DropperTileEntity;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;

public class CommonEventHandler {

    @SubscribeEvent
    public void onChunkAttachCapabilitiesEvent(AttachCapabilitiesEvent<Chunk> evt)
    {
        if (!evt.getObject().getCapability(RunicArcana.SYMBOL_CAP).isPresent())
            evt.addCapability(SymbolHandler.NAME, new SymbolHandler());
    }

    @SubscribeEvent
    public void onChunkTickEvent(TickEvent.WorldTickEvent evt)
    {
        ArrayList<IChunk> chunks;
        if(evt.side.isClient())
        {
            return;
        }else{

            if(ServerChunks.list.size()==0)
            {
                //we're running on singleplayer
                chunks = ClientChunks.list;
            }
            else
            {
                //we're running on a dedicated server
                chunks = ServerChunks.list;
            }
        }

        //((ServerWorld)evt.world).getChunkProvider()

        //give each symbol handler a tick
        for(int i=0; i<chunks.size(); i++)
        {
            IChunk chunk = ClientChunks.list.get(i);
            if (Minecraft.getInstance().world != null) {
                Minecraft.getInstance().world.getChunkProvider().getChunk(chunk.getPos().x,chunk.getPos().z,true).getCapability(RunicArcana.SYMBOL_CAP).ifPresent(symbolHandler ->{
                    symbolHandler.tick(evt.world, chunk);
                });
            }
        }


    }

}
