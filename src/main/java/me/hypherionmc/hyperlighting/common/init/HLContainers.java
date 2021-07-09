package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.common.containers.ContainerBatteryNeon;
import me.hypherionmc.hyperlighting.common.containers.ContainerSwitchBoard;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class HLContainers {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, ModConstants.MODID);

    public static final RegistryObject<ContainerType<ContainerBatteryNeon>> BATTERY_NEON_CONTAINER = CONTAINERS.register("guibatteryneon", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntityWorld();
        return new ContainerBatteryNeon(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<ContainerType<ContainerSwitchBoard>> SWITCHBOARD_CONTAINER = CONTAINERS.register("guiswitchboard", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntityWorld();
        return new ContainerSwitchBoard(windowId, world, pos, inv, inv.player);
    }));

}
