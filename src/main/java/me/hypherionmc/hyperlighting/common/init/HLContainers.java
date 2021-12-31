package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.common.containers.ContainerBatteryNeon;
import me.hypherionmc.hyperlighting.common.containers.ContainerSwitchBoard;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class HLContainers {

    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, ModConstants.MODID);

    public static final RegistryObject<MenuType<ContainerBatteryNeon>> BATTERY_NEON_CONTAINER = CONTAINERS.register("guibatteryneon", () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.getCommandSenderWorld();
        return new ContainerBatteryNeon(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<MenuType<ContainerSwitchBoard>> SWITCHBOARD_CONTAINER = CONTAINERS.register("guiswitchboard", () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.getCommandSenderWorld();
        return new ContainerSwitchBoard(windowId, world, pos, inv, inv.player);
    }));

}
