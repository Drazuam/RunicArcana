package com.latenighters.runicarcana.common.blocks.tile;

import com.latenighters.runicarcana.common.arcana.ArcanaMachine;
import com.latenighters.runicarcana.common.setup.Registration;
import net.minecraft.tileentity.ITickableTileEntity;

public class TileArcanaPylon extends ArcanaMachine implements ITickableTileEntity {

    public TileArcanaPylon() {
        super(Registration.ARCANA_PYLON_TILE.get());
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
        return true;
    }
}
