package me.hypherionmc.hyperlighting.api.fluid;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;

public interface FluidContainer {

    FluidVariant getContainedFluid();

    int getContainerLevel();

    int getContainerCapacity();

    boolean canContain(FluidVariant fluid);

    int fill(FluidVariant fluid, FillAction action);

    FluidVariant extract(int max, FillAction action);

    FluidVariant extract(FluidVariant fluid, FillAction action);

}
