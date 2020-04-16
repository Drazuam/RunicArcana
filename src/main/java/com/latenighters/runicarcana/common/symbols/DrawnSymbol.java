package com.latenighters.runicarcana.common.symbols;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

import java.rmi.registry.Registry;

public class DrawnSymbol implements INBTSerializable<CompoundNBT> {

    protected Symbol symbol;
    protected BlockPos drawnOn;
    protected Direction blockFace;

    public DrawnSymbol(CompoundNBT lnbt) {
        this.deserializeNBT(lnbt);
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public BlockPos getDrawnOn() {
        return drawnOn;
    }

    public Direction getBlockFace() {
        return blockFace;
    }

    public DrawnSymbol(Symbol symbol, BlockPos drawnOn, Direction blockFace) {
        this.symbol = symbol;
        this.drawnOn = drawnOn;
        this.blockFace = blockFace;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putString("symbol", symbol.getRegistryName().toString());
        nbt.merge(NBTUtil.writeBlockPos(drawnOn));
        nbt.putInt("face", blockFace.getIndex());

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        drawnOn = NBTUtil.readBlockPos(nbt);
        symbol = SymbolRegistryHandler.SYMBOLS.getValue(new ResourceLocation(nbt.getString("symbol")));
        blockFace = Direction.byIndex(nbt.getInt("face"));
    }
}
