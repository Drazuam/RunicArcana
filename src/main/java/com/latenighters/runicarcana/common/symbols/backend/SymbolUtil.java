package com.latenighters.runicarcana.common.symbols.backend;

import com.latenighters.runicarcana.RunicArcana;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;

import java.lang.ref.Reference;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class SymbolUtil {

    //client-side only
    public static IFunctionalObject getLookedFunctionalObject(){
        if(RunicArcana.proxy.getWorld()==null)return null;
        AtomicReference<IFunctionalObject> symbol = new AtomicReference<>();
        symbol.set(null);

        //first check for a symbol that the player is looking at
        PlayerEntity player = RunicArcana.proxy.getPlayer();
        //BlockPos blockPos = RunicArcana.proxy.getWorld().rayTraceBlocks(new RayTraceContext(player.getEyePosition(partialTicks), player.getLook(partialTicks)*player.reach, ));
        if (Minecraft.getInstance().objectMouseOver != null && Minecraft.getInstance().objectMouseOver.getType() == RayTraceResult.Type.BLOCK) {
            BlockRayTraceResult blockRayTrace = (BlockRayTraceResult)Minecraft.getInstance().objectMouseOver;
            IChunk chunk = RunicArcana.proxy.getWorld().getChunk(blockRayTrace.getPos());

            if(chunk instanceof Chunk)
                ((Chunk)chunk).getCapability(RunicArcana.SYMBOL_CAP).ifPresent(symbolHandler ->{
                    symbol.set(symbolHandler.getSymbolAt(blockRayTrace.getPos(), blockRayTrace.getFace()));
                });
        }
        return symbol.get();
    }

    public static void parseArguments(List<HashableTuple<String, Object>> args, Map<String, AtomicReference<Object>> objectsToFill)
    {
        args.forEach(arg ->{
            if(objectsToFill.containsKey(arg.getA()) && arg.getB().getClass().isAssignableFrom(objectsToFill.get(arg.getA()).getClass()))
                objectsToFill.get(arg.getA()).set(arg.getB());
        });
    }
}
