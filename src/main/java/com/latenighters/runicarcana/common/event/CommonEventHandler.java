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
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
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
        if(evt.side.isClient())
            return;

        Iterable<ChunkHolder> loadedChunks = ServerChunks.getLoadedChunks((ServerWorld) evt.world);
        if(loadedChunks==null) return;

        int chunksTicked = 0;

        //give each symbol handler a tick
        for(ChunkHolder chunkHolder : loadedChunks)
        {
            Chunk chunk = chunkHolder.getChunkIfComplete();
            if(chunk==null)continue;

            chunksTicked++;
            chunk.getCapability(RunicArcana.SYMBOL_CAP).ifPresent(symbolHandler ->{
                symbolHandler.tick(evt.world, chunk);
            });
        }
        chunksTicked++;
    }
}
