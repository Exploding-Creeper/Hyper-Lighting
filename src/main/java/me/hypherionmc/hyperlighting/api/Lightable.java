package me.hypherionmc.hyperlighting.api;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/***
 * Used to allow the Lighter Tool to change the state of a block
 */
public interface Lightable {

    public void toggleLight(Level worldIn, BlockState state, BlockPos pos);

}
