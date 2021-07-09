package me.hypherionmc.hyperlighting.api.energy;

public interface ISolarEnergyStorage {

    /***
     * Receive solar power from a machine or cable
     * @param maxReceive - The max amount of power to receive
     * @param simulate - Should this action be similated
     * @return - mount of energy that was (or would have been, if simulated) accepted by the storage.
     */
    int receiveSolar(int maxReceive, boolean simulate);

    int extractSolar(int maxExtract, boolean simulate);

    int getSolarEnergyStored();

    int getMaxSolarEnergyStored();

    boolean canExtract();

    boolean canReceive();

}
