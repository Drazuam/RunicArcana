package com.latenighters.runicarcana.common.symbols.backend.capability;

import com.latenighters.runicarcana.common.symbols.backend.DrawnSymbol;
import com.latenighters.runicarcana.common.symbols.backend.HashableTuple;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

public interface ISymbolHandler {

    public boolean isInit();
    public void clientSync(ChunkPos chunkPos);
    public ArrayList<DrawnSymbol> getSymbols();
    public boolean addSymbol(DrawnSymbol toadd, Chunk addingTo);
    public ArrayList<DrawnSymbol> getSymbolsAt(BlockPos position);
    public DrawnSymbol getSymbolAt(BlockPos position, Direction blockFace);
    public HashableTuple<List<HashableTuple<String, Object>>, List<HashableTuple<String, Object>>> getPreviousResolution(DrawnSymbol _symbol, Chunk chunk);
    public boolean isSymbolAt(BlockPos position);
    public boolean isSymbolAt(BlockPos position, Direction blockFace);
    public void addSymbol(DrawnSymbol symbol);
    public void markDirty();
    public void synchronizeSymbols(ArrayList<DrawnSymbol> symbols);
    void tick(World world, Chunk chunk);
}
