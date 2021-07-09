package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.api.DyeAble;
import me.hypherionmc.hyperlighting.api.RemoteSwitchable;
import me.hypherionmc.hyperlighting.common.config.HyperLightingConfig;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.items.BlockItemColor;
import me.hypherionmc.hyperlighting.common.tile.TileSolarLight;
import me.hypherionmc.hyperlighting.util.ModUtils;
import me.hypherionmc.rgblib.api.ColoredLightManager;
import me.hypherionmc.rgblib.api.RGBLight;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class FenceSolar extends ContainerBlock implements RemoteSwitchable, DyeAble {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    private static final VoxelShape BOUNDING_BOX = Block.makeCuboidShape(6,0,6,10,4.992,10);
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

    public FenceSolar(String name, DyeColor color, ItemGroup group) {
        super(Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().zeroHardnessAndResistance());

        this.setDefaultState(this.getStateContainer().getBaseState().with(LIT, HyperLightingConfig.batteryOnByDefault.get()).with(COLOR, color));

        if (ModUtils.isRGBLibPresent()) {
            ColoredLightManager.registerProvider(this, this::produceColoredLight);
        }

        HLItems.ITEMS.register(name, () -> new BlockItemColor(this, new Item.Properties().group(group)));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return BOUNDING_BOX;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(LIT, COLOR);
        super.fillStateContainer(builder);
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
            } else {
                BlockState oldState = state;
                if (state.get(LIT)) {
                    state = state.with(LIT, false);
                    worldIn.setBlockState(pos, state, 3);
                } else {
                    if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileSolarLight && ((TileSolarLight)worldIn.getTileEntity(pos)).getPowerLevel() > 0) {
                        state = state.with(LIT, true);
                        worldIn.setBlockState(pos, state, 3);
                        worldIn.notifyBlockUpdate(pos, oldState, state, 4);
                    } else {
                        state = state.with(LIT, false);
                        worldIn.setBlockState(pos, state, 3);
                        worldIn.notifyBlockUpdate(pos, oldState, state, 4);
                        player.sendStatusMessage(new TranslationTextComponent("Out of power"), true);
                    }
                }
            }

        }
        return ActionResultType.CONSUME;
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
       return state.get(LIT) ? 14 : 0;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TileSolarLight();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    // RGBLib Support
    private RGBLight produceColoredLight(BlockPos pos, BlockState state) {
        if (state.get(LIT) && !HyperLightingConfig.batteryColor.get()) {
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
    public BlockState remoteSwitched(BlockState state, BlockPos pos, World world) {
        return state.with(LIT, !state.get(LIT));
    }

    @Override
    public boolean getPoweredState(BlockState state) {
        return state.get(LIT);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new StringTextComponent(TextFormatting.YELLOW + "Dyable"));
        tooltip.add(new StringTextComponent(TextFormatting.GREEN + "Color: " + defaultDyeColor().name()));
        tooltip.add(new StringTextComponent(TextFormatting.BLUE + "Colored Lighting Supported"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
