package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.hyperlighting.api.DyeAble;
import me.hypherionmc.hyperlighting.api.ItemDyable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class BlockItemColor extends BlockItem implements ItemDyable {

    public BlockItemColor(Block block, FabricItemSettings settings) {
        super(block, settings);
    }

    private int getColorFromStack(ItemStack stack) {
        if (stack.getItem() instanceof BlockItemColor blockItemColor) {
            if (blockItemColor.getBlock() instanceof DyeAble dyeAble) {
                return dyeAble.defaultDyeColor().getMapColor().color;
            }
        }
        return 0;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public ItemColorProvider dyeHandler() {
        return new ItemColorProvider() {
            @Override
            public int getColor(ItemStack stack, int tintIndex) {
                return getColorFromStack(stack);
            }
        };
    }

}
