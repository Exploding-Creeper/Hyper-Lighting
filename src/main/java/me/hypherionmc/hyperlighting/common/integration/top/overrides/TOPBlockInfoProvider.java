package me.hypherionmc.hyperlighting.common.integration.top.overrides;

import mcjty.theoneprobe.api.*;
import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.api.CustomProbeInfo;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;

import static mcjty.theoneprobe.api.TextStyleClass.MODNAME;

public class TOPBlockInfoProvider implements IBlockDisplayOverride {

    @Override
    public boolean overrideStandardInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, Player playerEntity, Level world, BlockState blockState, IProbeHitData iProbeHitData) {

        if (probeMode != ProbeMode.DEBUG) {
            if (world.getBlockState(iProbeHitData.getPos()).getBlock() instanceof CustomProbeInfo customProbeInfo) {

                String modName = ModList.get().getModContainerById(ModConstants.MODID).get().getModInfo().getDisplayName();
                iProbeInfo.horizontal().item(iProbeHitData.getPickBlock()).vertical().text(CompoundText.create().name(blockState.getBlock().getName())).vertical().text(CompoundText.create().info(MODNAME + modName));

                iProbeInfo = customProbeInfo.getCustomProbeInfo(probeMode, playerEntity, world, blockState, iProbeHitData);
                return true;
            }
        }
        return false;
    }
}
