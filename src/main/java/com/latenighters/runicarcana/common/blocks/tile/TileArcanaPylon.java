package com.latenighters.runicarcana.common.blocks.tile;

import com.latenighters.runicarcana.RunicArcana;
import com.latenighters.runicarcana.common.arcana.ArcanaChamber;
import com.latenighters.runicarcana.common.arcana.ArcanaMachine;
import com.latenighters.runicarcana.common.setup.Registration;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.atomic.AtomicReference;

public class TileArcanaPylon extends ArcanaMachine {

    public TileArcanaPylon() {
        super(Registration.ARCANA_PYLON_TILE.get());
        //TODO maybe pull this out

        this.getCapability(RunicArcana.ARCANA_CAP).ifPresent(cap->{
            cap.getChambers().add(new ArcanaChamber(1000000,1));
        });
    }

    @Override
    public void tick() {
        super.tick();
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
    public boolean canExport() {
        return true;
    }

    @Override
    public boolean canImport() {
        return true;
    }
}
