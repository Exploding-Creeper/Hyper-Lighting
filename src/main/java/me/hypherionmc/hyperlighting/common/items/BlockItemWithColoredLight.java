package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.hyperlighting.api.DyeAble;
import me.hypherionmc.hyperlighting.util.ModUtils;
import me.hypherionmc.rgblib.api.APIUtils;
import me.hypherionmc.rgblib.api.ColoredLightManager;
import me.hypherionmc.rgblib.api.RGBLight;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;

public class BlockItemWithColoredLight extends BlockItemColor {

    public BlockItemWithColoredLight(Block blockIn, Properties builder) {
        super(blockIn, builder);

        if (ModUtils.isRGBLibPresent()) {
            ColoredLightManager.registerProvider(this, this::produceColoredLight);
        }
    }

    private RGBLight produceColoredLight(Entity entity, ItemStack stack) {
        if (stack.getItem() instanceof BlockItemWithColoredLight) {
            DyeColor color = ((DyeAble)((BlockItemWithColoredLight) stack.getItem()).getBlock()).defaultDyeColor();
            return RGBLight.builder().pos(APIUtils.entityPos(entity)).color(color.getColorValue(), false).radius(14).build();
        }
        return null;
    }
}
