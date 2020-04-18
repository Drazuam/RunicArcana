package com.latenighters.runicarcana.common.items;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.common.capabilities.ISymbolHandler;
import com.latenighters.runicarcana.common.capabilities.SymbolHandler;
import com.latenighters.runicarcana.common.capabilities.SymbolSyncer;
import com.latenighters.runicarcana.common.setup.ModSetup;
import com.latenighters.runicarcana.common.symbols.DebugSymbol;
import com.latenighters.runicarcana.common.symbols.DrawnSymbol;
import com.latenighters.runicarcana.common.symbols.Symbols;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.PacketDistributor;
import org.jline.utils.Log;

public class ChalkItem extends Item {

    public ChalkItem() {
        super(new Properties().maxStackSize(1).group(ModSetup.ITEM_GROUP));
    }

    private static final int DIRTY_RANGE = 20;

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {

        LazyOptional<ISymbolHandler> symbolOp = context.getWorld().getChunkAt(context.getPos()).getCapability(RunicArcana.SYMBOL_CAP);
        symbolOp.ifPresent(symbols -> {

            if (!context.getWorld().isRemote)
            {
                Chunk chunk = context.getWorld().getChunkAt(context.getPos());
                symbols.addSymbol(new DrawnSymbol(Symbols.DEBUG, context.getPos(), context.getFace()), chunk);

                for(PlayerEntity player : context.getWorld().getPlayers())
                {
                    if(player.chunkCoordX > chunk.getPos().x - DIRTY_RANGE && player.chunkCoordX < chunk.getPos().x + DIRTY_RANGE &&
                       player.chunkCoordZ > chunk.getPos().z - DIRTY_RANGE && player.chunkCoordZ < chunk.getPos().z + DIRTY_RANGE)
                    {
                        SymbolSyncer.SymbolDirtyMessage msg = new SymbolSyncer.SymbolDirtyMessage(chunk.getPos());
                        SymbolSyncer.INSTANCE.sendTo( msg, ((ServerPlayerEntity)(player)).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
                    }
                }
            }
        });

        return super.onItemUse(context);
    }
}
