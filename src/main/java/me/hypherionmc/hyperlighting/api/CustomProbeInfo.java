package me.hypherionmc.hyperlighting.api;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface CustomProbeInfo {

    IProbeInfo getCustomProbeInfo(ProbeMode probeMode, PlayerEntity playerEntity, World world, BlockState blockState, IProbeHitData iProbeHitData);

}
