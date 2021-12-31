package me.hypherionmc.hyperlighting.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/***
 * Used to allow the Lighter Tool to change the state of a block
 */
public interface Lightable {

    void toggleLight(Level worldIn, BlockState state, BlockPos pos);

}
