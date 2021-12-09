package me.hypherionmc.hyperlighting.api;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface Lightable {

    void toggleLight(World world, BlockState state, BlockPos pos);

}
