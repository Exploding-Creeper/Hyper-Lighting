package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.HyperLightingFabric;
import me.hypherionmc.hyperlighting.common.blockentities.SwitchBoardBlockEntity;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SwitchBoard extends Block implements BlockEntityProvider {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    private static final VoxelShape BOUNDING_BOX = Block.createCuboidShape(0, 0, 12.992, 16, 16, 16);
    private static final VoxelShape AABB_SOUTH = Block.createCuboidShape(0, 0, 0, 16, 16, 3.008);
    private static final VoxelShape AABB_EAST = Block.createCuboidShape(0, 0, 0, 3.008, 16, 16);
    private static final VoxelShape AABB_WEST = Block.createCuboidShape(12.992, 0, 0, 16, 16, 16);

    public SwitchBoard(String name) {
        super(Settings.of(Material.DECORATION));
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));

        HLItems.register(name, new BlockItem(this, new FabricItemSettings().group(HyperLightingFabric.machinesTab)));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            NamedScreenHandlerFactory screenHandlerFactory = blockEntity instanceof NamedScreenHandlerFactory ? (NamedScreenHandlerFactory) blockEntity : null;
            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case EAST -> AABB_EAST;
            case WEST -> AABB_WEST;
            case SOUTH -> AABB_SOUTH;
            case DOWN -> null;
            case UP -> null;
            case NORTH -> BOUNDING_BOX;
        };
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayer().getHorizontalFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        super.appendProperties(builder);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof SwitchBoardBlockEntity te) {
                ItemScatterer.spawn(world, pos, te);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SwitchBoardBlockEntity(pos, state);
    }

}
