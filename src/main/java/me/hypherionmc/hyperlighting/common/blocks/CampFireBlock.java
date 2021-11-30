package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.api.DyeAble;
import me.hypherionmc.hyperlighting.api.Lightable;
import me.hypherionmc.hyperlighting.common.config.HyperLightingConfig;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.items.BlockItemColor;
import me.hypherionmc.hyperlighting.common.tile.TileCampFire;
import me.hypherionmc.hyperlighting.util.CustomRenderType;
import me.hypherionmc.hyperlighting.util.ModUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@SuppressWarnings("deprecation")
public class CampFireBlock extends BaseEntityBlock implements SimpleWaterloggedBlock, DyeAble, CustomRenderType, Lightable {

    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D);
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty SIGNAL_FIRE = BlockStateProperties.SIGNAL_FIRE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private final boolean smokey;
    private final int fireDamage;

    public CampFireBlock(String name, DyeColor color, CreativeModeTab group) {
        super(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F).sound(SoundType.WOOD).noOcclusion());
        this.smokey = true;
        this.fireDamage = 1;
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, HyperLightingConfig.campfireOnByDefault.get()).setValue(SIGNAL_FIRE, Boolean.FALSE).setValue(WATERLOGGED, Boolean.FALSE).setValue(FACING, Direction.NORTH).setValue(COLOR, color));

        if (ModUtils.isRGBLibPresent()) {
            //ColoredLightManager.registerProvider(this, this::produceColoredLight);
        }

        HLItems.ITEMS.register(name, () -> new BlockItemColor(this, new Item.Properties().tab(group)));
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {

        if (!worldIn.isClientSide) {
            if (!player.getItemInHand(handIn).isEmpty() && player.getItemInHand(handIn).getItem() instanceof DyeItem) {
                state = state.setValue(COLOR, ((DyeItem)player.getItemInHand(handIn).getItem()).getDyeColor());
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

        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        if (tileentity instanceof TileCampFire) {
            TileCampFire TileCampFire = (TileCampFire) tileentity;
            ItemStack itemstack = player.getItemInHand(handIn);
            Optional<CampfireCookingRecipe> optional = TileCampFire.findMatchingRecipe(itemstack);
            if (optional.isPresent()) {
                if (!worldIn.isClientSide && TileCampFire.addItem(player.getAbilities().instabuild ? itemstack.copy() : itemstack, optional.get().getCookingTime())) {
                    player.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
                    return InteractionResult.SUCCESS;
                }

                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (!entityIn.fireImmune() && state.getValue(LIT) && entityIn instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) entityIn)) {
            entityIn.hurt(DamageSource.IN_FIRE, (float) this.fireDamage);
        }

        super.entityInside(state, worldIn, pos, entityIn);
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof TileCampFire) {
                Containers.dropContents(worldIn, pos, ((TileCampFire) tileentity).getInventory());
            }

            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor iworld = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        boolean flag = iworld.getFluidState(blockpos).getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(WATERLOGGED, Boolean.valueOf(flag)).setValue(SIGNAL_FIRE, Boolean.valueOf(this.isHayBlock(iworld.getBlockState(blockpos.below())))).setValue(LIT, Boolean.valueOf(!flag)).setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
            return Blocks.AIR.defaultBlockState();
        }

        return facing == Direction.DOWN ? stateIn.setValue(SIGNAL_FIRE, Boolean.valueOf(this.isHayBlock(facingState))) : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    private boolean isHayBlock(BlockState stateIn) {
        return stateIn.is(Blocks.HAY_BLOCK);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {

        if (!canBeLit(stateIn, worldIn, pos)) {
            extinguish(worldIn, pos, stateIn);
            worldIn.sendBlockUpdated(pos, stateIn, stateIn, 3);
        }

        if (stateIn.getValue(LIT)) {

            if (rand.nextInt(10) == 0) {
                worldIn.playLocalSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.6F, false);
            }

            if (this.smokey && rand.nextInt(5) == 0) {
                for (int i = 0; i < rand.nextInt(1) + 1; ++i) {
                    worldIn.addParticle(ParticleTypes.LAVA, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, (double) (rand.nextFloat() / 2.0F), 5.0E-5D, (double) (rand.nextFloat() / 2.0F));
                }
            }

        }
    }

    public static void extinguish(LevelAccessor world, BlockPos pos, BlockState state) {
        if (world.isClientSide() && state.getValue(LIT)) {
            for (int i = 0; i < 20; ++i) {
                spawnSmokeParticles((Level) world, pos, state.getValue(SIGNAL_FIRE), true);
            }
            world.setBlock(pos, state.setValue(LIT, false), 3);
            world.blockUpdated(pos, state.getBlock());
        }

        BlockEntity tileentity = world.getBlockEntity(pos);
        if (tileentity instanceof TileCampFire) {
            ((TileCampFire) tileentity).dropAllItems();
        }

    }

    @Override
    public boolean placeLiquid(LevelAccessor worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        if (!state.getValue(BlockStateProperties.WATERLOGGED) && fluidStateIn.getType() == Fluids.WATER) {
            boolean flag = state.getValue(LIT);
            if (flag) {
                if (!worldIn.isClientSide()) {
                    worldIn.playSound((Player) null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }

                extinguish(worldIn, pos, state);
            }

            worldIn.setBlock(pos, state.setValue(WATERLOGGED, Boolean.valueOf(true)).setValue(LIT, Boolean.valueOf(false)), 3);
            worldIn.scheduleTick(pos, fluidStateIn.getType(), fluidStateIn.getType().getTickDelay(worldIn));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onProjectileHit(Level worldIn, BlockState state, BlockHitResult hit, Projectile projectile) {
        if (!worldIn.isClientSide && projectile.isOnFire()) {
            Entity entity = projectile.getOwner();
            boolean flag = entity == null || entity instanceof Player || net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(worldIn, entity);
            if (flag && !state.getValue(LIT) && !state.getValue(WATERLOGGED)) {
                BlockPos blockpos = hit.getBlockPos();
                worldIn.setBlock(blockpos, state.setValue(BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
            }
        }

    }

    public static void spawnSmokeParticles(Level worldIn, BlockPos pos, boolean isSignalFire, boolean spawnExtraSmoke) {
        Random random = worldIn.getRandom();
        SimpleParticleType basicparticletype = isSignalFire ? ParticleTypes.CAMPFIRE_SIGNAL_SMOKE : ParticleTypes.CAMPFIRE_COSY_SMOKE;
        worldIn.addAlwaysVisibleParticle(basicparticletype, true, (double) pos.getX() + 0.5D + random.nextDouble() / 3.0D * (double) (random.nextBoolean() ? 1 : -1), (double) pos.getY() + random.nextDouble() + random.nextDouble(), (double) pos.getZ() + 0.5D + random.nextDouble() / 3.0D * (double) (random.nextBoolean() ? 1 : -1), 0.0D, 0.07D, 0.0D);
        if (spawnExtraSmoke) {
            worldIn.addParticle(ParticleTypes.SMOKE, (double) pos.getX() + 0.25D + random.nextDouble() / 2.0D * (double) (random.nextBoolean() ? 1 : -1), (double) pos.getY() + 0.4D, (double) pos.getZ() + 0.25D + random.nextDouble() / 2.0D * (double) (random.nextBoolean() ? 1 : -1), 0.0D, 0.005D, 0.0D);
        }

    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, SIGNAL_FIRE, WATERLOGGED, FACING, COLOR);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return false;
    }

    public boolean canBeLit(BlockState state, Level world, BlockPos pos) {
        return !state.getValue(WATERLOGGED) && !(world.isRaining() && world.canSeeSkyFromBelowWater(pos));
    }

    @Override
    public BlockColor dyeHandler() {
        return (state, world, pos, tintIndex) -> {
            if (state.getValue(LIT)) {
                return state.getValue(COLOR).getFireworkColor();
            } else {
                return DyeColor.BLACK.getFireworkColor();
            }
        };
    }

    @Override
    public DyeColor defaultDyeColor() {
        return this.defaultBlockState().getValue(COLOR);
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.cutoutMipped();
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return state.getValue(LIT) ? 15 : 0;
    }

    @Override
    public void toggleLight(Level worldIn, BlockState state, BlockPos pos) {

        state = state.setValue(LIT, !state.getValue(LIT));
        worldIn.setBlock(pos, state, 2);
        if (!state.getValue(LIT)) {
            extinguish(worldIn, pos, state);
        }
        worldIn.updateNeighborsAt(pos, this);

    }

    // RGBLib Support
    /*private RGBLight produceColoredLight(BlockPos pos, BlockState state) {
        if (state.getValue(LIT) && !HyperLightingConfig.campfireColor.get()) {
            //return RGBLight.builder().pos(pos).color(state.getValue(COLOR).getFireworkColor(), false).radius(15).build();
        }
        return null;
    }*/

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("tooltip.camp_fire"));
        tooltip.add(new TranslatableComponent("tooltip.camp_fire_line1"));
        tooltip.add(new TextComponent(ChatFormatting.YELLOW + "Dyable"));
        tooltip.add(new TextComponent(ChatFormatting.BLUE + "Colored Lighting Supported"));
        //super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileCampFire(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return (level1, blockPos, blockState, t) -> {
            if (t instanceof TileCampFire tile) {
                if (level1.isClientSide()) {
                    tile.clientTick();
                } else {
                    tile.serverTick();
                }
            }
        };
    }
}
