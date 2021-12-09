package me.hypherionmc.hyperlighting.common.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;

import java.util.function.ToIntFunction;

public class DangerLamp extends SuspiciousLamp {

    public DangerLamp(String name, ItemGroup group) {
        super(name, group, Settings.of(Material.GLASS).sounds(BlockSoundGroup.GLASS).ticksRandomly().luminance(getLightLevel()));
    }

    public static ToIntFunction<BlockState> getLightLevel() {
        return state -> (state.get(POWERED) && FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) ? 15 : 0;
    }

}
