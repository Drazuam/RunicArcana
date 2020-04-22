package com.latenighters.runicarcana.common.items;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.client.gui.OverlayPopup;
import com.latenighters.runicarcana.client.gui.ScreenChalk;
import com.latenighters.runicarcana.common.symbols.backend.*;
import com.latenighters.runicarcana.common.symbols.backend.capability.ISymbolHandler;
import com.latenighters.runicarcana.common.symbols.backend.capability.SymbolSyncer;
import com.latenighters.runicarcana.common.setup.ModSetup;
import com.latenighters.runicarcana.common.symbols.Symbols;
import net.minecraft.client.Minecraft;
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
import net.minecraft.util.Tuple;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.RegistryManager;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class ChalkItem extends Item {

    public static final AtomicReference<Tuple<String, DataType>> selectedFunction = new AtomicReference<>();

    static OverlayPopup popup = new OverlayPopup(selectedFunction);

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

        return ActionResultType.SUCCESS;
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        CompoundNBT nbt = stack.getOrCreateTag();
        nbt.putString("selected_symbol",Symbols.DEBUG.getRegistryName().toString());
        super.onCreated(stack, worldIn, playerIn);
    }

    @SubscribeEvent
    public static void onRenderEvent(RenderGameOverlayEvent.Post event)
    {
        if(event.getType() != RenderGameOverlayEvent.ElementType.CROSSHAIRS) return;
        if(Minecraft.getInstance().player.getHeldItemMainhand().getItem() instanceof ChalkItem)
            popup.render(event.getPartialTicks(), Minecraft.getInstance().player.getHeldItemMainhand());
        else if(Minecraft.getInstance().player.getHeldItemOffhand().getItem() instanceof ChalkItem)
            popup.render(event.getPartialTicks(), Minecraft.getInstance().player.getHeldItemOffhand());

    }

    @SubscribeEvent
    public static void onScrollWheel(InputEvent.MouseScrollEvent event)
    {
        if((Minecraft.getInstance().player.getHeldItemMainhand().getItem() instanceof ChalkItem || Minecraft.getInstance().player.getHeldItemMainhand().getItem() instanceof ChalkItem)
                && Minecraft.getInstance().player.isSteppingCarefully() && popup.funcObject.get()!=null){

            ItemStack chalk = Minecraft.getInstance().player.getHeldItemMainhand().getItem() instanceof ChalkItem ?
                    Minecraft.getInstance().player.getHeldItem(Hand.MAIN_HAND) : Minecraft.getInstance().player.getHeldItem(Hand.OFF_HAND);
            int indexMove = 0;
            if(event.getScrollDelta()>0)
                indexMove = 1;
            else
                indexMove = -1;

            if(popup.funcObject.get()==null)return;
            if(popup.selectedFunction.get()==null)return;

            if(chalk.getOrCreateTag().contains("linking_from"))
            {
                int prevIndex = 0;
                List<IFunctional> outputs = popup.funcObject.get().getOutputs();
                for(int i=0; i<outputs.size(); i++){
                    if(outputs.get(i).getName().equals(popup.selectedFunction.get().getA()) && outputs.get(i).getOutputType()==popup.selectedFunction.get().getB())
                        prevIndex=i;
                }

                int newInd = (prevIndex + indexMove)%popup.funcObject.get().getOutputs().size();
                if (newInd<0) newInd += popup.funcObject.get().getOutputs().size();
                popup.selectedFunction.set(new Tuple<>(outputs.get(newInd).getName(), outputs.get(newInd).getOutputType()));
            }
            else
            {
                int prevIndex = popup.funcObject.get().getInputs().indexOf(popup.selectedFunction.get());
                int newInd = (prevIndex + indexMove) % popup.funcObject.get().getInputs().size();
                if (newInd<0) newInd += popup.funcObject.get().getInputs().size();
                popup.selectedFunction.set(popup.funcObject.get().getInputs().get(newInd));
            }

            event.setCanceled(true);
        }
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
