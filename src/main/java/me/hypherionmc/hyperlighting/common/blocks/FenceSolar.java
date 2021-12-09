package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.HyperLightingFabric;
import me.hypherionmc.hyperlighting.api.DyeAble;
import me.hypherionmc.hyperlighting.api.RemoteSwitchable;
import me.hypherionmc.hyperlighting.common.blockentities.FenceSolarBlockEntity;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.items.BlockItemColor;
import me.hypherionmc.hyperlighting.utils.BlockUtils;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FenceSolar extends Block implements RemoteSwitchable, DyeAble, BlockEntityProvider {

    public static final BooleanProperty LIT = Properties.LIT;
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.of("color", DyeColor.class);
    private static final VoxelShape BOUNDING_BOX = Block.createCuboidShape(6, 0, 6, 10, 4.992, 10);

    public FenceSolar(String name, DyeColor color, ItemGroup group) {
        super(Settings.of(Material.DECORATION).noCollision().breakInstantly().luminance(BlockUtils.createLightLevelFromLitBlockState(14)));
        this.setDefaultState(this.getDefaultState().with(LIT, HyperLightingFabric.hyperLightingConfig.batteryOnByDefault).with(COLOR, color));

        HLItems.register(name, new BlockItemColor(this, new FabricItemSettings().group(group)));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return BOUNDING_BOX;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT, COLOR);
        super.appendProperties(builder);
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
            } else {
                BlockState oldState = state;
                if (state.get(LIT)) {
                    state = state.with(LIT, false);
                    worldIn.setBlockState(pos, state, 3);
                } else {
                    if (worldIn.getBlockEntity(pos) != null && worldIn.getBlockEntity(pos) instanceof FenceSolarBlockEntity te && te.getPowerLevel() > 0) {
                        state = state.with(LIT, true);
                        worldIn.setBlockState(pos, state, 3);
                        worldIn.updateListeners(pos, oldState, state, 4);
                    } else {
                        state = state.with(LIT, false);
                        worldIn.setBlockState(pos, state, 3);
                        worldIn.updateListeners(pos, oldState, state, 4);
                        player.sendMessage(new LiteralText("Out of power"), true);
                    }
                }
            }

        }
        return ActionResult.CONSUME;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
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
    public BlockState remoteSwitched(BlockState state, BlockPos pos, World world) {
        return state.with(LIT, !state.get(LIT));
    }

    @Override
    public boolean getPoweredState(BlockState state) {
        return state.get(LIT);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(new LiteralText(Formatting.YELLOW + "Dyable"));
        tooltip.add(new LiteralText(Formatting.GREEN + "Color: " + defaultDyeColor().name()));
        tooltip.add(new LiteralText(Formatting.BLUE + "Colored Lighting Supported"));
        super.appendTooltip(stack, world, tooltip, options);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FenceSolarBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        if (level.isClient()) {
            return null;
        }
        return (level1, blockPos, blockState, t) -> {
            if (t instanceof FenceSolarBlockEntity tile) {
                tile.serverTick();
            }
        };
    }

}
