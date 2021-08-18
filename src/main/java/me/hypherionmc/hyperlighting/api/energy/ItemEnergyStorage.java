package me.hypherionmc.hyperlighting.api.energy;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;

public class ItemEnergyStorage implements IEnergyStorage {

    public ItemStack itemStack;

    public IEnergyContainerItem itemRF;

    public ItemEnergyStorage(IEnergyContainerItem itemRF, ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemRF = itemRF;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return itemRF.receiveEnergy(itemStack, maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return itemRF.extractEnergy(itemStack, maxExtract, simulate);
    }

    @Override
    public int getEnergyStored() {
        return itemRF.getEnergyStored(itemStack);
    }

    @Override
    public int getMaxEnergyStored() {
        return itemRF.getMaxEnergyStored(itemStack);
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

}
