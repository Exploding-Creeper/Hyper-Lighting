package me.hypherionmc.hyperlighting.api.energy;

import net.minecraft.nbt.CompoundNBT;
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
