package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.hyperlighting.api.DyeAble;
import me.hypherionmc.hyperlighting.api.ItemDyable;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockItemColor extends BlockItem implements ItemDyable {

    public BlockItemColor(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    private int getColorFromStack(ItemStack stack) {
        if (stack.getItem() instanceof BlockItemColor) {
            BlockItemColor blockItemColor = (BlockItemColor) stack.getItem();
            if (blockItemColor.getBlock() instanceof DyeAble) {
                DyeAble dyeAble = (DyeAble) blockItemColor.getBlock();
                return dyeAble.defaultDyeColor().getMaterialColor().col;
            }
        }
        return 0;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ItemColor dyeHandler() {
        return new ItemColor() {
            @Override
            public int getColor(ItemStack p_getColor_1_, int p_getColor_2_) {
                return getColorFromStack(p_getColor_1_);
            }
        };
    }
}
