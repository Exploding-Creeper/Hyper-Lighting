package me.hypherionmc.hyperlighting.api;

import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.util.DyeColor;

public interface DyeAble {

    BlockColorProvider dyeHandler();

    DyeColor defaultDyeColor();

}
