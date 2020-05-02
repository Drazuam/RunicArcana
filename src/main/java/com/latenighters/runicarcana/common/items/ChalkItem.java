package com.latenighters.runicarcana.common.items;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.client.gui.OverlayPopup;
import com.latenighters.runicarcana.common.setup.ModSetup;
import com.latenighters.runicarcana.common.symbols.Symbols;
import com.latenighters.runicarcana.common.symbols.backend.*;
import com.latenighters.runicarcana.common.symbols.backend.capability.ISymbolHandler;
import com.latenighters.runicarcana.common.symbols.backend.capability.SymbolSyncer;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.RegistryManager;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class ChalkItem extends Item {

    public static AtomicReference<HashableTuple<String, DataType>> selectedFunction;

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
                //TODO this will not work if shift+right clicking on a non-symbol functional object
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
            else if(context.getWorld().isRemote && context.getPlayer().isSteppingCarefully())
            {
                IFunctionalObject symbol = SymbolUtil.getLookedFunctionalObject();
                if(symbol !=null) {

                    ItemStack chalk = context.getItem();
                    Chunk chunk = context.getWorld().getChunkAt(context.getPos());

                    if (!chalk.getOrCreateTag().contains("linking_from") && selectedFunction!=null && selectedFunction.get()!=null) {
                        CompoundNBT nbt = symbol.basicSerializeNBT();
                        nbt.putString("func",selectedFunction.get().getA());
                        nbt.putString("type",selectedFunction.get().getB().name);
                        chalk.getTag().put("linking_from",nbt);
                    }else
                    {
                        sendNBTToServer(symbol,chalk,chunk);
                    }
                }


            }
        });

        return ActionResultType.SUCCESS;
    }

    @OnlyIn(Dist.CLIENT)
    public void sendNBTToServer(IFunctionalObject symbol, ItemStack chalk, Chunk chunk)
    {
        if(OverlayPopup.selectedFunction.get()==null) return;
        IFunctionalObject object = null;
        try {
            object = symbol.getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if(object==null)return;
        if(!chalk.getOrCreateTag().contains("linking_from"))return;
        if (chalk.getTag() != null) {
            object.deserializeNBT(chalk.getTag().getCompound("linking_from"));
        }
        object = object.findReal(chunk);
        if(object==null)return;

        SymbolSyncer.INSTANCE.sendToServer(new SymbolSyncer.SymbolLinkMessage(object,symbol,
                new HashableTuple<>(chalk.getTag().getCompound("linking_from").getString("func"),DataType.getDataType(chalk.getTag().getCompound("linking_from").getString("type"))),
                OverlayPopup.selectedFunction.get().getA(),OverlayPopup.selectedFunction.get().getB().name));

        chalk.getTag().remove("linking_from");
        OverlayPopup.funcObject.set(null);
        OverlayPopup.selectedFunction.set(null);
        OverlayPopup.updateRenderList(chalk,null);
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
            Symbol symbol = RegistryManager.ACTIVE.getRegistry(Symbol.class).getValue(new ResourceLocation(buf.readString(128)));
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
