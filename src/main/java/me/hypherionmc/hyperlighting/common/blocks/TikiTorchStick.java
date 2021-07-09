package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.HyperLighting;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class TikiTorchStick extends Block {

    private static final VoxelShape BOUNDING_BOX = Block.makeCuboidShape(7.008,0,7.008,8.992,16,8.992);

    public TikiTorchStick(String name) {
        super(Properties.create(Material.WOOD));

        HLItems.ITEMS.register(name, () -> new BlockItem(this, new Item.Properties().group(HyperLighting.mainTab)));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return BOUNDING_BOX;
    }


}
