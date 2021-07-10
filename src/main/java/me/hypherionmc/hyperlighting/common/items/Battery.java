package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.hyperlighting.HyperLighting;
import me.hypherionmc.hyperlighting.api.energy.IEnergyContainerItem;
import me.hypherionmc.hyperlighting.api.energy.ItemEnergyStorage;
import me.hypherionmc.hyperlighting.common.capabilities.CapabilityProviderEnergy;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;

public class Battery extends Item implements IEnergyContainerItem {

    private final String ENERGY_TAG = "energy";

    private final int capacity;
    private final int maxReceive;
    private final int maxExtract;

    public Battery(int capacity, int maxReceive, int maxExtract) {
        super(new Properties().group(HyperLighting.machinesTab).maxStackSize(1).setNoRepair());
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new CapabilityProviderEnergy<>(new ItemEnergyStorage(this, stack));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (CapabilityEnergy.ENERGY == null) return;
        if (stack.getCapability(CapabilityEnergy.ENERGY).isPresent()) {
            IEnergyStorage energyStorage = stack.getCapability(CapabilityEnergy.ENERGY).resolve().get();
            tooltip.add(new StringTextComponent("Energy: " + energyStorage.getEnergyStored() + "/" + energyStorage.getMaxEnergyStored()));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        if (!container.hasTag()) {
            container.setTag(new CompoundNBT());
        }
        int stored = Math.min(container.getTag().getInt(ENERGY_TAG), getMaxEnergyStored(container));
        int energyReceived = Math.min(capacity - stored, Math.min(this.maxReceive, maxReceive));
        if (!simulate) {
            stored += energyReceived;
            container.getTag().putInt(ENERGY_TAG, stored);
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        if (container.getTag() == null || !container.getTag().contains(ENERGY_TAG)) {
            return 0;
        }
        int stored = Math.min(container.getTag().getInt(ENERGY_TAG), getMaxEnergyStored(container));
        int energyExtracted = Math.min(stored, Math.min(this.maxExtract, maxExtract));

        if (!simulate) {
            stored -= energyExtracted;
            container.getTag().putInt(ENERGY_TAG, stored);
        }
        return energyExtracted;
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        if (container.getTag() == null || !container.getTag().contains(ENERGY_TAG)) {
            return 0;
        }
        return Math.min(container.getTag().getInt(ENERGY_TAG), getMaxEnergyStored(container));
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return capacity;
    }

}
