package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.HyperLightingFabric;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class TikiTorchStick extends Block {

    private static final VoxelShape BOUNDING_BOX = Block.createCuboidShape(7.008, 0, 7.008, 8.992, 16, 8.992);

    public TikiTorchStick(String name) {
        super(Settings.of(Material.WOOD));

        HLItems.register(name, new BlockItem(this, new FabricItemSettings().group(HyperLightingFabric.mainTab)));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return BOUNDING_BOX;
    }
}
