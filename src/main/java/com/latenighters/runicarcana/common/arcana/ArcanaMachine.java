package com.latenighters.runicarcana.common.arcana;

import com.latenighters.runicarcana.RunicArcana;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class ArcanaMachine extends TileEntity implements ITickableTileEntity {

    public final ArrayList<ArcanaChamber> chambers = new ArrayList<>();

    public abstract boolean canExport();

    public abstract boolean canImport();

    public int getImportRate() {
        return 0;
    }

    public int getExportRate() {
        return 0;
    }

    private final Set<Tuple<Integer, Tuple<Integer, BlockPos>>> upstreamLinks = new HashSet<>();
    private final Set<Tuple<Integer, Tuple<Integer, BlockPos>>> downstreamLinks = new HashSet<>();

    public ArcanaMachine(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick() {
        if (this.canExport() && this.getExportRate() > 0) {
            upstreamLinks.forEach(link -> {

                TileEntity tileTo = this.world.getTileEntity(link.getB().getB());
                if(tileTo instanceof ArcanaMachine)
                    this.getChamberFromSlot(link.getA()).transferToArcanaChamber(((ArcanaMachine) tileTo)
                            .getChamberFromSlot(link.getB().getA()),this.getExportRate());
                else
                    upstreamLinks.remove(link);
            });
        }
    }

    public void clearLinks()
    {
        this.upstreamLinks.clear();
        this.downstreamLinks.clear();
    }

    public void addExportLink(int localSlot, BlockPos blockPos, int machineSlot){
        this.upstreamLinks.add(new Tuple<>(localSlot, new Tuple<>(machineSlot, blockPos)));
    }

    public abstract int getChamberSlotFromHitVec(Vec3d hitVec);
    public abstract ArcanaChamber getChamberFromSlot(int slot);


    public ArcanaMix addMix(ArcanaChamber chamber, ArcanaMix mix)
    {
        if(chambers.contains(chamber))
        {
            return chamber.addArcana(mix);
        }
        return mix;
    }

//    public ArcanaMix removeMix(ArcanaChamber chamber, int amount)
//    {
//
//    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        return RunicArcana.ARCANA_CAP.orEmpty(cap,LazyOptional.empty());
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }

    @Override
    public CompoundNBT serializeNBT() {
        return null;
    }
}
