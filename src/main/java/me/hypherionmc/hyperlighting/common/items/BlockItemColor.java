package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.hyperlighting.api.DyeAble;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class BlockItemColor extends BlockItem implements IItemColor {

    public BlockItemColor(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Override
    public int getColor(ItemStack stack, int p_getColor_2_) {
        return getColorFromStack(stack);
    }

    private int getColorFromStack(ItemStack stack) {
        if (stack.getItem() instanceof BlockItemColor) {
            BlockItemColor blockItemColor = (BlockItemColor) stack.getItem();
            if (blockItemColor.getBlock() instanceof DyeAble) {
                DyeAble dyeAble = (DyeAble) blockItemColor.getBlock();
                return dyeAble.defaultDyeColor().getColorValue();
            }
        }
        return 0;
    }

}
