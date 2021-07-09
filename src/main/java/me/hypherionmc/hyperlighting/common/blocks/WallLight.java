package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.common.init.HLItems;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class WallLight extends AbstractButtonBlock {

    public WallLight(String name, ItemGroup group) {
        super(false, Properties.create(Material.ROCK).zeroHardnessAndResistance().doesNotBlockMovement());
        HLItems.ITEMS.register(name, () -> new BlockItem(this, new Item.Properties().group(group)));
    }

    @Override
    protected SoundEvent getSoundEvent(boolean isOn) {
        return isOn ? SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON : SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF;
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return 8;
    }
}
