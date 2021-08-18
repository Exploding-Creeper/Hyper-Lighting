package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.items.BlockItemColor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.Random;

public class SuspiciousLamp extends Block {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public SuspiciousLamp(String name, CreativeModeTab group) {
        super(Properties.of(Material.GLASS).sound(SoundType.GLASS).randomTicks());
        this.registerDefaultState(this.defaultBlockState().setValue(POWERED, false));

        HLItems.ITEMS.register(name, () -> new BlockItemColor(this, new Item.Properties().tab(group)));
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        if (state.getValue(POWERED) && !FMLEnvironment.dist.isClient()) {
            return 15;
        } else {
            return 0;
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide) {
            boolean flag = state.getValue(POWERED);
            if (flag != worldIn.hasNeighborSignal(pos)) {
                if (flag) {
                    worldIn.getBlockTicks().scheduleTick(pos, this, 4);
                } else {
                    worldIn.setBlock(pos, state.cycle(POWERED), 2);
                }
            }

        }
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
        if (state.getValue(POWERED) && !worldIn.hasNeighborSignal(pos)) {
            worldIn.setBlock(pos, state.cycle(POWERED), 2);
        }

    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, world, pos, neighbor);
    }
}
