package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.util.CustomRenderType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class UnclearGlass extends Block implements CustomRenderType {

    public UnclearGlass(String name, ItemGroup group) {
        super(Properties.create(Material.GLASS).sound(SoundType.GLASS).hardnessAndResistance(0.3F).notSolid());

        HLItems.ITEMS.register(name, () -> new BlockItem(this, new Item.Properties().group(group)));
    }

    @Override
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return worldIn.getMaxLightLevel();
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.getCutoutMipped();
    }

    @Override
    public boolean isTransparent(BlockState state) {
        return false;
    }

    @Override
    public VoxelShape getRayTraceShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.empty();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
        return adjacentBlockState.matchesBlock(this) ? true : super.isSideInvisible(state, adjacentBlockState, side);
    }

}
