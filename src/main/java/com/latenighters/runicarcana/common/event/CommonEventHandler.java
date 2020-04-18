package com.latenighters.runicarcana.common.event;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.common.capabilities.SymbolHandler;
import com.latenighters.runicarcana.common.symbols.Symbol;
import com.latenighters.runicarcana.common.symbols.SymbolRegistration;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommonEventHandler {

    @SubscribeEvent
    public void onChunkAttachCapabilitiesEvent(AttachCapabilitiesEvent<Chunk> evt)
    {
        if (!evt.getObject().getCapability(RunicArcana.SYMBOL_CAP).isPresent())
            evt.addCapability(SymbolHandler.NAME, new SymbolHandler());
    }

}
