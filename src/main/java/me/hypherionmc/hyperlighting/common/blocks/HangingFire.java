package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.api.DyeAble;
import me.hypherionmc.hyperlighting.api.Lightable;
import me.hypherionmc.hyperlighting.common.config.HyperLightingConfig;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.items.BlockItemColor;
import me.hypherionmc.hyperlighting.common.items.BlockItemWithColoredLight;
import me.hypherionmc.hyperlighting.util.CustomRenderType;
import me.hypherionmc.hyperlighting.util.ModUtils;
import me.hypherionmc.rgblib.api.ColoredLightManager;
import me.hypherionmc.rgblib.api.RGBLight;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class HangingFire extends Block implements CustomRenderType, DyeAble, Lightable {

    public static EnumProperty<AttachFace> ATTACH_FACE = BlockStateProperties.FACE;
    public static BooleanProperty LIT = BlockStateProperties.LIT;
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

    public HangingFire(String name, DyeColor color, ItemGroup group) {
        super(Properties.create(Material.IRON).notSolid().doesNotBlockMovement().sound(SoundType.METAL));
        this.setDefaultState(this.getDefaultState().with(ATTACH_FACE, AttachFace.CEILING).with(LIT, true).with(COLOR, color));

        if (ModUtils.isRGBLibPresent()) {
            ColoredLightManager.registerProvider(this, this::produceColoredLight);
        }

        HLItems.ITEMS.register(name, () -> new BlockItemColor(this, new Item.Properties().group(group)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ATTACH_FACE, LIT, COLOR);
        super.fillStateContainer(builder);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        if (context.getFace() == Direction.DOWN) {
            return this.getDefaultState().with(ATTACH_FACE, AttachFace.CEILING);
        }
        return Blocks.AIR.getDefaultState();
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facing != Direction.DOWN) {
            return Blocks.AIR.getDefaultState();
        }
        return updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.getCutoutMipped();
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
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new StringTextComponent(TextFormatting.YELLOW + "Dyable"));
        tooltip.add(new StringTextComponent(TextFormatting.GREEN + "Color: " + defaultDyeColor().name()));
        tooltip.add(new StringTextComponent(TextFormatting.BLUE + "Colored Lighting Supported"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
