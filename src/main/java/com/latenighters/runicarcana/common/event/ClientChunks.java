package com.latenighters.runicarcana.common.event;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientChunks {

    public static ArrayList<IChunk> list = new ArrayList<IChunk>();

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load evt)
    {
        if (evt.getChunk() instanceof Chunk)
            list.add(evt.getChunk());
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload evt)
    {
        if (evt.getChunk() instanceof Chunk)
            list.remove(evt.getChunk());
    }
}
