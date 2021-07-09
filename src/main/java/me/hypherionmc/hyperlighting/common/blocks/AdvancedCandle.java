package me.hypherionmc.hyperlighting.common.blocks;

import com.google.common.base.Predicate;
import me.hypherionmc.hyperlighting.api.DyeAble;
import me.hypherionmc.hyperlighting.api.Lightable;
import me.hypherionmc.hyperlighting.common.config.HyperLightingConfig;
import me.hypherionmc.hyperlighting.common.handlers.ParticleRegistryHandler;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.items.BlockItemWithColoredLight;
import me.hypherionmc.hyperlighting.util.ModUtils;
import me.hypherionmc.rgblib.api.ColoredLightManager;
import me.hypherionmc.rgblib.api.RGBLight;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class AdvancedCandle extends Block implements DyeAble, Lightable {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final DirectionProperty FACING = DirectionProperty.create("facing", new Predicate<Direction>()
    {
        public boolean apply(@Nullable Direction p_apply_1_)
        {
            return p_apply_1_ != Direction.DOWN;
        }
    });
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

    protected static final VoxelShape STANDING_AABB = Block.makeCuboidShape(6,0,6,10,11.696,10);
    protected static final VoxelShape TORCH_NORTH_AABB = Block.makeCuboidShape(6,0,6,10,11.696,10);
    protected static final VoxelShape TORCH_SOUTH_AABB = Block.makeCuboidShape(6,0,6,10,11.696,10);
    protected static final VoxelShape TORCH_WEST_AABB = Block.makeCuboidShape(6,0,6,10,11.696,10);
    protected static final VoxelShape TORCH_EAST_AABB = Block.makeCuboidShape(6,0,6,10,11.696,10);

    public AdvancedCandle(String name, DyeColor color, ItemGroup group) {
        super(Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().zeroHardnessAndResistance());

        this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.UP).with(LIT, HyperLightingConfig.candleOnByDefault.get()).with(COLOR, color));

        if (ModUtils.isRGBLibPresent()) {
            ColoredLightManager.registerProvider(this, this::produceColoredLight);
        }

        HLItems.ITEMS.register(name, () -> new BlockItemWithColoredLight(this, new Item.Properties().group(group)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(LIT, FACING, COLOR);
        super.fillStateContainer(builder);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch (state.get(FACING))
        {
            case EAST:
                return TORCH_EAST_AABB;
            case WEST:
                return TORCH_WEST_AABB;
            case SOUTH:
                return TORCH_SOUTH_AABB;
            case NORTH:
                return TORCH_NORTH_AABB;
            default:
                return STANDING_AABB;
        }
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {

        if (stateIn.get(LIT)) {
            DyeColor color = stateIn.get(COLOR);

            double d0 = (double)pos.getX() + 0.5D;
            double d1 = (double)pos.getY() + 0.7D;
            double d2 = (double)pos.getZ() + 0.5D;
            double d3 = 0.22D;
            double d4 = 0.27D;

            worldIn.addParticle(ParticleTypes.SMOKE, d0, d1 + 0.1, d2, 0.0D, 0.0D, 0.0D);
            worldIn.addParticle(ParticleRegistryHandler.CUSTOM_FLAME.get(), d0, d1 + 0.1F, d2, color.getColorComponentValues()[0], color.getColorComponentValues()[1], color.getColorComponentValues()[2]);

        }

    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        if (context.getFace() != Direction.DOWN) {
            return this.getDefaultState().with(FACING, context.getFace());
        }
        return null;
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facing == Direction.DOWN) {
            return Blocks.AIR.getDefaultState();
        } else {
            for (Direction direction : Direction.values()) {
                if (!isValidPosition(stateIn, worldIn, currentPos, direction)) {
                    return Blocks.AIR.getDefaultState();
                }
            }
        }
        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos, Direction direction) {
        return hasEnoughSolidSide(worldIn, pos.down(), direction);
    }

    @Override
    public void toggleLight(World worldIn, BlockState state, BlockPos pos) {
        state = state.with(LIT, !state.get(LIT));
        worldIn.setBlockState(pos, state, 2);
        if (!state.get(LIT)) {
            worldIn.playSound((PlayerEntity) null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.3f, 1.0f);
        }
        worldIn.notifyNeighborsOfStateChange(pos, this);
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return state.get(LIT) ? 15 : 0;
    }

    // RGBLib Support
    private RGBLight produceColoredLight(BlockPos pos, BlockState state) {
        if (state.get(LIT) && !HyperLightingConfig.candleColor.get()) {
            return RGBLight.builder().pos(pos).color(state.get(COLOR).getColorValue(), false).radius(15).build();
        }
        return null;
    }

    @Override
    public IBlockColor dyeHandler() {
        return (state, world, pos, tintIndex) -> {
            if (state.get(LIT)) {
                return state.get(COLOR).getColorValue();
            } else {
                return DyeColor.BLACK.getColorValue();
            }
        };
    }

    @Override
    public DyeColor defaultDyeColor() {
        return this.getDefaultState().get(COLOR);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {

            if (!player.getHeldItem(handIn).isEmpty() && player.getHeldItem(handIn).getItem() instanceof DyeItem) {
                state = state.with(COLOR, ((DyeItem)player.getHeldItem(handIn).getItem()).getDyeColor());
                worldIn.setBlockState(pos, state, 3);
                worldIn.notifyBlockUpdate(pos, state, state, 3);

                if (!player.isCreative()) {
                    ItemStack stack = player.getHeldItem(handIn);
                    stack.shrink(1);
                    player.setHeldItem(handIn, stack);
                }

                return ActionResultType.CONSUME;
            }

        }
        return ActionResultType.PASS;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new StringTextComponent(TextFormatting.YELLOW + "Dyable"));
        tooltip.add(new StringTextComponent(TextFormatting.GREEN + "Color: " + defaultDyeColor().name()));
        tooltip.add(new StringTextComponent(TextFormatting.BLUE + "Colored Lighting Supported"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
