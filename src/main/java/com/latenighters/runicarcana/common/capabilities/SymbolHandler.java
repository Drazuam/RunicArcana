package com.latenighters.runicarcana.common.capabilities;

import com.latenighters.runicarcana.RunicArcana;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Callable;

import static com.latenighters.runicarcana.RunicArcana.MODID;

public class SymbolHandler implements ISymbolHandler, INBTSerializable<CompoundNBT> {

    public SymbolHandler() {
    }

    @Override
    public CompoundNBT serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }

    public static class SymbolHandlerFactory extends SymbolHandler implements Callable<ISymbolHandler>{
        @Override
        public ISymbolHandler call() throws Exception {
            return new SymbolHandler();
        }
    }

    public static class Provider implements ICapabilityProvider
    {

        public static final ResourceLocation NAME = new ResourceLocation(MODID, "symbolhandler");
        private LazyOptional<ISymbolHandler> instance = LazyOptional.of(RunicArcana.SYMBOL_CAP::getDefaultInstance);
        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

            return RunicArcana.SYMBOL_CAP.orEmpty(cap,instance);

        }
    }
}
