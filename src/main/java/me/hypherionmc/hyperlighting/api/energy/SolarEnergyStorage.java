package me.hypherionmc.hyperlighting.api.energy;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.energy.EnergyStorage;

public class SolarEnergyStorage extends EnergyStorage {

    public SolarEnergyStorage(int capacity)
    {
        super(capacity, capacity, capacity, 0);
    }

    public SolarEnergyStorage(int capacity, int maxTransfer)
    {
        super(capacity, maxTransfer, maxTransfer, 0);
    }

    public SolarEnergyStorage(int capacity, int maxReceive, int maxExtract)
    {
        super(capacity, maxReceive, maxExtract, 0);
    }

    public SolarEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy)
    {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public CompoundTag writeNBT(CompoundTag compound) {
        compound.putInt("energy", this.energy);
        compound.putInt("capacity", this.capacity);
        compound.putInt("maxReceive", this.maxReceive);
        compound.putInt("maxExtract", this.maxExtract);
        return compound;
    }

    public void readNBT(CompoundTag compound) {
        this.capacity = compound.getInt("capacity");
        this.energy = compound.getInt("energy");
        this.maxExtract = compound.getInt("maxExtract");
        this.maxReceive = compound.getInt("maxReceive");
    }

    public int setEnergyStored(int energyStored) {
        this.energy = energyStored;
        return this.energy;
    }

    public int receiveEnergyInternal(int maxReceive, boolean simulate)
    {
        int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            energy += energyReceived;
        return energyReceived;
    }
}
