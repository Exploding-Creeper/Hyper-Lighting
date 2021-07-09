package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.api.DyeAble;
import me.hypherionmc.hyperlighting.api.Lightable;
import me.hypherionmc.hyperlighting.common.config.HyperLightingConfig;
import me.hypherionmc.hyperlighting.common.handlers.ParticleRegistryHandler;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.items.BlockItemWithColoredLight;
import me.hypherionmc.hyperlighting.util.CustomRenderType;
import me.hypherionmc.hyperlighting.util.ModUtils;
import me.hypherionmc.rgblib.api.ColoredLightManager;
import me.hypherionmc.rgblib.api.RGBLight;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.AttachFace;
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

public class AdvancedLantern extends HorizontalFaceBlock implements DyeAble, Lightable, CustomRenderType {

    //Bounding Boxes
    private static final VoxelShape BB_TOP = Block.makeCuboidShape(4.992,0,4,11.008,11.008,11.008);
    private static final VoxelShape BB_NORTH = Block.makeCuboidShape(4.992,3.008,8,11.008,16,16.992);
    private static final VoxelShape BB_SOUTH = Block.makeCuboidShape(4.992,3.008,-0.992,11.008,16,8);
    private static final VoxelShape BB_EAST = Block.makeCuboidShape(-0.992,3.008,4.992,8,16,11.008);
    private static final VoxelShape BB_WEST = Block.makeCuboidShape(8,3.008,4.992,16.992,16,11.008);
    private static final VoxelShape BB_CEILING = Block.makeCuboidShape(4.912,3.008,5.088,11.904,16,11.088);

    //Properties
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final DirectionProperty HORIZONTAL_FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

    public AdvancedLantern(String name, DyeColor color, ItemGroup group) {
        super(Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().zeroHardnessAndResistance());

        this.setDefaultState(this.getStateContainer().getBaseState().with(HORIZONTAL_FACING, Direction.NORTH).with(LIT, HyperLightingConfig.lanternOnByDefault.get()).with(COLOR, color));

        if (ModUtils.isRGBLibPresent()) {
            ColoredLightManager.registerProvider(this, this::produceColoredLight);
        }

        HLItems.ITEMS.register(name, () -> new BlockItemWithColoredLight(this, new Item.Properties().group(group)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(LIT, HORIZONTAL_FACING, FACE, COLOR);
        super.fillStateContainer(builder);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        switch(state.get(FACE)) {
            case FLOOR:
            default:
                return BB_TOP;
            case WALL:
                switch(state.get(HORIZONTAL_FACING)) {
                    case EAST:
                        return BB_EAST;
                    case WEST:
                        return BB_WEST;
                    case SOUTH:
                        return BB_SOUTH;
                    case NORTH:
                    default:
                        return BB_NORTH;
                }
            case CEILING:
                return BB_CEILING;
        }
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        for (Direction direction : Direction.values()) {
            if (!isValidPosition(stateIn, worldIn, currentPos, direction)) {
                return Blocks.AIR.getDefaultState();
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
        if (state.get(LIT) && !HyperLightingConfig.lanternColor.get()) {
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
    public void animateTick(BlockState state, World worldIn, BlockPos pos, Random rand) {
        if (worldIn.isRemote && state.get(LIT)) {
            DyeColor color = state.get(COLOR);
            Direction direction = state.get(HORIZONTAL_FACING);

            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY() + 0.7D;
            double d2 = (double) pos.getZ() + 0.5D;

            if (state.get(FACE) == AttachFace.WALL) {
                Direction direction1 = direction.getOpposite();
                worldIn.addParticle(ParticleTypes.SMOKE, d0 + 0.27D * (double)direction1.getXOffset(), d1 , d2 + 0.27D * (double)direction1.getZOffset(), 0.0D, 0.0D, 0.0D);
                worldIn.addParticle(ParticleRegistryHandler.CUSTOM_FLAME.get(), d0 + 0.27D * (double)direction1.getXOffset(), d1 - 0.3D, d2 + 0.27D * (double)direction1.getZOffset(), color.getColorComponentValues()[0], color.getColorComponentValues()[1], color.getColorComponentValues()[2]);
            } else if (state.get(FACE) == AttachFace.FLOOR) {
                worldIn.addParticle(ParticleTypes.SMOKE, d0, d1 - 0.3D, d2, 0.0D, 0.0D, 0.0D);
                worldIn.addParticle(ParticleRegistryHandler.CUSTOM_FLAME.get(), d0, d1 - 0.5D, d2, color.getColorComponentValues()[0], color.getColorComponentValues()[1], color.getColorComponentValues()[2]);
            } else if (state.get(FACE) == AttachFace.CEILING) {
                worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                worldIn.addParticle(ParticleRegistryHandler.CUSTOM_FLAME.get(), d0, d1 - 0.3D, d2, color.getColorComponentValues()[0], color.getColorComponentValues()[1], color.getColorComponentValues()[2]);
            }
        }
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.getCutoutMipped();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new StringTextComponent(TextFormatting.YELLOW + "Dyable"));
        tooltip.add(new StringTextComponent(TextFormatting.GREEN + "Color: " + defaultDyeColor().name()));
        tooltip.add(new StringTextComponent(TextFormatting.BLUE + "Colored Lighting Supported"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
