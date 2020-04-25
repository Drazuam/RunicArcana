package com.latenighters.runicarcana.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ClientProxy implements IProxy{
    @Override
    public PlayerEntity getPlayer() {
        return Minecraft.getInstance().player;
    }

    @Override
    public World getWorld() {
        return Minecraft.getInstance().world;
    }
}
