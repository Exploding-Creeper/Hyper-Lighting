package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.HyperLightingFabric;
import me.hypherionmc.hyperlighting.api.DyeAble;
import me.hypherionmc.hyperlighting.api.Lightable;
import me.hypherionmc.hyperlighting.common.handlers.ParticleRegistryHandler;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.items.BlockItemWithColoredLight;
import me.hypherionmc.hyperlighting.utils.BlockUtils;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.*;
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
import java.util.function.Predicate;

public class AdvancedCandle extends Block implements DyeAble, Lightable {

    public static final BooleanProperty LIT = Properties.LIT;
    public static final DirectionProperty FACING = DirectionProperty.of("facing", new Predicate<Direction>() {
        @Override
        public boolean test(Direction direction) {
            return direction != Direction.DOWN;
        }
    });

    public static final EnumProperty<DyeColor> COLOR = EnumProperty.of("color", DyeColor.class);

    protected static final VoxelShape STANDING_AABB = Block.createCuboidShape(6, 0, 6, 10, 11.696, 10);
    protected static final VoxelShape TORCH_NORTH_AABB = Block.createCuboidShape(6, 0, 6, 10, 11.696, 10);
    protected static final VoxelShape TORCH_SOUTH_AABB = Block.createCuboidShape(6, 0, 6, 10, 11.696, 10);
    protected static final VoxelShape TORCH_WEST_AABB = Block.createCuboidShape(6, 0, 6, 10, 11.696, 10);
    protected static final VoxelShape TORCH_EAST_AABB = Block.createCuboidShape(6, 0, 6, 10, 11.696, 10);


    public AdvancedCandle(String name, DyeColor color, ItemGroup itemGroup) {
        super(Settings.of(Material.DECORATION).noCollision().breakInstantly().luminance(BlockUtils.createLightLevelFromLitBlockState(15)));
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.UP).with(LIT, HyperLightingFabric.hyperLightingConfig.candleOnByDefault).with(COLOR, color));

        HLItems.register(name, new BlockItemWithColoredLight(this, new FabricItemSettings().group(itemGroup)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT, FACING, COLOR);
        super.appendProperties(builder);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(LIT)) {
            DyeColor color = state.get(COLOR);

            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY() + 0.7D;
            double d2 = (double) pos.getZ() + 0.5D;
            double d3 = 0.22D;
            double d4 = 0.27D;

            world.addParticle(ParticleTypes.SMOKE, d0, d1 + 0.1, d2, 0.0D, 0.0D, 0.0D);
            world.addParticle(ParticleRegistryHandler.CUSTOM_FLAME, d0, d1 + 0.1F, d2, color.getColorComponents()[0], color.getColorComponents()[1], color.getColorComponents()[2]);
        }
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        if (ctx.getSide() != Direction.DOWN) {
            return this.getDefaultState().with(FACING, ctx.getSide());
        }
        return null;
    }

    public boolean isValidPosition(BlockState state, WorldView world, BlockPos pos, Direction direction) {
        return sideCoversSmallSquare(world, pos, direction);
    }

    @Override
    public void toggleLight(World world, BlockState state, BlockPos pos) {
        state = state.with(LIT, !state.get(LIT));
        world.setBlockState(pos, state, 2);
        if (!state.get(LIT)) {
            world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.3f, 1.0f);
        }
        world.updateNeighbors(pos, this);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facing, BlockState neighborState, WorldAccess worldIn, BlockPos currentPos, BlockPos neighborPos) {
        if (facing == Direction.DOWN && !this.isValidPosition(stateIn, worldIn, currentPos, facing)) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(stateIn, facing, neighborState, worldIn, currentPos, neighborPos);
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
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case EAST -> TORCH_EAST_AABB;
            case WEST -> TORCH_WEST_AABB;
            case SOUTH -> TORCH_SOUTH_AABB;
            case NORTH -> TORCH_NORTH_AABB;
            default -> STANDING_AABB;
        };
    }
}
