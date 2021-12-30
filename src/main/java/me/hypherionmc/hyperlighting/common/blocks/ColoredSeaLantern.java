package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.api.DyeAble;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.items.BlockItemColor;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.Waterloggable;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
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
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ColoredSeaLantern extends Block implements Waterloggable, DyeAble {

    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.of("color", DyeColor.class);

    public ColoredSeaLantern(String name, DyeColor color, ItemGroup group) {
        super(Settings.of(Material.WOOD, color.getMapColor()).strength(2.0f).sounds(BlockSoundGroup.GLASS).nonOpaque().luminance((state) -> 15));
        this.setDefaultState(this.getDefaultState().with(COLOR, color).with(WATERLOGGED, false));
        HLItems.register(name, new BlockItemColor(this, new FabricItemSettings().group(group)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(COLOR, WATERLOGGED);
        super.appendProperties(builder);
    }

    @Override
    public BlockColorProvider dyeHandler() {
        return (state, world, pos, tintIndex) -> {
            return state.get(COLOR).getMapColor().color;
        };
    }

    @Override
    public DyeColor defaultDyeColor() {
        return this.getDefaultState().get(COLOR);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(new LiteralText(Formatting.YELLOW + "Dyable"));
        tooltip.add(new LiteralText(Formatting.GREEN + "Color: " + defaultDyeColor().name()));
        tooltip.add(new LiteralText(Formatting.BLUE + "Colored Lighting Supported"));
        super.appendTooltip(stack, world, tooltip, options);
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
        return ActionResult.PASS;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facing, BlockState neighborState, WorldAccess worldIn, BlockPos currentPos, BlockPos neighborPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.createAndScheduleFluidTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }
        return super.getStateForNeighborUpdate(stateIn, facing, neighborState, worldIn, currentPos, neighborPos);
    }

}
