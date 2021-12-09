package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.items.BlockItemColor;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;
import java.util.function.ToIntFunction;

public class SuspiciousLamp extends Block {

    public static final BooleanProperty POWERED = Properties.POWERED;

    public SuspiciousLamp(String name, ItemGroup group) {
        super(Settings.of(Material.GLASS).sounds(BlockSoundGroup.GLASS).ticksRandomly().luminance(getLightLevel()));
        this.setDefaultState(this.getDefaultState().with(POWERED, false));

        HLItems.register(name, new BlockItemColor(this, new FabricItemSettings().group(group)));
    }

    public SuspiciousLamp(String name, ItemGroup group, Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(POWERED, false));

        HLItems.register(name, new BlockItemColor(this, new FabricItemSettings().group(group)));
    }

    public static ToIntFunction<BlockState> getLightLevel() {
        return state -> (state.get(POWERED) && FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) ? 15 : 0;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (!world.isClient) {
            boolean flag = state.get(POWERED);
            if (flag != world.isReceivingRedstonePower(pos)) {
                if (flag) {
                    world.createAndScheduleBlockTick(pos, this, 4);
                } else {
                    world.setBlockState(pos, state.cycle(POWERED), 2);
                }
            }
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(POWERED) && !world.isReceivingRedstonePower(pos)) {
            world.setBlockState(pos, state.cycle(POWERED), 2);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
        super.appendProperties(builder);
    }

}
