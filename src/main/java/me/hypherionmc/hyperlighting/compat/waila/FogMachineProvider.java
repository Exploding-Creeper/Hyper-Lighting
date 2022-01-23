package me.hypherionmc.hyperlighting.compat.waila;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;
import me.hypherionmc.hyperlighting.common.blockentities.FogMachineBlockEntity;
import me.hypherionmc.hyperlighting.utils.RenderUtils;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.StringUtils;

public class FogMachineProvider implements IBlockComponentProvider {

    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlockEntity() instanceof FogMachineBlockEntity fogMachine) {
            long amount = fogMachine.getTank().getAmount();
            long capacity = fogMachine.getTank().getCapacity();
            tooltip.add(new LiteralText("Fluid Level: " + RenderUtils.getFluidAmount(amount, capacity).getString()));
            tooltip.add(new LiteralText("Autofire: " + (fogMachine.isAutoFireEnabled() ? Formatting.GREEN + "Yes" : "No")));
            if (fogMachine.isAutoFireEnabled()) {
                tooltip.add(new LiteralText("Autofire Interval: " + RenderUtils.getTimeDisplayString(fogMachine.autoFireTime).getString()));
            }
            tooltip.add(new LiteralText("Fog Color: " + StringUtils.capitalize(fogMachine.getColor().getName().replace("_", " "))));
            tooltip.add(new LiteralText("Status: " + (fogMachine.canFire() ? Formatting.GREEN + "Ready" : Formatting.RED + "Not Ready")));
        }
    }
}
