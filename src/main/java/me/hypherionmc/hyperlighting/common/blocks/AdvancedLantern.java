package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.HyperLightingFabric;
import me.hypherionmc.hyperlighting.api.DyeAble;
import me.hypherionmc.hyperlighting.api.Lightable;
import me.hypherionmc.hyperlighting.common.handlers.ParticleRegistryHandler;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.items.BlockItemWithColoredLight;
import me.hypherionmc.hyperlighting.utils.BlockUtils;
import me.hypherionmc.hyperlighting.utils.CustomRenderType;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.*;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class AdvancedLantern extends WallMountedBlock implements DyeAble, Lightable, CustomRenderType {

    //Properties
    public static final BooleanProperty LIT = Properties.LIT;
    public static final DirectionProperty HORIZONTAL_FACING = HorizontalFacingBlock.FACING;
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.of("color", DyeColor.class);

    //Bounding Boxes
    private static final VoxelShape BB_TOP = Block.createCuboidShape(4.992, 0, 4, 11.008, 11.008, 11.008);
    private static final VoxelShape BB_NORTH = Block.createCuboidShape(4.992, 3.008, 8, 11.008, 16, 16.992);
    private static final VoxelShape BB_SOUTH = Block.createCuboidShape(4.992, 3.008, -0.992, 11.008, 16, 8);
    private static final VoxelShape BB_EAST = Block.createCuboidShape(-0.992, 3.008, 4.992, 8, 16, 11.008);
    private static final VoxelShape BB_WEST = Block.createCuboidShape(8, 3.008, 4.992, 16.992, 16, 11.008);
    private static final VoxelShape BB_CEILING = Block.createCuboidShape(4.912, 3.008, 5.088, 11.904, 16, 11.088);

    public AdvancedLantern(String name, DyeColor color, ItemGroup group) {
        super(Settings.of(Material.DECORATION).noCollision().breakInstantly().nonOpaque().luminance(BlockUtils.createLightLevelFromLitBlockState(15)));
        this.setDefaultState(this.getDefaultState().with(HORIZONTAL_FACING, Direction.NORTH).with(LIT, HyperLightingFabric.hyperLightingConfig.lanternOnByDefault).with(COLOR, color));

        HLItems.register(name, new BlockItemWithColoredLight(this, new FabricItemSettings().group(group)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT, HORIZONTAL_FACING, FACE, COLOR);
        super.appendProperties(builder);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACE)) {
            case FLOOR -> BB_TOP; //default -> BB_TOP;
            case WALL -> switch (state.get(HORIZONTAL_FACING)) {
                case EAST -> BB_EAST;
                case WEST -> BB_WEST;
                case SOUTH -> BB_SOUTH;
                case DOWN -> null;
                case UP -> null;
                case NORTH -> BB_NORTH;
                //case default -> BB_NORTH;
            };
            case CEILING -> BB_CEILING;
        };
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facing, BlockState neighborState, WorldAccess worldIn, BlockPos currentPos, BlockPos neighborPos) {
        if (facing == Direction.DOWN && !this.isValidPosition(stateIn, worldIn, currentPos, facing)) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(stateIn, facing, neighborState, worldIn, currentPos, neighborPos);
    }

    public boolean isValidPosition(BlockState state, WorldView world, BlockPos pos, Direction direction) {
        return sideCoversSmallSquare(world, pos, direction);
    }

    @Override
    public void toggleLight(World worldIn, BlockState state, BlockPos pos) {
        state = state.with(LIT, !state.get(LIT));
        worldIn.setBlockState(pos, state, 2);
        if (!state.get(LIT)) {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.3f, 1.0f);
        }
        worldIn.updateNeighbors(pos, this);
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
        return this.getDefaultState().get(COLOR);
    }

    @Override
    public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockHitResult hit) {
        if (!worldIn.isClient) {

            if (!player.getStackInHand(handIn).isEmpty() && player.getStackInHand(handIn).getItem() instanceof DyeItem) {
                state = state.with(COLOR, ((DyeItem) player.getStackInHand(handIn).getItem()).getColor());
                worldIn.setBlockState(pos, state, 3);
                worldIn.updateListeners(pos, state, state, 3);

                if (!player.isCreative()) {
                    ItemStack stack = player.getStackInHand(handIn);
                    stack.decrement(1);
                    player.setStackInHand(handIn, stack);
                }

                return ActionResult.CONSUME;
            }

        }
        return ActionResult.PASS;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(new LiteralText(Formatting.YELLOW + "Dyable"));
        tooltip.add(new LiteralText(Formatting.GREEN + "Color: " + defaultDyeColor().name()));
        tooltip.add(new LiteralText(Formatting.BLUE + "Colored Lighting Supported"));
        super.appendTooltip(stack, world, tooltip, options);
    }

    @Override
    public void randomDisplayTick(BlockState state, World worldIn, BlockPos pos, Random random) {
        if (worldIn.isClient && state.get(LIT)) {
            DyeColor color = state.get(COLOR);
            Direction direction = state.get(HORIZONTAL_FACING);

            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY() + 0.7D;
            double d2 = (double) pos.getZ() + 0.5D;

            if (state.get(FACE) == WallMountLocation.WALL) {
                Direction direction1 = direction.getOpposite();
                worldIn.addParticle(ParticleTypes.SMOKE, d0 + 0.27D * (double) direction1.getOffsetX(), d1, d2 + 0.27D * (double) direction1.getOffsetZ(), 0.0D, 0.0D, 0.0D);
                worldIn.addParticle(ParticleRegistryHandler.CUSTOM_FLAME, d0 + 0.27D * (double) direction1.getOffsetX(), d1 - 0.3D, d2 + 0.27D * (double) direction1.getOffsetZ(), color.getColorComponents()[0], color.getColorComponents()[1], color.getColorComponents()[2]);
            } else if (state.get(FACE) == WallMountLocation.FLOOR) {
                worldIn.addParticle(ParticleTypes.SMOKE, d0, d1 - 0.3D, d2, 0.0D, 0.0D, 0.0D);
                worldIn.addParticle(ParticleRegistryHandler.CUSTOM_FLAME, d0, d1 - 0.5D, d2, color.getColorComponents()[0], color.getColorComponents()[1], color.getColorComponents()[2]);
            } else if (state.get(FACE) == WallMountLocation.CEILING) {
                worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                worldIn.addParticle(ParticleRegistryHandler.CUSTOM_FLAME, d0, d1 - 0.3D, d2, color.getColorComponents()[0], color.getColorComponents()[1], color.getColorComponents()[2]);
            }
        }
    }

    @Override
    public RenderLayer getCustomRenderType() {
        return RenderLayer.getCutoutMipped();
    }
}
