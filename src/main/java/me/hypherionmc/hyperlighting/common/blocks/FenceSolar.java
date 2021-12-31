package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.api.DyeAble;
import me.hypherionmc.hyperlighting.api.RemoteSwitchable;
import me.hypherionmc.hyperlighting.common.config.HyperLightingConfig;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.items.BlockItemColor;
import me.hypherionmc.hyperlighting.common.tile.TileSolarLight;
import me.hypherionmc.hyperlighting.util.ModUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;

public class FenceSolar extends BaseEntityBlock implements RemoteSwitchable, DyeAble {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
    private static final VoxelShape BOUNDING_BOX = Block.box(6, 0, 6, 10, 4.992, 10);

    public FenceSolar(String name, DyeColor color, CreativeModeTab group) {
        super(Properties.of(Material.DECORATION).noCollission().instabreak());

        this.registerDefaultState(this.getStateDefinition().any().setValue(LIT, HyperLightingConfig.batteryOnByDefault.get()).setValue(COLOR, color));

        if (ModUtils.isRGBLibPresent()) {
            //ColoredLightManager.registerProvider(this, this::produceColoredLight);
        }

        HLItems.ITEMS.register(name, () -> new BlockItemColor(this, new Item.Properties().tab(group)));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return BOUNDING_BOX;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, COLOR);
        super.createBlockStateDefinition(builder);
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
            } else {
                BlockState oldState = state;
                if (state.getValue(LIT)) {
                    state = state.setValue(LIT, false);
                    worldIn.setBlock(pos, state, 3);
                } else {
                    if (worldIn.getBlockEntity(pos) != null && worldIn.getBlockEntity(pos) instanceof TileSolarLight && ((TileSolarLight) worldIn.getBlockEntity(pos)).getPowerLevel() > 0) {
                        state = state.setValue(LIT, true);
                        worldIn.setBlock(pos, state, 3);
                        worldIn.sendBlockUpdated(pos, oldState, state, 4);
                    } else {
                        state = state.setValue(LIT, false);
                        worldIn.setBlock(pos, state, 3);
                        worldIn.sendBlockUpdated(pos, oldState, state, 4);
                        player.displayClientMessage(new TranslatableComponent("Out of power"), true);
                    }
                }
            }

        }
        return InteractionResult.CONSUME;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return state.getValue(LIT) ? 14 : 0;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    // RGBLib Support
    /*private RGBLight produceColoredLight(BlockPos pos, BlockState state) {
        if (state.getValue(LIT) && !HyperLightingConfig.batteryColor.get()) {
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
    public BlockState remoteSwitched(BlockState state, BlockPos pos, Level world) {
        return state.setValue(LIT, !state.getValue(LIT));
    }

    @Override
    public boolean getPoweredState(BlockState state) {
        return state.getValue(LIT);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TextComponent(ChatFormatting.YELLOW + "Dyable"));
        tooltip.add(new TextComponent(ChatFormatting.GREEN + "Color: " + defaultDyeColor().name()));
        tooltip.add(new TextComponent(ChatFormatting.BLUE + "Colored Lighting Supported"));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileSolarLight(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        if (level.isClientSide()) {
            return null;
        }
        return (level1, blockPos, blockState, t) -> {
            if (t instanceof TileSolarLight tile) {
                tile.serverTick();
            }
        };
    }
}
