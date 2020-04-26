package com.latenighters.runicarcana.common.blocks;

import com.latenighters.runicarcana.common.blocks.tile.TileArcanaCollector;
import com.latenighters.runicarcana.common.blocks.tile.TileArcanaPylon;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class ArcanaPylon extends Block {
    public ArcanaPylon(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileArcanaPylon();
    }
}
