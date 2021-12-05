package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.api.DyeAble;
import me.hypherionmc.hyperlighting.api.Lightable;
import me.hypherionmc.hyperlighting.common.config.HyperLightingConfig;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.items.BlockItemColor;
import me.hypherionmc.hyperlighting.util.CustomRenderType;
import me.hypherionmc.hyperlighting.util.ModUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import java.util.List;

public class HangingFire extends Block implements CustomRenderType, DyeAble, Lightable {

    public static EnumProperty<AttachFace> ATTACH_FACE = BlockStateProperties.ATTACH_FACE;
    public static BooleanProperty LIT = BlockStateProperties.LIT;
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

    public HangingFire(String name, DyeColor color, CreativeModeTab group) {
        super(Properties.of(Material.METAL).noOcclusion().noCollission().sound(SoundType.METAL));
        this.registerDefaultState(this.defaultBlockState().setValue(ATTACH_FACE, AttachFace.CEILING).setValue(LIT, true).setValue(COLOR, color));

        if (ModUtils.isRGBLibPresent()) {
            //ColoredLightManager.registerProvider(this, this::produceColoredLight);
        }

        HLItems.ITEMS.register(name, () -> new BlockItemColor(this, new Item.Properties().tab(group)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ATTACH_FACE, LIT, COLOR);
        super.createBlockStateDefinition(builder);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (context.getClickedFace() == Direction.DOWN) {
            return this.defaultBlockState().setValue(ATTACH_FACE, AttachFace.CEILING);
        }
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facing != Direction.DOWN) {
            return Blocks.AIR.defaultBlockState();
        }
        return updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.cutoutMipped();
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
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TextComponent(ChatFormatting.YELLOW + "Dyable"));
        tooltip.add(new TextComponent(ChatFormatting.GREEN + "Color: " + defaultDyeColor().name()));
        tooltip.add(new TextComponent(ChatFormatting.BLUE + "Colored Lighting Supported"));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}
