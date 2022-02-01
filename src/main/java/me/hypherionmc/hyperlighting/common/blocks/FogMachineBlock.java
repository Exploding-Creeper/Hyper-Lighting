package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.HyperLightingFabric;
import me.hypherionmc.hyperlighting.api.ISidedTickable;
import me.hypherionmc.hyperlighting.common.blockentities.FogMachineBlockEntity;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.network.NetworkHandler;
import me.hypherionmc.hyperlighting.utils.CustomRenderType;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FogMachineBlock extends Block implements BlockEntityProvider, CustomRenderType {

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    private static final VoxelShape BOUNDING_BOX = Block.createCuboidShape(4.864, 0, 1.936, 10.864, 5.248, 14.432);
    private static final VoxelShape BOUNDING_BOX_INVERTED = Block.createCuboidShape(1.936, 0, 5.136, 14.432, 5.248, 11.136);

    public FogMachineBlock(String name) {
        super(Settings.of(Material.METAL));
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));

        HLItems.register(name, new BlockItem(this, new FabricItemSettings().group(HyperLightingFabric.machinesTab)));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FogMachineBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return (level1, blockPos, blockState, t) -> {
            if (t instanceof ISidedTickable tile) {
                if (level1.isClient) {
                    tile.tickClient();
                } else {
                    tile.tickServer();
                }
            }
        };
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        super.appendProperties(builder);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(FACING) == Direction.NORTH || state.get(FACING) == Direction.SOUTH) {
            return BOUNDING_BOX;
        } else {
            return BOUNDING_BOX_INVERTED;
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            if (!(world.getBlockEntity(hit.getBlockPos()) instanceof FogMachineBlockEntity fogMachine && fogMachine.onPlayerUse(player, hand))) {
                NetworkHandler.sendOpenGuiPacket((ServerPlayerEntity) player, pos);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayer().getHorizontalFacing().getOpposite());
    }

    @Override
    public RenderLayer getCustomRenderType() {
        return RenderLayer.getCutoutMipped();
    }
}
