package com.latenighters.runicarcana.common.capabilities;

import com.latenighters.runicarcana.common.symbols.DrawnSymbol;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;

public interface ISymbolHandler {

    public boolean isInit();
    public void clientSync(ChunkPos chunkPos);
    public ArrayList<DrawnSymbol> getSymbols();
    public boolean addSymbol(DrawnSymbol toadd, Chunk addingTo);
    public ArrayList<DrawnSymbol> getSymbolsAt(BlockPos position);
    public DrawnSymbol getSymbolAt(BlockPos position, Direction blockFace);
    public boolean isSymbolAt(BlockPos position);
    public boolean isSymbolAt(BlockPos position, Direction blockFace);
    public void addSymbol(DrawnSymbol symbol);
    public void markDirty();
    public void synchronizeSymbols(ArrayList<DrawnSymbol> symbols);
    void tick(World world, Chunk chunk);
}
