package com.drazuam.runicarcana.common.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;


/**
 * Created by Joel on 2/22/2017.
 */
public class PacketHandler {
    private static int packetId = 0;

    public static SimpleNetworkWrapper INSTANCE = null;

    public PacketHandler() {
    }

    public static int nextID() {
        return packetId++;
    }

    public static void registerMessages(String channelName) {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
        registerMessages();
    }

    public static void registerMessages() {
        // Register messages which are sent from the client to the server here:
        INSTANCE.registerMessage(PacketSendDust.Handler.class, PacketSendDust.class, nextID(), Side.SERVER);
        INSTANCE.registerMessage(PacketSendScrap.Handler.class, PacketSendScrap.class, nextID(), Side.SERVER);
    }
}