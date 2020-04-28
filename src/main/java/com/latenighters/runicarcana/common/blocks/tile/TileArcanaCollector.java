package com.latenighters.runicarcana.common.blocks.tile;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.common.arcana.ArcanaChamber;
import com.latenighters.runicarcana.common.arcana.ArcanaMachine;
import com.latenighters.runicarcana.common.arcana.ArcanaMix;
import com.latenighters.runicarcana.common.setup.Registration;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class TileArcanaCollector extends ArcanaMachine {

    public TileArcanaCollector() {
        super(Registration.ARCANA_COLLECTOR_TILE.get());
        this.chambers.add(new ArcanaChamber(10000, 1));
    }

    @Override
    public void tick() {
        super.tick();

        this.chambers.get(0).addArcana(ArcanaMix.COMMON.mult(0.2f));
    }

    @Override
    public int getChamberSlotFromHitVec(Vec3d hitVec) {
        return 0;
    }

    @Override
    public ArcanaChamber getChamberFromSlot(int slot) {
        return this.chambers.get(slot);
    }

    @Override
    public int getExportRate() {
        return 10000;
    }

    @Override
    public boolean canExport() {
        return true;
    }

    @Override
    public boolean canImport() {
        return false;
    }
}
