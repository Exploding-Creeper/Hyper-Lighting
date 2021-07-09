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
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.*;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BatteryNeon extends ContainerBlock implements RemoteSwitchable, DyeAble {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    private static final VoxelShape DOWN_BOUNDING_BOX = Block.makeCuboidShape(0,0,7.008,16,3.008,8.992);
    private static final VoxelShape UP_BOUNDING_BOX = Block.makeCuboidShape(0,12.8,7.008,16,16,8.992);
    private static final VoxelShape SOUTH_BOUNDING_BOX = Block.makeCuboidShape(0,7.008,12.992,16,8.992,16);
    private static final VoxelShape EAST_BOUNDING_BOX = Block.makeCuboidShape(16,7.008,0,12.8,8.992,16);
    private static final VoxelShape WEST_BOUNDING_BOX = Block.makeCuboidShape(0,7.008,0,3.2,8.992,16);
    private static final VoxelShape NORTH_BOUNDING_BOX = Block.makeCuboidShape(0,7.008,0.336,16,8.992,3.328);

    public BatteryNeon(String name) {
        super(Properties.create(Material.GLASS).sound(SoundType.GLASS));
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(LIT, HyperLightingConfig.batteryOnByDefault.get()));

        HLItems.ITEMS.register(name, () -> new BlockItemColor(this, new Item.Properties().group(HyperLighting.mainTab)));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.get(FACING)) {
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
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote && !Screen.hasControlDown()) {

            if (state.get(LIT)) {
                BlockState oldState = state;
                state = state.with(LIT, false);
                worldIn.setBlockState(pos, state, 3);
                worldIn.notifyBlockUpdate(pos, oldState, state, 4);
                return ActionResultType.CONSUME;
            } else {
                if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileBatteryNeon && ((TileBatteryNeon)worldIn.getTileEntity(pos)).getPowerLevel() > 0) {
                    BlockState oldState = state;
                    state = state.with(LIT, true);
                    worldIn.setBlockState(pos, state, 3);
                    worldIn.notifyBlockUpdate(pos, oldState, state, 4);
                    return ActionResultType.CONSUME;
                } else {
                    BlockState oldState = state;
                    state = state.with(LIT, false);
                    worldIn.setBlockState(pos, state, 3);
                    worldIn.notifyBlockUpdate(pos, oldState, state, 4);
                    player.sendStatusMessage(new StringTextComponent("Out of power"), true);
                    return ActionResultType.CONSUME;
                }

            }

        } else {
            if (Screen.hasControlDown()) {
                OpenGUIPacket openGUIPacket = new OpenGUIPacket(ModConstants.SOLAR_GUI, pos);
                PacketHandler.INSTANCE.sendToServer(openGUIPacket);
                return ActionResultType.CONSUME;
            }
        }
        return ActionResultType.CONSUME;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(LIT, FACING);
        super.fillStateContainer(builder);
    }


    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return state.get(LIT) ? 15 : 0;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getFace()).with(LIT, false);

    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileBatteryNeon();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    @Override
    public IBlockColor dyeHandler() {
        return (state, world, pos, tintIndex) -> {
            if (state.get(LIT)) {
                if (world != null && world.getTileEntity(pos) instanceof TileBatteryNeon) {
                    TileBatteryNeon tileBatteryNeon = (TileBatteryNeon) world.getTileEntity(pos);
                    return (tileBatteryNeon.getDyeHandler().getStackInSlot(0).getItem() instanceof DyeItem ? ((DyeItem) tileBatteryNeon.getDyeHandler().getStackInSlot(0).getItem()).getDyeColor().getColorValue() : DyeColor.WHITE.getColorValue());
                }
            } else {
                return DyeColor.BLACK.getColorValue();
            }
            return DyeColor.BLACK.getColorValue();
        };
    }

    @Override
    public DyeColor defaultDyeColor() {
        return DyeColor.WHITE;
    }

    @Override
    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid) {
        if (!world.isRemote) {
            if (world.getTileEntity(pos) instanceof TileBatteryNeon) {
                TileBatteryNeon tileBatteryNeon = (TileBatteryNeon) world.getTileEntity(pos);
                tileBatteryNeon.dropInventory();
            }
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
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
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new StringTextComponent(TextFormatting.YELLOW + "Dyable"));
        tooltip.add(new StringTextComponent(TextFormatting.GREEN + "Color: " + defaultDyeColor().name()));
        tooltip.add(new StringTextComponent(TextFormatting.BLUE + "Colored Lighting Supported"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}