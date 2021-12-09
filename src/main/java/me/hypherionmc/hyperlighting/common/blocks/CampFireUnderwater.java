package me.hypherionmc.hyperlighting.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CampFireUnderwater extends CampFireBlock {

    public CampFireUnderwater(String name, DyeColor color, ItemGroup group) {
        super(name, color, group);
    }

    @Override
    public boolean canBeLit(BlockState state, World world, BlockPos pos) {
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(new TranslatableText("tooltip.camp_fire_soul"));
        tooltip.add(new TranslatableText("tooltip.camp_fire_soul_line1"));
        tooltip.add(new LiteralText(Formatting.YELLOW + "Dyable"));
        tooltip.add(new LiteralText(Formatting.BLUE + "Colored Lighting Supported"));
        //super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        if (direction == Direction.DOWN) {
            return state.with(SIGNAL_FIRE, this.isHayBlock(neighborState));
        }
        return state;
    }
}
