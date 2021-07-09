package me.hypherionmc.hyperlighting.api;

import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.DyeColor;

public interface DyeAble {

    public IBlockColor dyeHandler();
    public DyeColor defaultDyeColor();

}
