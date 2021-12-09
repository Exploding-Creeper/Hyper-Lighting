package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.HyperLightingFabric;
import me.hypherionmc.hyperlighting.common.handlers.ParticleRegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemGroup;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Random;

public class UnderwaterLantern extends AdvancedLantern implements Waterloggable {

    public static BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public UnderwaterLantern(String name, ItemGroup group) {
        super(name, DyeColor.BLUE, group);
        this.setDefaultState(this.getDefaultState().with(WATERLOGGED, false).with(LIT, HyperLightingFabric.hyperLightingConfig.underwaterOnByDefault));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
        super.appendProperties(builder);
    }

    @Override
    public void randomDisplayTick(BlockState state, World worldIn, BlockPos pos, Random rand) {
        if (worldIn.isClient && state.get(LIT)) {
            DyeColor color = DyeColor.values()[rand.nextInt(DyeColor.values().length)];
            Direction direction = state.get(HORIZONTAL_FACING);

            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY() + 0.7D;
            double d2 = (double) pos.getZ() + 0.5D;

            if (state.get(FACE) == WallMountLocation.WALL) {
                Direction direction1 = direction.getOpposite();
                worldIn.addParticle(ParticleTypes.SMOKE, d0 + 0.27D * (double) direction1.getOffsetX(), d1, d2 + 0.27D * (double) direction1.getOffsetZ(), 0.0D, 0.0D, 0.0D);
                worldIn.addParticle(ParticleRegistryHandler.CUSTOM_FLAME, d0 + 0.27D * (double) direction1.getOffsetX(), d1 - 0.3D, d2 + 0.27D * (double) direction1.getOffsetZ(), color.getColorComponents()[0], color.getColorComponents()[1], color.getColorComponents()[2]);
            } else if (state.get(FACE) == WallMountLocation.FLOOR) {
                worldIn.addParticle(ParticleTypes.SMOKE, d0, d1 - 0.3D, d2, 0.0D, 0.0D, 0.0D);
                worldIn.addParticle(ParticleRegistryHandler.CUSTOM_FLAME, d0, d1 - 0.5D, d2, color.getColorComponents()[0], color.getColorComponents()[1], color.getColorComponents()[2]);
            } else if (state.get(FACE) == WallMountLocation.CEILING) {
                worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                worldIn.addParticle(ParticleRegistryHandler.CUSTOM_FLAME, d0, d1 - 0.3D, d2, color.getColorComponents()[0], color.getColorComponents()[1], color.getColorComponents()[2]);
            }
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facing, BlockState neighborState, WorldAccess worldIn, BlockPos currentPos, BlockPos neighborPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.createAndScheduleFluidTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }
        return super.getStateForNeighborUpdate(stateIn, facing, neighborState, worldIn, currentPos, neighborPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public boolean isValidPosition(BlockState state, WorldView world, BlockPos pos, Direction direction) {
        return true;
    }
}
