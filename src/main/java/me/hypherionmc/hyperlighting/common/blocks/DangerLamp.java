package me.hypherionmc.hyperlighting.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class DangerLamp extends SuspiciousLamp {

    public DangerLamp(String name, ItemGroup group) {
        super(name, group);
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        if (state.get(POWERED) && FMLEnvironment.dist.isClient()) {
            return 15;
        } else {
            return 0;
        }
    }
}
