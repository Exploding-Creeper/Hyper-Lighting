package me.hypherionmc.hyperlighting.api;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface RemoteSwitchable {

    BlockState remoteSwitched(BlockState state, BlockPos pos, Level world);
    boolean getPoweredState(BlockState state);

}
