package com.latenighters.runicarcana.common.symbols.backend;

import com.latenighters.runicarcana.common.symbols.backend.capability.SymbolSyncer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.registries.RegistryManager;

import java.util.List;
import java.util.Map;

public class DrawnSymbol implements INBTSerializable<CompoundNBT>, IFunctionalObject {

    protected Symbol symbol;
    protected BlockPos drawnOn;
    protected Direction blockFace;
    protected int tickCounter = 0;
    protected int workDone = 0;
    protected float workVelocity = 0;
    protected int workShown = 0;

    private static final int SEND_WORK_RANGE = 4;
    private static final float mass              = 50f;
    private static final float constant_drag     = 0.005f;
    private static final float proportional_drag = 0.005f;

    public Map<Tuple<String,DataType>, Tuple<IFunctionalObject,IFunctional>> linkedInputs;

    public static World world;

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

    @Override
    public List<IFunctional> getOutputs() {
        return symbol.getOutputs();
    }

    @Override
    public List<Tuple<String,DataType>> getInputs() {
        return symbol.getInputs();
    }

    @Override
    public List<IFunctional> getFunctions() {
        return symbol.getFunctions();
    }

    @Override
    public List<Tuple<Tuple<String, DataType>, IFunctional>> getTriggers() {
        return symbol.getTriggers();
    }

    @Override
    public Map<Tuple<String, DataType>, Tuple<IFunctionalObject, IFunctional>> getInputLinks() {
        return linkedInputs;
    }

    public DrawnSymbol(Symbol symbol, BlockPos drawnOn, Direction blockFace, World world) {
        this.symbol = symbol;
        this.drawnOn = drawnOn;
        this.blockFace = blockFace;
        this.world = world;
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

    public void tick(World world, Chunk chunk)
    {
        this.tickCounter++;
        this.getSymbol().onTick(this, world, chunk, this.getDrawnOn(), this.getBlockFace());





        //TODO do this stuff based on render ticks, not game ticks
        workVelocity += ((float)workDone)/mass - proportional_drag*workVelocity;
        if(workVelocity>0)
            workVelocity -= constant_drag;
        if(workVelocity<0)
            workVelocity = 0;
        workShown += workVelocity;

        workDone = 0;
    }

    public void applyWork(int work)
    {
        workDone += work;
    }

    public void applyServerWork(int work, Chunk chunk)
    {
        for(PlayerEntity player : chunk.getWorld().getPlayers())
        {
            if(player.chunkCoordX > chunk.getPos().x - SEND_WORK_RANGE && player.chunkCoordX < chunk.getPos().x + SEND_WORK_RANGE &&
                    player.chunkCoordZ > chunk.getPos().z - SEND_WORK_RANGE && player.chunkCoordZ < chunk.getPos().z + SEND_WORK_RANGE)
            {
                SymbolSyncer.AddWorkMessage msg = new SymbolSyncer.AddWorkMessage(work, this, chunk.getPos());
                SymbolSyncer.INSTANCE.sendTo( msg, ((ServerPlayerEntity)(player)).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
            }
        }
    }

    public int getWork()
    {
        return workShown;
    }

    public int getTicksAlive() {
        return tickCounter;
    }
}
