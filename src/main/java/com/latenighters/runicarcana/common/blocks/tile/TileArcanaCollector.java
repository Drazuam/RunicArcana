package com.latenighters.runicarcana.common.blocks.tile;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class TileArcanaCollector extends TileEntity implements ITickableTileEntity {

    public TileArcanaCollector(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick() {

    }
}
