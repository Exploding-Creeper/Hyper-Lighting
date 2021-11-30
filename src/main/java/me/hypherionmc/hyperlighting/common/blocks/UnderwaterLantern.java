package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.common.config.HyperLightingConfig;
import me.hypherionmc.hyperlighting.common.handlers.ParticleRegistryHandler;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;

import java.util.Random;

public class UnderwaterLantern extends AdvancedLantern implements SimpleWaterloggedBlock {

    public static BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public UnderwaterLantern(String name, CreativeModeTab group) {
        super(name, DyeColor.BLUE, group);
        this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, false).setValue(LIT, HyperLightingConfig.underwaterOnByDefault.get()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public void animateTick(BlockState state, Level worldIn, BlockPos pos, Random rand) {
        if (worldIn.isClientSide && state.getValue(LIT)) {
            DyeColor color = DyeColor.values()[rand.nextInt(DyeColor.values().length)];
            Direction direction = state.getValue(HORIZONTAL_FACING);

            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY() + 0.7D;
            double d2 = (double) pos.getZ() + 0.5D;

            if (state.getValue(FACE) == AttachFace.WALL) {
                Direction direction1 = direction.getOpposite();
                worldIn.addParticle(ParticleTypes.SMOKE, d0 + 0.27D * (double)direction1.getStepX(), d1 , d2 + 0.27D * (double)direction1.getStepZ(), 0.0D, 0.0D, 0.0D);
                worldIn.addParticle(ParticleRegistryHandler.CUSTOM_FLAME.get(), d0 + 0.27D * (double)direction1.getStepX(), d1 - 0.3D, d2 + 0.27D * (double)direction1.getStepZ(), color.getTextureDiffuseColors()[0], color.getTextureDiffuseColors()[1], color.getTextureDiffuseColors()[2]);
            } else if (state.getValue(FACE) == AttachFace.FLOOR) {
                worldIn.addParticle(ParticleTypes.SMOKE, d0, d1 - 0.3D, d2, 0.0D, 0.0D, 0.0D);
                worldIn.addParticle(ParticleRegistryHandler.CUSTOM_FLAME.get(), d0, d1 - 0.5D, d2, color.getTextureDiffuseColors()[0], color.getTextureDiffuseColors()[1], color.getTextureDiffuseColors()[2]);
            } else if (state.getValue(FACE) == AttachFace.CEILING) {
                worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                worldIn.addParticle(ParticleRegistryHandler.CUSTOM_FLAME.get(), d0, d1 - 0.3D, d2, color.getTextureDiffuseColors()[0], color.getTextureDiffuseColors()[1], color.getTextureDiffuseColors()[2]);
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        }
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

}
