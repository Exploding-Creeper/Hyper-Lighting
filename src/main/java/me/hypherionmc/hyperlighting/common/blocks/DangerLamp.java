package me.hypherionmc.hyperlighting.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class DangerLamp extends SuspiciousLamp {

    public DangerLamp(String name, CreativeModeTab group) {
        super(name, group);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        if (state.getValue(POWERED) && FMLEnvironment.dist.isClient()) {
            return 15;
        } else {
            return 0;
        }
    }
}
