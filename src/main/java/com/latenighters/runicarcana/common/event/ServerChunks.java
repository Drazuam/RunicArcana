package com.latenighters.runicarcana.common.event;

import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
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

    static Iterable<ChunkHolder> getLoadedChunks(ServerWorld world) {
        Iterable<ChunkHolder> loadedChunks = null;
        if (world == null) return null;
        try {
            Object landing = ObfuscationReflectionHelper.findMethod(ChunkManager.class, "func_223491_f").invoke(world.getChunkProvider().chunkManager);
            if (landing instanceof Iterable) {
                loadedChunks = (Iterable<ChunkHolder>) landing;
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return loadedChunks;
    }
}
