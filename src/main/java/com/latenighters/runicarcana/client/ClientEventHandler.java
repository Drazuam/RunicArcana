package com.latenighters.runicarcana.client;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.latenighters.runicarcana.client.render.SymbolRenderer.renderSymbols;

public class ClientEventHandler {

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent evt)
    {
        renderSymbols(evt);
    }

}
