package com.latenighters.runicarcana.common.blocks.tile;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.common.arcana.ArcanaMachine;
import com.latenighters.runicarcana.common.setup.Registration;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class TileArcanaCollector extends ArcanaMachine implements ITickableTileEntity {

    public TileArcanaCollector() {
        super(Registration.ARCANA_COLLECTOR_TILE.get());
    }

    @Override
    public void tick() {

    }

    @Override
    public boolean canExport() {
        return true;
    }

    @Override
    public boolean canImport() {
        return false;
    }
}
