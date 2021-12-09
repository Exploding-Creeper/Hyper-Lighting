package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.HyperLightingFabric;
import me.hypherionmc.hyperlighting.api.DyeAble;
import me.hypherionmc.hyperlighting.api.Lightable;
import me.hypherionmc.hyperlighting.common.blockentities.CampFireBlockEntity;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.items.BlockItemColor;
import me.hypherionmc.hyperlighting.utils.BlockUtils;
import me.hypherionmc.hyperlighting.utils.CustomRenderType;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class CampFireBlock extends Block implements Waterloggable, Lightable, DyeAble, CustomRenderType, BlockEntityProvider {

    public static final EnumProperty<DyeColor> COLOR = EnumProperty.of("color", DyeColor.class);
    public static final BooleanProperty LIT = Properties.LIT;
    public static final BooleanProperty SIGNAL_FIRE = Properties.SIGNAL_FIRE;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D);
    private final boolean smokey;
    private final int fireDamage;

    public CampFireBlock(String name, DyeColor color, ItemGroup group) {
        super(Settings.of(Material.WOOD, MapColor.BROWN).strength(2.0f).sounds(BlockSoundGroup.WOOD).nonOpaque().luminance(BlockUtils.createLightLevelFromLitBlockState(15)));
        this.smokey = true;
        this.fireDamage = 1;
        this.setDefaultState(this.getDefaultState().with(LIT, HyperLightingFabric.hyperLightingConfig.campfireOnByDefault).with(SIGNAL_FIRE, Boolean.FALSE).with(WATERLOGGED, Boolean.FALSE).with(FACING, Direction.NORTH).with(COLOR, color));

        HLItems.register(name, new BlockItemColor(this, new FabricItemSettings().group(group)));
    }

    public static void extinguish(World world, BlockPos pos, BlockState state) {
        if (world.isClient() && state.get(LIT)) {
            for (int i = 0; i < 20; ++i) {
                spawnSmokeParticles(world, pos, state.get(SIGNAL_FIRE), true);
            }
            world.setBlockState(pos, state.with(LIT, false), 3);
            world.updateNeighbors(pos, state.getBlock());
        }

        BlockEntity tileentity = world.getBlockEntity(pos);
        if (tileentity instanceof CampFireBlockEntity te) {
            te.dropAllItems();
        }

    }

    public static void spawnSmokeParticles(World worldIn, BlockPos pos, boolean isSignalFire, boolean spawnExtraSmoke) {
        Random random = worldIn.getRandom();
        DefaultParticleType basicparticletype = isSignalFire ? ParticleTypes.CAMPFIRE_SIGNAL_SMOKE : ParticleTypes.CAMPFIRE_COSY_SMOKE;
        worldIn.addParticle(basicparticletype, true, (double) pos.getX() + 0.5D + random.nextDouble() / 3.0D * (double) (random.nextBoolean() ? 1 : -1), (double) pos.getY() + random.nextDouble() + random.nextDouble(), (double) pos.getZ() + 0.5D + random.nextDouble() / 3.0D * (double) (random.nextBoolean() ? 1 : -1), 0.0D, 0.07D, 0.0D);
        if (spawnExtraSmoke) {
            worldIn.addParticle(ParticleTypes.SMOKE, (double) pos.getX() + 0.25D + random.nextDouble() / 2.0D * (double) (random.nextBoolean() ? 1 : -1), (double) pos.getY() + 0.4D, (double) pos.getZ() + 0.25D + random.nextDouble() / 2.0D * (double) (random.nextBoolean() ? 1 : -1), 0.0D, 0.005D, 0.0D);
        }
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

        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        if (tileentity instanceof CampFireBlockEntity campFireBlockEntity) {
            ItemStack itemstack = player.getStackInHand(handIn);
            Optional<CampfireCookingRecipe> optional = campFireBlockEntity.findMatchingRecipe(itemstack);
            if (optional.isPresent()) {
                if (!worldIn.isClient && campFireBlockEntity.addItem(player.getAbilities().creativeMode ? itemstack.copy() : itemstack, optional.get().getCookTime())) {
                    player.increaseStat(Stats.INTERACT_WITH_CAMPFIRE, 1);
                    return ActionResult.SUCCESS;
                }

                return ActionResult.CONSUME;
            }
        }

        return ActionResult.PASS;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entityIn) {
        if (!entityIn.isFireImmune() && state.get(LIT) && entityIn instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) entityIn)) {
            entityIn.damage(DamageSource.IN_FIRE, (float) this.fireDamage);
        }
        super.onEntityCollision(state, world, pos, entityIn);
    }

    @Override
    public void onStateReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof CampFireBlockEntity te) {
                ItemScatterer.spawn(worldIn, pos, te.getInventory());
            }
            super.onStateReplaced(state, worldIn, pos, newState, moved);
        }
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        World iworld = context.getWorld();
        BlockPos blockpos = context.getBlockPos();
        boolean flag = iworld.getFluidState(blockpos).getFluid().matchesType(Fluids.WATER);
        return this.getDefaultState().with(WATERLOGGED, flag).with(SIGNAL_FIRE, this.isHayBlock(iworld.getBlockState(blockpos.down()))).with(LIT, !flag).with(FACING, context.getPlayerFacing());
    }

    boolean isHayBlock(BlockState stateIn) {
        return stateIn.isOf(Blocks.HAY_BLOCK);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            return Blocks.AIR.getDefaultState();
        }
        if (direction == Direction.DOWN) {
            return state.with(SIGNAL_FIRE, this.isHayBlock(neighborState));
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void randomDisplayTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (!canBeLit(stateIn, worldIn, pos)) {
            extinguish(worldIn, pos, stateIn);
            worldIn.updateListeners(pos, stateIn, stateIn, 3);
        }

        if (stateIn.get(LIT)) {

            if (rand.nextInt(10) == 0) {
                worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 0.5F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.6F, false);
            }

            if (this.smokey && rand.nextInt(5) == 0) {
                for (int i = 0; i < rand.nextInt(1) + 1; ++i) {
                    worldIn.addParticle(ParticleTypes.LAVA, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, rand.nextFloat() / 2.0F, 5.0E-5D, rand.nextFloat() / 2.0F);
                }
            }

        }
    }

    public boolean canBeLit(BlockState state, World world, BlockPos pos) {
        return !state.get(WATERLOGGED) && !(world.isRaining() && world.isSkyVisibleAllowingSea(pos));
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
    public void toggleLight(World worldIn, BlockState state, BlockPos pos) {
        state = state.with(LIT, !state.get(LIT));
        worldIn.setBlockState(pos, state, 2);
        if (!state.get(LIT)) {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.3f, 1.0f);
        }
        worldIn.updateNeighbors(pos, this);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(new TranslatableText("tooltip.camp_fire"));
        tooltip.add(new TranslatableText("tooltip.camp_fire_line1"));
        tooltip.add(new LiteralText(Formatting.YELLOW + "Dyable"));
        tooltip.add(new LiteralText(Formatting.BLUE + "Colored Lighting Supported"));
        //super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return (level1, blockPos, blockState, t) -> {
            if (t instanceof CampFireBlockEntity tile) {
                if (level1.isClient()) {
                    tile.clientTick();
                } else {
                    tile.serverTick();
                }
            }
        };
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CampFireBlockEntity(pos, state);
    }

    @Override
    public RenderLayer getCustomRenderType() {
        return RenderLayer.getCutoutMipped();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT, SIGNAL_FIRE, WATERLOGGED, FACING, COLOR);
        super.appendProperties(builder);
    }
}
