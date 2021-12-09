package me.hypherionmc.hyperlighting.api.energy;

import net.minecraft.nbt.NbtCompound;

/***
 * Loosely based on the Forge Energy System
 */
public class SolarEnergyStorage {

    protected int energyLevel;
    protected int energyCapacity;
    protected int maxEnergyReceive;
    protected int maxEnergyExtract;

    public SolarEnergyStorage(int capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public SolarEnergyStorage(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer, 0);
    }

    public SolarEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public SolarEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        this.energyLevel = energy;
        this.maxEnergyReceive = maxReceive;
        this.maxEnergyExtract = maxExtract;
        this.energyCapacity = capacity;
    }

    public NbtCompound writeNBT(NbtCompound compound) {
        compound.putInt("energy", this.energyLevel);
        return compound;
    }

    public void readNBT(NbtCompound compound) {
        this.energyLevel = compound.getInt("energy");
    }

    public int setEnergyStored(int energyStored) {
        this.energyLevel = energyStored;
        return this.energyLevel;
    }

    public int receiveEnergyInternal(int toReceive, boolean test) {
        int energyReceived = Math.min(this.energyCapacity - this.energyLevel, Math.min(this.maxEnergyReceive, toReceive));
        if (!test)
            this.energyLevel += energyReceived;
        return energyReceived;
    }

    public int receiveEnergy(int toReceive, boolean test) {
        if (this.maxEnergyReceive < 1) {
            return 0;
        }
        return this.receiveEnergyInternal(toReceive, test);
    }

    public int extractEnergyInternal(int toExtract, boolean test) {
        int energyExtracted = Math.min(this.energyLevel, Math.min(this.energyCapacity, toExtract));
        if (!test)
            this.energyLevel -= energyExtracted;
        return energyExtracted;
    }

    public int extractEnergy(int toExtract, boolean test) {
        if (this.maxEnergyExtract < 1) {
            return 0;
        }
        int energyExtracted = Math.min(this.energyLevel, Math.min(this.maxEnergyExtract, toExtract));
        if (!test)
            this.energyLevel -= energyExtracted;
        return energyExtracted;
    }

    public int getEnergyCapacity() {
        return energyCapacity;
    }

    public int getEnergyLevel() {
        return energyLevel;
    }

    public int getMaxEnergyExtract() {
        return maxEnergyExtract;
    }

    public int getMaxEnergyReceive() {
        return maxEnergyReceive;
    }
}
