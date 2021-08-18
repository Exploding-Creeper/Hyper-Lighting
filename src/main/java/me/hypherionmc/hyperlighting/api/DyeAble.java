package me.hypherionmc.hyperlighting.api;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.world.item.DyeColor;

public interface DyeAble {

    public BlockColor dyeHandler();
    public DyeColor defaultDyeColor();

}
