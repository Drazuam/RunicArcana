package com.latenighters.runicarcana.common.blocks.tile;

import com.latenighters.runicarcana.common.arcana.ArcanaChamber;
import com.latenighters.runicarcana.common.arcana.ArcanaMachine;
import com.latenighters.runicarcana.common.setup.Registration;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.Vec3d;

public class TileArcanaPylon extends ArcanaMachine {

    public TileArcanaPylon() {
        super(Registration.ARCANA_PYLON_TILE.get());
        //TODO maybe pull this out
        this.chambers.add(new ArcanaChamber(1000000,1));
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
        return this.chambers.get(slot);
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
