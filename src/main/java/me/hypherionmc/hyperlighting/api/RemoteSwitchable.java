package me.hypherionmc.hyperlighting.api;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface RemoteSwitchable {

    BlockState remoteSwitched(BlockState state, BlockPos pos, World world);

    boolean getPoweredState(BlockState state);

}
