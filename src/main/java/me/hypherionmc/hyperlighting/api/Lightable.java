package me.hypherionmc.hyperlighting.api;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/***
 * Used to allow the Lighter Tool to change the state of a block
 */
public interface Lightable {

    public void toggleLight(World worldIn, BlockState state, BlockPos pos);

}
