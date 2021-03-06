package com.latenighters.runicarcana.common.blocks;

import com.latenighters.runicarcana.common.blocks.tile.TileArcanaCollector;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class ArcanaCollector extends Block {
    public ArcanaCollector(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileArcanaCollector();
    }



}
