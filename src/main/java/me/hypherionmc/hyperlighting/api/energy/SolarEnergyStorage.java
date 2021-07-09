package me.hypherionmc.hyperlighting.api.energy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.energy.IEnergyStorage;

public class SolarEnergyStorage implements ISolarEnergyStorage, IEnergyStorage {

    protected int energy;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    public SolarEnergyStorage(int capacity)
    {
        this(capacity, capacity, capacity, 0);
    }

    public SolarEnergyStorage(int capacity, int maxTransfer)
    {
        this(capacity, maxTransfer, maxTransfer, 0);
    }

    public SolarEnergyStorage(int capacity, int maxReceive, int maxExtract)
    {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public SolarEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy)
    {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.energy = Math.max(0 , Math.min(capacity, energy));
    }

    @Override
    public int receiveSolar(int maxReceive, boolean simulate)
    {
        if (!canReceive())
            return 0;

        int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            energy += energyReceived;
        return energyReceived;
    }

    public int receiveSolarInternal(int maxReceive, boolean simulate)
    {
        int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            energy += energyReceived;
        return energyReceived;
    }

    @Override
    public int extractSolar(int maxExtract, boolean simulate)
    {
        if (!canExtract())
            return 0;

        int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            energy -= energyExtracted;
        return energyExtracted;
    }

    public int extractSolarInternal(int maxExtract, boolean simulate)
    {
        int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            energy -= energyExtracted;
        return energyExtracted;
    }

    @Override
    public int getSolarEnergyStored()
    {
        return energy;
    }

    @Override
    public int getMaxSolarEnergyStored()
    {
        return capacity;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return this.receiveSolar(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return this.extractSolar(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored() {
        return this.getSolarEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return this.getMaxSolarEnergyStored();
    }

    @Override
    public boolean canExtract()
    {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive()
    {
        return this.maxReceive > 0;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public CompoundNBT writeNBT(CompoundNBT compound) {
        compound.putInt("energy", this.energy);
        compound.putInt("capacity", this.capacity);
        compound.putInt("maxReceive", this.maxReceive);
        compound.putInt("maxExtract", this.maxExtract);
        return compound;
    }

    public void readNBT(CompoundNBT compound) {
        this.capacity = compound.getInt("capacity");
        this.energy = compound.getInt("energy");
        this.maxExtract = compound.getInt("maxExtract");
        this.maxReceive = compound.getInt("maxReceive");
    }

}
