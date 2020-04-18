package com.latenighters.runicarcana.common.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class SymbolHandlerStorage implements Capability.IStorage<ISymbolHandler> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<ISymbolHandler> capability, ISymbolHandler instance, Direction side) {
        return ((SymbolHandler)instance).serializeNBT();
        //return null;
    }

    @Override
    public void readNBT(Capability<ISymbolHandler> capability, ISymbolHandler instance, Direction side, INBT nbt) {
        ((SymbolHandler)instance).deserializeNBT((CompoundNBT)nbt);
    }
}