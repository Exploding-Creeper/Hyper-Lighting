package me.hypherionmc.hyperlighting.common.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import me.hypherionmc.hyperlighting.HyperLightingFabric;
import me.hypherionmc.hyperlighting.api.DyeAble;
import me.hypherionmc.hyperlighting.api.Lightable;
import me.hypherionmc.hyperlighting.common.handlers.ParticleRegistryHandler;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.items.BlockItemWithColoredLight;
import me.hypherionmc.hyperlighting.utils.BlockUtils;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.*;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
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
import java.util.Map;
import java.util.Random;

public class AdvancedTorchBlock extends HorizontalFacingBlock implements Lightable, DyeAble {

    public static final BooleanProperty LIT = Properties.POWERED;
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.of("color", DyeColor.class);
    public static final EnumProperty<WallMountLocation> ATTACH_FACE = EnumProperty.of("face", WallMountLocation.class, WallMountLocation.FLOOR, WallMountLocation.WALL);
    private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.createCuboidShape(5.5D, 3.0D, 11.0D, 10.5D, 13.0D, 16.0D), Direction.SOUTH, Block.createCuboidShape(5.5D, 3.0D, 0.0D, 10.5D, 13.0D, 5.0D), Direction.WEST, Block.createCuboidShape(11.0D, 3.0D, 5.5D, 16.0D, 13.0D, 10.5D), Direction.EAST, Block.createCuboidShape(0.0D, 3.0D, 5.5D, 5.0D, 13.0D, 10.5D), Direction.UP, Block.createCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 10.0D, 10.0D)));

    public AdvancedTorchBlock(String name, DyeColor color, ItemGroup group) {
        super(Settings.of(Material.DECORATION).noCollision().breakInstantly().luminance(BlockUtils.createLightLevelFromPoweredBlockState(15)));
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(LIT, HyperLightingFabric.hyperLightingConfig.torchOnByDefault).with(COLOR, color));

        HLItems.register(name, new BlockItemWithColoredLight(this, new FabricItemSettings().group(group)));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(ATTACH_FACE)) {
            case FLOOR -> SHAPES.get(Direction.UP);
            case WALL -> SHAPES.get(state.get(FACING));
            case CEILING -> null;
        };
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT, ATTACH_FACE, FACING, COLOR);
        super.appendProperties(builder);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        Direction direction = context.getSide();
        if (direction == Direction.UP) {
            return this.getDefaultState().with(ATTACH_FACE, WallMountLocation.FLOOR);
        } else {
            return this.getDefaultState().with(ATTACH_FACE, WallMountLocation.WALL).with(FACING, direction);
        }
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
    public void randomDisplayTick(BlockState stateIn, World worldIn, BlockPos pos, Random random) {
        if (stateIn.get(LIT)) {

            DyeColor color = stateIn.get(COLOR);

            if (stateIn.get(ATTACH_FACE) == WallMountLocation.FLOOR) {
                double d0 = (double) pos.getX() + 0.5D;
                double d1 = (double) pos.getY() + 0.7D;
                double d2 = (double) pos.getZ() + 0.5D;
                worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);

                // xSpeed, ySpeed and zSpeed here is used to pass color data. This isn't the proper way, but I don't wanna add a bunch of extra code for something so simple
                worldIn.addParticle(ParticleRegistryHandler.CUSTOM_FLAME, d0, d1, d2, color.getColorComponents()[0], color.getColorComponents()[1], color.getColorComponents()[2]);
            } else {
                Direction direction = stateIn.get(FACING);
                double d0 = (double) pos.getX() + 0.5D;
                double d1 = (double) pos.getY() + 0.7D;
                double d2 = (double) pos.getZ() + 0.5D;
                double d3 = 0.22D;
                double d4 = 0.27D;
                Direction direction1 = direction.getOpposite();
                worldIn.addParticle(ParticleTypes.SMOKE, d0 + 0.27D * (double) direction1.getOffsetX(), d1 + 0.22D, d2 + 0.27D * (double) direction1.getOffsetZ(), 0.0D, 0.0D, 0.0D);

                // xSpeed, ySpeed and zSpeed here is used to pass color data. This isn't the proper way, but I don't wanna add a bunch of extra code for something so simple
                worldIn.addParticle(ParticleRegistryHandler.CUSTOM_FLAME, d0 + 0.27D * (double) direction1.getOffsetX(), d1 + 0.22D, d2 + 0.27D * (double) direction1.getOffsetZ(), color.getColorComponents()[0], color.getColorComponents()[1], color.getColorComponents()[2]);
            }
        }
    }
}
