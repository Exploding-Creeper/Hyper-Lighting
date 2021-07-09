package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.common.config.HyperLightingConfig;
import me.hypherionmc.hyperlighting.common.handlers.ParticleRegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Random;

public class UnderwaterLantern extends AdvancedLantern implements IWaterLoggable {

    public static BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public UnderwaterLantern(String name, ItemGroup group) {
        super(name, DyeColor.BLUE, group);
        this.setDefaultState(this.getDefaultState().with(WATERLOGGED, false).with(LIT, HyperLightingConfig.underwaterOnByDefault.get()));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
        super.fillStateContainer(builder);
    }

    @Override
    public void animateTick(BlockState state, World worldIn, BlockPos pos, Random rand) {
        if (worldIn.isRemote && state.get(LIT)) {
            DyeColor color = DyeColor.values()[rand.nextInt(DyeColor.values().length)];
            Direction direction = state.get(HORIZONTAL_FACING);

            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY() + 0.7D;
            double d2 = (double) pos.getZ() + 0.5D;

            if (state.get(FACE) == AttachFace.WALL) {
                Direction direction1 = direction.getOpposite();
                worldIn.addParticle(ParticleTypes.SMOKE, d0 + 0.27D * (double)direction1.getXOffset(), d1 , d2 + 0.27D * (double)direction1.getZOffset(), 0.0D, 0.0D, 0.0D);
                worldIn.addParticle(ParticleRegistryHandler.CUSTOM_FLAME.get(), d0 + 0.27D * (double)direction1.getXOffset(), d1 - 0.3D, d2 + 0.27D * (double)direction1.getZOffset(), color.getColorComponentValues()[0], color.getColorComponentValues()[1], color.getColorComponentValues()[2]);
            } else if (state.get(FACE) == AttachFace.FLOOR) {
                worldIn.addParticle(ParticleTypes.SMOKE, d0, d1 - 0.3D, d2, 0.0D, 0.0D, 0.0D);
                worldIn.addParticle(ParticleRegistryHandler.CUSTOM_FLAME.get(), d0, d1 - 0.5D, d2, color.getColorComponentValues()[0], color.getColorComponentValues()[1], color.getColorComponentValues()[2]);
            } else if (state.get(FACE) == AttachFace.CEILING) {
                worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                worldIn.addParticle(ParticleRegistryHandler.CUSTOM_FLAME.get(), d0, d1 - 0.3D, d2, color.getColorComponentValues()[0], color.getColorComponentValues()[1], color.getColorComponentValues()[2]);
            }
        }
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }
        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

}
