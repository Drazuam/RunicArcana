package com.latenighters.runicarcana.common.blocks.tile;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.common.arcana.ArcanaChamber;
import com.latenighters.runicarcana.common.arcana.ArcanaMachine;
import com.latenighters.runicarcana.common.arcana.ArcanaMix;
import com.latenighters.runicarcana.common.setup.Registration;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.atomic.AtomicReference;

public class TileArcanaCollector extends ArcanaMachine {

    public TileArcanaCollector() {
        super(Registration.ARCANA_COLLECTOR_TILE.get());
        this.getCapability(RunicArcana.ARCANA_CAP).ifPresent(cap->{
            cap.getChambers().add(new ArcanaChamber(10000, 1));
        });

    }

    @Override
    public void tick() {
        super.tick();
        this.getCapability(RunicArcana.ARCANA_CAP).ifPresent(cap-> {
            cap.getChambers().get(0).addArcana(ArcanaMix.COMMON.mult(0.02f));
        });
    }

    @Override
    public int getChamberSlotFromHitVec(Vec3d hitVec) {
        return 0;
    }

    @Override
    public ArcanaChamber getChamberFromSlot(int slot) {

        AtomicReference<ArcanaChamber> arcanaChamber = new AtomicReference<>();
        getCapability(RunicArcana.ARCANA_CAP).ifPresent(cap->{
            arcanaChamber.set(cap.getChambers().get(slot));
        });

        return arcanaChamber.get();
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
