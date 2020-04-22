package com.latenighters.runicarcana.network;

import com.latenighters.runicarcana.common.items.armor.PrincipicBootsItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkSync {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("principium", "synchronization"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerPackets()
    {
        int ind = 0;
        INSTANCE.registerMessage(ind++, PrincipicBootsItem.StepSyncMessage.class,
                PrincipicBootsItem.StepSyncMessage::encode,
                PrincipicBootsItem.StepSyncMessage::decode,
                PrincipicBootsItem.StepSyncMessage::handle);

    }
}
