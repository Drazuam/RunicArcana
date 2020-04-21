package com.latenighters.runicarcana.common.items;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.common.symbols.backend.capability.ISymbolHandler;
import com.latenighters.runicarcana.common.symbols.backend.capability.SymbolSyncer;
import com.latenighters.runicarcana.common.setup.ModSetup;
import com.latenighters.runicarcana.common.symbols.backend.DrawnSymbol;
import com.latenighters.runicarcana.common.symbols.backend.Symbol;
import com.latenighters.runicarcana.common.symbols.Symbols;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.RegistryManager;

import java.util.function.Supplier;

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
                Symbol symbolToDraw = null;
                    symbolToDraw = RegistryManager.ACTIVE.getRegistry(Symbol.class)
                            .getValue(new ResourceLocation(context.getItem().getOrCreateTag().contains("selected_symbol")
                                    ? context.getItem().getOrCreateTag().getString("selected_symbol") : Symbols.DEBUG.getRegistryName().toString()));

                symbols.addSymbol(new DrawnSymbol(symbolToDraw, context.getPos(), context.getFace(),chunk), chunk);

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

    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putString("selected_symbol",Symbols.DEBUG.getRegistryName().toString());
        super.onCreated(stack, worldIn, playerIn);
    }

    public static class ChalkSyncMessage
    {
        public Symbol selectedSymbol;
        public Hand hand;

        public ChalkSyncMessage(Symbol selectedSymbol, Hand hand)
        {
            this.selectedSymbol = selectedSymbol;
            this.hand = hand;
        }

        public static void encode(final ChalkSyncMessage msg, final PacketBuffer buf)
        {
            buf.writeString(msg.selectedSymbol.getRegistryName().toString());
            buf.writeBoolean(msg.hand.equals(Hand.MAIN_HAND));
        }

        public static ChalkSyncMessage decode(final PacketBuffer buf)
        {
            Symbol symbol = RegistryManager.ACTIVE.getRegistry(Symbol.class).getValue(new ResourceLocation(buf.readString()));
            Hand hand = buf.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND;

            return new ChalkSyncMessage(symbol, hand);
        }

        public static void handle(final ChalkSyncMessage msg, final Supplier<NetworkEvent.Context> contextSupplier)
        {
            final NetworkEvent.Context context = contextSupplier.get();
            if (context.getDirection().equals(NetworkDirection.PLAY_TO_CLIENT))
            {

                context.setPacketHandled(true);
            }
            else if (context.getDirection().equals(NetworkDirection.PLAY_TO_SERVER))
            {
                if(context.getSender()==null)return;
                ItemStack chalk = context.getSender().getHeldItem(msg.hand);
                chalk.getOrCreateTag().putString("selected_symbol", msg.selectedSymbol.getRegistryName().toString());

                context.setPacketHandled(true);
            }
        }

    }





}
