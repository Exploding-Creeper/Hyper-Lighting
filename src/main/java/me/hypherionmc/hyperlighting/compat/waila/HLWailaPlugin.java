package me.hypherionmc.hyperlighting.compat.waila;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import me.hypherionmc.hyperlighting.HyperLightingFabric;
import me.hypherionmc.hyperlighting.common.blockentities.CampFireBlockEntity;
import me.hypherionmc.hyperlighting.common.blocks.CampFireBlock;
import me.hypherionmc.hyperlighting.common.blocks.FogMachineBlock;

public class HLWailaPlugin implements IWailaPlugin {

    @Override
    public void register(IRegistrar registrar) {
        HyperLightingFabric.logger.info("Registering WTHIT Plugins");
        registrar.addComponent(new FogMachineProvider(), TooltipPosition.BODY, FogMachineBlock.class);

        registrar.addBlockData(new CampfireDataProvider(), CampFireBlockEntity.class);
        registrar.addComponent(new CampfireProvider(), TooltipPosition.BODY, CampFireBlock.class);
    }

}
