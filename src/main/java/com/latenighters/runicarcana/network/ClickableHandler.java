package com.latenighters.runicarcana.network;


import com.latenighters.runicarcana.common.items.IClickable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class ClickableHandler
{

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("principium", "clicker"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerPackets()
    {
        int ind = 0;
        INSTANCE.registerMessage(ind++, ClickActionMessage.class,
                ClickActionMessage::encode,
                ClickActionMessage::decode,
                ClickActionMessage::handle);

    }

    public static class ClickActionMessage
    {
        public int slot;
        public boolean success;
        private Boolean isValid;

        public ClickActionMessage(int slot, boolean success) {
            this.slot = slot;
            this.success = success;
            isValid = false;
        }

        public static void encode(final ClickActionMessage msg, final PacketBuffer buf)
        {
            buf.writeInt(msg.slot);
            buf.writeBoolean(msg.success);
        }

        public static ClickActionMessage decode(final PacketBuffer buf)
        {
            return new ClickActionMessage(buf.readInt(), buf.readBoolean());
        }

        public static void handle(final ClickActionMessage msg, final Supplier<NetworkEvent.Context> contextSupplier)
        {
            final NetworkEvent.Context context = contextSupplier.get();
            if (context.getDirection().equals(NetworkDirection.PLAY_TO_CLIENT))
            {
                context.enqueueWork(() -> {

                    //TODO: error handling

                    if(!msg.success)
                        return;

                    final ClientPlayerEntity sender = Minecraft.getInstance().player;
                    if (sender == null) {
                        return;
                    }

                    ItemStack item;
                    if(sender.openContainer instanceof CreativeScreen.CreativeContainer) {
                        if(msg.slot < sender.inventory.mainInventory.size())
                            item = sender.inventory.getStackInSlot(msg.slot);
                        else
                            item = sender.inventory.armorInventory.get(msg.slot-sender.inventory.mainInventory.size());
                    }
                    else {
                        item = sender.openContainer.getSlot(msg.slot).getStack();
                    }

                    if(item.getItem() instanceof IClickable)
                    {
                        ((IClickable) item.getItem()).onClick(sender, item, sender.openContainer, msg.slot);
                    }

                });
                context.setPacketHandled(true);
            }
            else if (context.getDirection().equals(NetworkDirection.PLAY_TO_SERVER))
            {
                context.enqueueWork(() -> {
                    final ServerPlayerEntity sender = context.getSender();
                    if (sender == null) {
                        return;
                    }

                    ItemStack item;
                    if(sender.openContainer instanceof PlayerContainer) {
                        if(msg.slot < sender.inventory.mainInventory.size())
                            item = sender.openContainer.getSlot(msg.slot).getStack();
                        else
                            item = sender.inventory.armorInventory.get(msg.slot-sender.inventory.mainInventory.size());
                    }
                    else {
                        item = sender.openContainer.getSlot(msg.slot).getStack();
                    }

                    if(item.getItem() instanceof IClickable)
                    {
                        Boolean result = ((IClickable) item.getItem()).onClick(sender, item, sender.openContainer, msg.slot);
                        INSTANCE.reply(new ClickActionMessage(msg.slot, result), context);
                    }

                });
                context.setPacketHandled(true);
            }
        }

    }



}
