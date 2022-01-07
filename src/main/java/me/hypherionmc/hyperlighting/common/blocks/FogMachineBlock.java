package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.HyperLighting;
import me.hypherionmc.hyperlighting.api.ISidedTickable;
import me.hypherionmc.hyperlighting.common.containers.ContainerFogMachine;
import me.hypherionmc.hyperlighting.common.containers.ContainerSwitchBoard;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.tile.TileFogMachine;
import me.hypherionmc.hyperlighting.common.tile.TileSwitchBoard;
import me.hypherionmc.hyperlighting.util.CustomRenderType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class FogMachineBlock extends BaseEntityBlock implements CustomRenderType {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape BOUNDING_BOX = Block.box(4.864,0,1.936,10.864,5.248,14.432);
    private static final VoxelShape BOUNDING_BOX_INVERTED = Block.box(1.936,0,5.136,14.432,5.248,11.136);

    public FogMachineBlock(String name) {
        super(Properties.of(Material.HEAVY_METAL));
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));

        HLItems.ITEMS.register(name, () -> new BlockItem(this, new Item.Properties().tab(HyperLighting.machinesTab)));
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.cutoutMipped();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TileFogMachine(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return (level1, blockPos, blockState, t) -> {
            if (t instanceof ISidedTickable tile) {
                if (level1.isClientSide()) {
                    tile.tickClient();
                } else {
                    tile.tickServer();
                }
            }
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
        super.createBlockStateDefinition(pBuilder);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (pState.getValue(FACING) == Direction.NORTH || pState.getValue(FACING) == Direction.SOUTH) {
            return BOUNDING_BOX;
        } else {
            return BOUNDING_BOX_INVERTED;
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide) {
            if (!FluidUtil.interactWithFluidHandler(pPlayer, pHand, pLevel, pPos, pHit.getDirection())) {
                if (pLevel.getBlockEntity(pPos) instanceof TileFogMachine tileFogMachine) {
                    MenuProvider containerProvider = new MenuProvider() {
                        @Override
                        public Component getDisplayName() {
                            return new TranslatableComponent("container.fogmachine");
                        }

                        @Override
                        public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
                            return new ContainerFogMachine(i, pLevel, pPos, playerInventory, playerEntity);
                        }
                    };
                    NetworkHooks.openGui((ServerPlayer) pPlayer, containerProvider, tileFogMachine.getBlockPos());
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }
}
