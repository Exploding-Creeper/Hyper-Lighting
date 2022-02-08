package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.api.DyeAble;
import me.hypherionmc.hyperlighting.api.Lightable;
import me.hypherionmc.hyperlighting.common.config.HyperLightingConfig;
import me.hypherionmc.hyperlighting.common.handlers.ParticleRegistryHandler;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.items.BlockItemWithColoredLight;
import me.hypherionmc.hyperlighting.util.CustomRenderType;
import me.hypherionmc.hyperlighting.util.ModUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class AdvancedLantern extends FaceAttachedHorizontalDirectionalBlock implements DyeAble, Lightable, CustomRenderType {

    //Properties
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final DirectionProperty HORIZONTAL_FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

    //Bounding Boxes
    private static final VoxelShape BB_TOP = Block.box(4.992, 0, 4, 11.008, 11.008, 11.008);
    private static final VoxelShape BB_NORTH = Block.box(4.992, 3.008, 8, 11.008, 16, 16.992);
    private static final VoxelShape BB_SOUTH = Block.box(4.992, 3.008, -0.992, 11.008, 16, 8);
    private static final VoxelShape BB_EAST = Block.box(-0.992, 3.008, 4.992, 8, 16, 11.008);
    private static final VoxelShape BB_WEST = Block.box(8, 3.008, 4.992, 16.992, 16, 11.008);
    private static final VoxelShape BB_CEILING = Block.box(4.912, 3.008, 5.088, 11.904, 16, 11.088);

    public AdvancedLantern(String name, DyeColor color, CreativeModeTab group) {
        super(Properties.of(Material.DECORATION).noCollission().instabreak());

        this.registerDefaultState(this.getStateDefinition().any().setValue(HORIZONTAL_FACING, Direction.NORTH).setValue(LIT, HyperLightingConfig.lanternOnByDefault.get()).setValue(COLOR, color));

        if (ModUtils.isRGBLibPresent()) {
            //ColoredLightManager.registerProvider(this, this::produceColoredLight);
        }

        HLItems.ITEMS.register(name, () -> new BlockItemWithColoredLight(this, new Item.Properties().tab(group)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, HORIZONTAL_FACING, FACE, COLOR);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        switch (state.getValue(FACE)) {
            case FLOOR:
            default:
                return BB_TOP;
            case WALL:
                switch (state.getValue(HORIZONTAL_FACING)) {
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
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        for (Direction direction : Direction.values()) {
            if (!isValidPosition(stateIn, worldIn, currentPos, direction)) {
                return Blocks.AIR.defaultBlockState();
            }
        }
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public boolean isValidPosition(BlockState state, LevelReader worldIn, BlockPos pos, Direction direction) {
        return canSupportCenter(worldIn, pos.below(), direction);
    }

    @Override
    public void toggleLight(Level worldIn, BlockState state, BlockPos pos) {
        state = state.setValue(LIT, !state.getValue(LIT));
        worldIn.setBlock(pos, state, 2);
        if (!state.getValue(LIT)) {
            worldIn.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.3f, 1.0f);
        }
        worldIn.updateNeighborsAt(pos, this);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return state.getValue(LIT) ? 15 : 0;
    }

    // RGBLib Support
    /*private RGBLight produceColoredLight(BlockPos pos, BlockState state) {
        if (state.getValue(LIT) && !HyperLightingConfig.lanternColor.get()) {
            //return RGBLight.builder().pos(pos).color(state.getValue(COLOR).getMaterialColor().col, false).radius(15).build();
        }
        return null;
    }*/

    @Override
    public BlockColor dyeHandler() {
        return (state, world, pos, tintIndex) -> {
            if (state.getValue(LIT)) {
                return state.getValue(COLOR).getMaterialColor().col;
            } else {
                return DyeColor.BLACK.getMaterialColor().col;
            }
        };
    }

    @Override
    public DyeColor defaultDyeColor() {
        return this.defaultBlockState().getValue(COLOR);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!worldIn.isClientSide) {

            if (!player.getItemInHand(handIn).isEmpty() && player.getItemInHand(handIn).getItem() instanceof DyeItem) {
                state = state.setValue(COLOR, ((DyeItem) player.getItemInHand(handIn).getItem()).getDyeColor());
                worldIn.setBlock(pos, state, 3);
                worldIn.sendBlockUpdated(pos, state, state, 3);

                if (!player.isCreative()) {
                    ItemStack stack = player.getItemInHand(handIn);
                    stack.shrink(1);
                    player.setItemInHand(handIn, stack);
                }

                return InteractionResult.CONSUME;
            }

        }
        return InteractionResult.PASS;
    }

    @Override
    public void animateTick(BlockState state, Level worldIn, BlockPos pos, Random rand) {
        if (worldIn.isClientSide && state.getValue(LIT)) {
            DyeColor color = state.getValue(COLOR);
            Direction direction = state.getValue(HORIZONTAL_FACING);

            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY() + 0.7D;
            double d2 = (double) pos.getZ() + 0.5D;

            if (state.getValue(FACE) == AttachFace.WALL) {
                Direction direction1 = direction.getOpposite();
                worldIn.addParticle(ParticleTypes.SMOKE, d0 + 0.27D * (double) direction1.getStepX(), d1, d2 + 0.27D * (double) direction1.getStepZ(), 0.0D, 0.0D, 0.0D);
                worldIn.addParticle(ParticleRegistryHandler.CUSTOM_FLAME.get(), d0 + 0.27D * (double) direction1.getStepX(), d1 - 0.3D, d2 + 0.27D * (double) direction1.getStepZ(), color.getTextureDiffuseColors()[0], color.getTextureDiffuseColors()[1], color.getTextureDiffuseColors()[2]);
            } else if (state.getValue(FACE) == AttachFace.FLOOR) {
                worldIn.addParticle(ParticleTypes.SMOKE, d0, d1 - 0.3D, d2, 0.0D, 0.0D, 0.0D);
                worldIn.addParticle(ParticleRegistryHandler.CUSTOM_FLAME.get(), d0, d1 - 0.5D, d2, color.getTextureDiffuseColors()[0], color.getTextureDiffuseColors()[1], color.getTextureDiffuseColors()[2]);
            } else if (state.getValue(FACE) == AttachFace.CEILING) {
                worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                worldIn.addParticle(ParticleRegistryHandler.CUSTOM_FLAME.get(), d0, d1 - 0.3D, d2, color.getTextureDiffuseColors()[0], color.getTextureDiffuseColors()[1], color.getTextureDiffuseColors()[2]);
            }
        }
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.cutoutMipped();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TextComponent(ChatFormatting.YELLOW + "Dyable"));
        tooltip.add(new TextComponent(ChatFormatting.GREEN + "Color: " + defaultDyeColor().name()));
        tooltip.add(new TextComponent(ChatFormatting.BLUE + "Colored Lighting Supported"));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}
