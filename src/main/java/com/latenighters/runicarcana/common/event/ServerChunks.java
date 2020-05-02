package com.latenighters.runicarcana.common.event;

import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;

@Mod.EventBusSubscriber(Dist.DEDICATED_SERVER)
public class ServerChunks {

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
