package com.latenighters.runicarcana.common.arcana;

import com.latenighters.runicarcana.RunicArcana;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static com.latenighters.runicarcana.RunicArcana.MODID;

public class ArcanaHandler implements IArcanaHandler, ICapabilitySerializable<CompoundNBT> {

    public final List<ArcanaChamber> chambers = new ArrayList<>();

    public List<ArcanaChamber> getChambers() {
        return chambers;
    }

    public ArcanaHandler() {
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();

        ListNBT lnbt = new ListNBT();
        for(ArcanaChamber chamber : chambers)
        {
            lnbt.add(chamber.serializeNBT());
        }
        nbt.put("chambers",lnbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ListNBT listNBT = nbt.getList("chambers",10);
        listNBT.forEach(nbt2->{
            ArcanaChamber chamber = new ArcanaChamber(nbt);
            this.chambers.add(chamber);
        });
    }

    public static final ResourceLocation NAME = new ResourceLocation(MODID, "arcanahandler");
    private final LazyOptional<IArcanaHandler> instance = LazyOptional.of(RunicArcana.ARCANA_CAP::getDefaultInstance);
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return RunicArcana.ARCANA_CAP.orEmpty(cap,instance);
    }

    public static class ArcanaHandlerFactory implements Callable<ArcanaHandler>
    {
        @Override
        public ArcanaHandler call() throws Exception {
            return new ArcanaHandler();
        }
    }
}
