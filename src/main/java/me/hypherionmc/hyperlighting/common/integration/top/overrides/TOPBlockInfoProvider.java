package me.hypherionmc.hyperlighting.common.integration.top.overrides;

import mcjty.theoneprobe.api.*;
import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.api.CustomProbeInfo;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;

import static mcjty.theoneprobe.api.TextStyleClass.MODNAME;

public class TOPBlockInfoProvider implements IBlockDisplayOverride {

    @Override
    public boolean overrideStandardInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, PlayerEntity playerEntity, World world, BlockState blockState, IProbeHitData iProbeHitData) {

        if (probeMode != ProbeMode.DEBUG) {

            if (world.getBlockState(iProbeHitData.getPos()).getBlock() instanceof CustomProbeInfo) {

                String modName = ModList.get().getModContainerById(ModConstants.MODID).get().getModInfo().getDisplayName();
                iProbeInfo.horizontal().item(iProbeHitData.getPickBlock()).vertical().text(CompoundText.create().name(blockState.getBlock().getTranslatedName())).vertical().text(CompoundText.create().info(MODNAME + modName));

                iProbeInfo = ((CustomProbeInfo) world.getBlockState(iProbeHitData.getPos()).getBlock()).getCustomProbeInfo(probeMode, playerEntity, world, blockState, iProbeHitData);
                return true;
            }
        }

        return false;
    }

}
