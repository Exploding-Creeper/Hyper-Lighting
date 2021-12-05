package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.HyperLighting;
import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.api.DyeAble;
import me.hypherionmc.hyperlighting.api.RemoteSwitchable;
import me.hypherionmc.hyperlighting.common.config.HyperLightingConfig;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.items.BlockItemColor;
import me.hypherionmc.hyperlighting.common.network.PacketHandler;
import me.hypherionmc.hyperlighting.common.network.packets.OpenGUIPacket;
import me.hypherionmc.hyperlighting.common.tile.TileBatteryNeon;
import net.minecraft.ChatFormatting;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;

public class BatteryNeon extends BaseEntityBlock implements RemoteSwitchable, DyeAble {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    private static final VoxelShape DOWN_BOUNDING_BOX = Block.box(0,0,7.008,16,3.008,8.992);
    private static final VoxelShape UP_BOUNDING_BOX = Block.box(0,12.8,7.008,16,16,8.992);
    private static final VoxelShape SOUTH_BOUNDING_BOX = Block.box(0,7.008,12.992,16,8.992,16);
    private static final VoxelShape EAST_BOUNDING_BOX = Block.box(0,7.008,16,12.8,8.992,16);
    private static final VoxelShape WEST_BOUNDING_BOX = Block.box(0,7.008,0,3.2,8.992,16);
    private static final VoxelShape NORTH_BOUNDING_BOX = Block.box(0,7.008,0.336,16,8.992,3.328);

    public BatteryNeon(String name) {
        super(Properties.of(Material.GLASS).sound(SoundType.GLASS));
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(LIT, HyperLightingConfig.batteryOnByDefault.get()));

        HLItems.ITEMS.register(name, () -> new BlockItemColor(this, new Item.Properties().tab(HyperLighting.mainTab)));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        switch (state.getValue(FACING)) {
            case UP:
                return DOWN_BOUNDING_BOX;
            case DOWN:
            default:
                return UP_BOUNDING_BOX;
            case NORTH:
                return SOUTH_BOUNDING_BOX;
            case EAST:
                return WEST_BOUNDING_BOX;
            case WEST:
                return EAST_BOUNDING_BOX;
            case SOUTH:
                return NORTH_BOUNDING_BOX;
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!worldIn.isClientSide && !Screen.hasControlDown()) {

            if (state.getValue(LIT)) {
                BlockState oldState = state;
                state = state.setValue(LIT, false);
                worldIn.setBlock(pos, state, 2);
                worldIn.sendBlockUpdated(pos, oldState, state, 4);
                return InteractionResult.CONSUME;
            } else {
                if (worldIn.getBlockEntity(pos) != null && worldIn.getBlockEntity(pos) instanceof TileBatteryNeon && ((TileBatteryNeon)worldIn.getBlockEntity(pos)).getPowerLevel() > 0) {
                    BlockState oldState = state;
                    state = state.setValue(LIT, true);
                    worldIn.setBlock(pos, state, 2);
                    worldIn.sendBlockUpdated(pos, oldState, state, 4);
                    return InteractionResult.CONSUME;
                } else {
                    BlockState oldState = state;
                    state = state.setValue(LIT, false);
                    worldIn.setBlock(pos, state, 2);
                    worldIn.sendBlockUpdated(pos, oldState, state, 4);
                    player.displayClientMessage(new TextComponent("Out of power"), true);
                    return InteractionResult.CONSUME;
                }

            }

        } else {
            if (Screen.hasControlDown()) {
                OpenGUIPacket openGUIPacket = new OpenGUIPacket(ModConstants.SOLAR_GUI, pos);
                PacketHandler.INSTANCE.sendToServer(openGUIPacket);
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, FACING);
        super.createBlockStateDefinition(builder);
    }


    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return state.getValue(LIT) ? 15 : 0;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace()).setValue(LIT, false);

    }

    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    @Override
    public BlockColor dyeHandler() {
        return (state, world, pos, tintIndex) -> {
            if (state.getValue(LIT)) {
                if (world != null && world.getBlockEntity(pos) instanceof TileBatteryNeon) {
                    TileBatteryNeon tileBatteryNeon = (TileBatteryNeon) world.getBlockEntity(pos);
                    return (tileBatteryNeon.getDyeHandler().getStackInSlot(0).getItem() instanceof DyeItem ? ((DyeItem) tileBatteryNeon.getDyeHandler().getStackInSlot(0).getItem()).getDyeColor().getMaterialColor().col : DyeColor.WHITE.getTextColor());
                }
            } else {
                return DyeColor.BLACK.getMaterialColor().col;
            }
            return DyeColor.BLACK.getMaterialColor().col;
        };
    }

    @Override
    public DyeColor defaultDyeColor() {
        return DyeColor.WHITE;
    }

    @Override
    public BlockState remoteSwitched(BlockState state, BlockPos pos, Level world) {
        return state.setValue(LIT, !state.getValue(LIT));
    }

    @Override
    public boolean getPoweredState(BlockState state) {
        return state.getValue(LIT);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TextComponent(ChatFormatting.YELLOW + "Dyable"));
        tooltip.add(new TextComponent(ChatFormatting.GREEN + "Color: " + defaultDyeColor().name()));
        tooltip.add(new TextComponent(ChatFormatting.BLUE + "Colored Lighting Supported"));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileBatteryNeon(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        if (level.isClientSide()) {
            return null;
        }
        return (level1, blockPos, blockState, t) -> {
            if (t instanceof TileBatteryNeon tile) {
                tile.serverTick();
            }
        };
    }

}
