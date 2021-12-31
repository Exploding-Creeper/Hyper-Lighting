package me.hypherionmc.hyperlighting.api;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface CustomProbeInfo {
    IProbeInfo getCustomProbeInfo(ProbeMode probeMode, Player playerEntity, Level world, BlockState blockState, IProbeHitData iProbeHitData);
}
