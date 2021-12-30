package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.hyperlighting.api.ItemDyable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;

public class ColoredWaterBucketItem extends BucketItem implements ItemDyable {

    private final DyeColor color;
    private final boolean isGlowing;

    public ColoredWaterBucketItem(Fluid fluid, Settings settings, DyeColor color, boolean isGlowing) {
        super(fluid, settings);
        this.color = color;
        this.isGlowing = isGlowing;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return isGlowing;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public ItemColorProvider dyeHandler() {
        return new ItemColorProvider() {
            @Override
            public int getColor(ItemStack stack, int tintIndex) {
                return color.getMapColor().color;
            }
        };
    }

    public boolean isGlowing() {
        return isGlowing;
    }

    public DyeColor getColor() {
        return color;
    }
}
