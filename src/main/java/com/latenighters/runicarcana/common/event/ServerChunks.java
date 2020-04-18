package com.latenighters.runicarcana.common.event;

import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber(Dist.DEDICATED_SERVER)
public class ServerChunks {

    public static ArrayList<IChunk> list = new ArrayList<IChunk>();

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load evt)
    {
        list.add(evt.getChunk());
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload evt)
    {
        list.remove(evt.getChunk());
    }
}
