package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.hyperlighting.api.ItemDyable;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ColoredWaterBucketItem extends BucketItem implements ItemDyable {

    private final DyeColor color;
    private final boolean isGlowing;

    public ColoredWaterBucketItem(Supplier<? extends FlowingFluid> supplier, Item.Properties settings, DyeColor color, boolean isGlowing) {
        super(supplier, settings);
        this.color = color;
        this.isGlowing = isGlowing;
    }


    @OnlyIn(Dist.CLIENT)
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

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if (this.getClass() == ColoredWaterBucketItem.class)
            return new net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper(stack);
        else
        return super.initCapabilities(stack, nbt);
    }
}
