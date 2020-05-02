package com.latenighters.runicarcana.common.event;

import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientChunks {

    public static ArrayList<Chunk> list = new ArrayList<Chunk>();

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load evt)
    {
        if (evt.getChunk() instanceof Chunk && evt.getWorld().isRemote() && !list.contains(evt.getChunk()))
            list.add((Chunk)(evt.getChunk()));
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload evt)
    {
        if (evt.getChunk() instanceof Chunk && evt.getWorld().isRemote())
            list.remove((Chunk)(evt.getChunk()));
    }

    public static ArrayList<Chunk> getLoadedChunks()
    {
        return list;
    }

//    public static AtomicReferenceArray<Chunk> getLoadedChunks(ClientWorld world){
//
//        //field_217256_d
//        AtomicReferenceArray<Chunk>  loadedChunks = null;
//        if (world == null) return null;
//
//        Object landing  = null;
//        Object landing2 = null;
//        try {
//            landing = (ObfuscationReflectionHelper.findField(ClientChunkProvider.class, "field_217256_d.field_217195_b").get(world.getChunkProvider()));
//            landing2 = ObfuscationReflectionHelper.findField(ClientChunkProvider$ChunkArray.class,"field_217195_b").get(landing);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
////        Object landing2 = ObfuscationReflectionHelper.findField(ClientChunkProvider.class,"field_217195_b");
////        Object landing2 = ObfuscationReflectionHelper.getPrivateValue(landing.getClass(),landing, "field_217195_b");
//
//        if (landing2 instanceof AtomicReferenceArray) {
//            loadedChunks = (AtomicReferenceArray<Chunk>) landing;
//        }
//
//        return loadedChunks;
//    }
}
