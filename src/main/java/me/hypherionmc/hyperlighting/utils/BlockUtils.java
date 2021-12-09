package me.hypherionmc.hyperlighting.utils;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;

import java.util.function.ToIntFunction;

public class BlockUtils {

    public static ToIntFunction<BlockState> createLightLevelFromLitBlockState(int litLevel) {
        return state -> state.get(Properties.LIT) ? litLevel : 0;
    }

    public static ToIntFunction<BlockState> createLightLevelFromPoweredBlockState(int litLevel) {
        return state -> state.get(Properties.POWERED) ? litLevel : 0;
    }

}
