package com.latenighters.runicarcana.common.blocks.tile;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.common.arcana.ArcanaHandler;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class TileArcanaPylon extends TileEntity implements ITickableTileEntity {

    public TileArcanaPylon(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick() {

    }
}
