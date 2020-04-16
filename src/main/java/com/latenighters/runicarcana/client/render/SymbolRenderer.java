package com.latenighters.runicarcana.client.render;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.common.capabilities.ISymbolHandler;
import com.latenighters.runicarcana.common.capabilities.SymbolHandler;
import com.latenighters.runicarcana.common.symbols.DebugSymbol;
import com.latenighters.runicarcana.common.symbols.Symbol;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Matrix3f;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.util.LazyOptional;

public class SymbolRenderer {

    //range to render symbols in chunks
    private static int symbol_render_range = 1;

    public static void renderSymbols(RenderWorldLastEvent evt)
    {
        PlayerEntity player = Minecraft.getInstance().player;
        Chunk homeChunk = player.world.getChunkAt(player.getPosition());

        LazyOptional<ISymbolHandler> symbolOp = homeChunk.getCapability(RunicArcana.SYMBOL_CAP);
        symbolOp.ifPresent(symbols -> {
            evt.getMatrixStack();



        });

    }

    public static void renderQuad(Symbol symbol)
    {

    }
}
