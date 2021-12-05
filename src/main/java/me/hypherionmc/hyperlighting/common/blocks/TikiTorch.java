package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.api.DyeAble;
import me.hypherionmc.hyperlighting.api.Lightable;
import me.hypherionmc.hyperlighting.common.config.HyperLightingConfig;
import me.hypherionmc.hyperlighting.common.handlers.ParticleRegistryHandler;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.items.BlockItemColor;
import me.hypherionmc.hyperlighting.util.ModUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class TikiTorch extends Block implements DyeAble, Lightable {

    public static BooleanProperty LIT = BlockStateProperties.LIT;
    protected static final VoxelShape STANDING_AABB = Block.box(6,0,6,10,16,10);
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

    public TikiTorch(String name, DyeColor color, CreativeModeTab group) {
        super(Properties.of(Material.DECORATION).noCollission().instabreak());

        this.registerDefaultState(this.getStateDefinition().any().setValue(LIT, HyperLightingConfig.tikiOnByDefault.get()).setValue(COLOR, color));

        if (ModUtils.isRGBLibPresent()) {
            //ColoredLightManager.registerProvider(this, this::produceColoredLight);
        }

        HLItems.ITEMS.register(name, () -> new BlockItemColor(this, new Item.Properties().tab(group)));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return STANDING_AABB;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, COLOR);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
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
            worldIn.playSound((Player) null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.3f, 1.0f);
        }
        worldIn.updateNeighborsAt(pos, this);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return state.getValue(LIT) ? 15 : 0;
    }

    // RGBLib Support
    /*private RGBLight produceColoredLight(BlockPos pos, BlockState state) {
        if (state.getValue(LIT) && !HyperLightingConfig.tikiColor.get()) {
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
        return InteractionResult.PASS;
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
        if (stateIn.getValue(LIT)) {
            DyeColor color = stateIn.getValue(COLOR);

            double d0 = (double)pos.getX() + 0.5D;
            double d1 = (double)pos.getY() + 0.6D;
            double d2 = (double)pos.getZ() + 0.5D;

            worldIn.addParticle(ParticleTypes.SMOKE, d0, d1 + 0.4f, d2, 0.0D, 0.0D, 0.0D);
            worldIn.addParticle(ParticleRegistryHandler.CUSTOM_FLAME.get(),  d0, d1 + 0.4f, d2, color.getTextureDiffuseColors()[0], color.getTextureDiffuseColors()[1], color.getTextureDiffuseColors()[2]);

        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TextComponent(ChatFormatting.YELLOW + "Dyable"));
        tooltip.add(new TextComponent(ChatFormatting.GREEN + "Color: " + defaultDyeColor().name()));
        tooltip.add(new TextComponent(ChatFormatting.BLUE + "Colored Lighting Supported"));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}
