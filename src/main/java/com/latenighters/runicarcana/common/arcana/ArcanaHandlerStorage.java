package com.latenighters.runicarcana.common.arcana;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class ArcanaHandlerStorage implements Capability.IStorage<IArcanaHandler> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<IArcanaHandler> capability, IArcanaHandler instance, Direction side) {
        return null;
    }

    @Override
    public void readNBT(Capability<IArcanaHandler> capability, IArcanaHandler instance, Direction side, INBT nbt) {

    }
}
