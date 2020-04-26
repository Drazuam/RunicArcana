package com.latenighters.runicarcana.common.arcana;

import com.latenighters.runicarcana.RunicArcana;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class ArcanaMachine extends TileEntity {

    public final ArrayList<ArcanaChamber> chambers = new ArrayList<>();

    public abstract boolean canExport();
    public abstract boolean canImport();

    public final Set<ArcanaMachine> upstreamLinks   = new HashSet<>();
    public final Set<ArcanaMachine> downstreamLinks = new HashSet<>();

    public ArcanaMachine(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        return RunicArcana.ARCANA_CAP.orEmpty(cap,LazyOptional.empty());
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }

    @Override
    public CompoundNBT serializeNBT() {
        return null;
    }
}
