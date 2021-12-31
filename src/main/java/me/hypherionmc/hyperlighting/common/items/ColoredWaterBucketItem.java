package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.hyperlighting.api.ItemDyable;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.function.Supplier;

public class ColoredWaterBucketItem extends BucketItem implements ItemDyable {

    private final DyeColor color;
    private final boolean isGlowing;

    public ColoredWaterBucketItem(Supplier<? extends FlowingFluid> supplier, Item.Properties settings, DyeColor color, boolean isGlowing) {
        super(supplier, settings);
        this.color = color;
        this.isGlowing = isGlowing;
    }


    @Override
    public ItemColor dyeHandler() {
        return new ItemColor() {
            @Override
            public int getColor(ItemStack stack, int tintIndex) {
                return color.getMaterialColor().col;
            }
        };
    }

    @Override
    public boolean isFoil(ItemStack p_41453_) {
        return isGlowing;
    }

    public boolean isGlowing() {
        return isGlowing;
    }

    public DyeColor getColor() {
        return color;
    }
}
