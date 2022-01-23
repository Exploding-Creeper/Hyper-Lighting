package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.HyperLightingFabric;
import me.hypherionmc.hyperlighting.api.DyeAble;
import me.hypherionmc.hyperlighting.api.RemoteSwitchable;
import me.hypherionmc.hyperlighting.common.blockentities.BatteryNeonBlockEntity;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.items.BlockItemColor;
import me.hypherionmc.hyperlighting.common.network.NetworkHandler;
import me.hypherionmc.hyperlighting.utils.BlockUtils;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BatteryNeon extends Block implements BlockEntityProvider, RemoteSwitchable, DyeAble {

    public static final BooleanProperty LIT = Properties.LIT;
    public static final DirectionProperty FACING = Properties.FACING;
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.of("color", DyeColor.class);

    private static final VoxelShape DOWN_BOUNDING_BOX = Block.createCuboidShape(0, 0, 7.008, 16, 3.008, 8.992);
    private static final VoxelShape UP_BOUNDING_BOX = Block.createCuboidShape(0, 12.8, 7.008, 16, 16, 8.992);
    private static final VoxelShape SOUTH_BOUNDING_BOX = Block.createCuboidShape(0, 7.008, 12.992, 16, 8.992, 16);
    private static final VoxelShape EAST_BOUNDING_BOX = Block.createCuboidShape(0, 7.008, 16, 12.8, 8.992, 16);
    private static final VoxelShape WEST_BOUNDING_BOX = Block.createCuboidShape(0, 7.008, 0, 3.2, 8.992, 16);
    private static final VoxelShape NORTH_BOUNDING_BOX = Block.createCuboidShape(0, 7.008, 0.336, 16, 8.992, 3.328);

    public BatteryNeon(String name) {
        super(Settings.of(Material.GLASS).sounds(BlockSoundGroup.GLASS).luminance(BlockUtils.createLightLevelFromLitBlockState(15)));
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(LIT, HyperLightingFabric.hyperLightingConfig.batteryOnByDefault).with(COLOR, DyeColor.WHITE));

        HLItems.register(name, new BlockItemColor(this, new FabricItemSettings().group(HyperLightingFabric.mainTab)));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case UP -> DOWN_BOUNDING_BOX;
            case DOWN -> UP_BOUNDING_BOX;
            case NORTH -> SOUTH_BOUNDING_BOX;
            case EAST -> WEST_BOUNDING_BOX;
            case WEST -> EAST_BOUNDING_BOX;
            case SOUTH -> NORTH_BOUNDING_BOX;
        };
    }

    @Override
    public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (worldIn.isClient && Screen.hasControlDown()) {
            NetworkHandler.sendOpenGuiPacket(pos);
            return ActionResult.SUCCESS;
        } else if (!worldIn.isClient) {
            if (state.get(LIT)) {
                BlockState oldState = state;
                state = state.with(LIT, false);
                worldIn.setBlockState(pos, state, 2);
                worldIn.updateListeners(pos, oldState, state, 4);
                return ActionResult.SUCCESS;
            } else {
                if (worldIn.getBlockEntity(pos) != null && worldIn.getBlockEntity(pos) instanceof BatteryNeonBlockEntity te && te.getPowerLevel() > 0) {
                    BlockState oldState = state;
                    state = state.with(LIT, true);
                    worldIn.setBlockState(pos, state, 2);
                    worldIn.updateListeners(pos, oldState, state, 4);
                    return ActionResult.SUCCESS;
                } else {
                    BlockState oldState = state;
                    state = state.with(LIT, false);
                    worldIn.setBlockState(pos, state, 2);
                    worldIn.updateListeners(pos, oldState, state, 4);
                    player.sendMessage(new LiteralText("Out of power"), true);
                    return ActionResult.SUCCESS;
                }
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT, FACING, COLOR);
        super.appendProperties(builder);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getSide()).with(LIT, HyperLightingFabric.hyperLightingConfig.batteryOnByDefault).with(COLOR, DyeColor.WHITE);
    }

    @Override
    public BlockColorProvider dyeHandler() {
        return (state, world, pos, tintIndex) -> {
            if (state.get(LIT)) {
                return state.get(COLOR).getMapColor().color;
            } else {
                return DyeColor.BLACK.getMapColor().color;
            }
        };
    }

    @Override
    public DyeColor defaultDyeColor() {
        return DyeColor.WHITE;
    }

    @Override
    public BlockState remoteSwitched(BlockState state, BlockPos pos, World world) {
        return state.with(LIT, !state.get(LIT));
    }

    @Override
    public boolean getPoweredState(BlockState state) {
        return state.get(LIT);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(new LiteralText(Formatting.YELLOW + "Dyable"));
        tooltip.add(new LiteralText(Formatting.GREEN + "Color: " + defaultDyeColor().name()));
        tooltip.add(new LiteralText(Formatting.BLUE + "Colored Lighting Supported"));
        super.appendTooltip(stack, world, tooltip, options);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BatteryNeonBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        if (level.isClient()) {
            return null;
        }
        return (level1, blockPos, blockState, t) -> {
            if (t instanceof BatteryNeonBlockEntity tile) {
                tile.serverTick();
            }
        };
    }

    @Override
    public void onStateReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof BatteryNeonBlockEntity te) {
                ItemScatterer.spawn(worldIn, pos, te.getInventory());
            }
            super.onStateReplaced(state, worldIn, pos, newState, moved);
        }
    }
}
