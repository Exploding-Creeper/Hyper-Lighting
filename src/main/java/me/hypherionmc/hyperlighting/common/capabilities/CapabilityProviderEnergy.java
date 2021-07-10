package me.hypherionmc.hyperlighting.common.capabilities;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityProviderEnergy<HANDLER> implements ICapabilityProvider {

    protected IEnergyStorage iEnergyStorage;

    public CapabilityProviderEnergy(IEnergyStorage instance) {
        this.iEnergyStorage = instance;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CapabilityEnergy.ENERGY.orEmpty(cap, LazyOptional.of(() -> this.iEnergyStorage));
    }
}
