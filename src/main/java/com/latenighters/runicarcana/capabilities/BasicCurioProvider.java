package com.latenighters.runicarcana.capabilities;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.capability.CuriosCapability;
import top.theillusivec4.curios.api.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BasicCurioProvider implements ICapabilityProvider {

    final LazyOptional<ICurio> capability;

    public BasicCurioProvider(ICurio curio) {

        this.capability = LazyOptional.of(() -> curio);
    }

    @SuppressWarnings("ConstantConditions")
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        return CuriosCapability.ITEM.orEmpty(cap, capability);
    }
}