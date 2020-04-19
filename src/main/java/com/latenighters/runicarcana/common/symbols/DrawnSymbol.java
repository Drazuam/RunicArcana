package com.latenighters.runicarcana.common.symbols;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.RegistryManager;

import java.rmi.registry.Registry;

public class DrawnSymbol implements INBTSerializable<CompoundNBT> {

    protected Symbol symbol;
    protected BlockPos drawnOn;
    protected Direction blockFace;
    protected int tickCounter = 0;

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

    public ResourceLocation getTexture() {
        return this.symbol.texture;
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
        symbol = RegistryManager.ACTIVE.getRegistry(Symbol.class).getValue(new ResourceLocation(nbt.getString("symbol")));
        blockFace = Direction.byIndex(nbt.getInt("face"));
    }

    public void encode(final PacketBuffer buf) {
        this.symbol.encode(this,buf);
    }

    public void decode(final PacketBuffer buf) {
        this.symbol.decode(this,buf);
    }

    //generate a new Drawn Symbol from a packetBuffer
    public DrawnSymbol(final PacketBuffer buf, final Symbol symbol) {
        this.symbol = symbol;
        this.decode(buf);
    }

    public void tick(World world, IChunk chunk)
    {
        this.tickCounter++;
        this.getSymbol().onTick(this, world, chunk, this.getDrawnOn(), this.getBlockFace());
    }

    public int getTicksAlive() {
        return tickCounter;
    }
}
