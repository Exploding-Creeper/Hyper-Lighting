package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.util.CustomRenderType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class LavaLamp extends Block implements CustomRenderType {

    public static BooleanProperty LIT = BlockStateProperties.LIT;
    private static final VoxelShape BOUNDING_BOX = Block.makeCuboidShape(6,0,6,10,12,10);

    public LavaLamp(String name, ItemGroup group) {
        super(Properties.create(Material.FIRE).tickRandomly());
        this.setDefaultState(this.getDefaultState().with(LIT, false));

        HLItems.ITEMS.register(name, () -> new BlockItem(this, new Item.Properties().group(group)));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return BOUNDING_BOX;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(LIT);
        super.fillStateContainer(builder);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand handIn, BlockRayTraceResult hitFace) {
        if (!worldIn.isRemote) {
            state = state.cycleValue(LIT);
            worldIn.setBlockState(pos, state, 2);
        }
        return ActionResultType.CONSUME;
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        if (state.get(LIT)) {
            return 10;
        }
        return 0;
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.getCutoutMipped();
    }

}
