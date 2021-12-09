package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.HyperLightingFabric;
import me.hypherionmc.hyperlighting.common.blockentities.SolarPanelBlockEntity;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SolarPanel extends Block implements BlockEntityProvider {

    private final VoxelShape BOUNDS = Block.createCuboidShape(0, 0, 0, 16, 1.92, 16);

    public SolarPanel(String name) {
        super(Settings.of(Material.GLASS));

        HLItems.register(name, new BlockItem(this, new FabricItemSettings().group(HyperLightingFabric.machinesTab)));

    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return BOUNDS;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SolarPanelBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        if (level.isClient()) {
            return null;
        }
        return (level1, blockPos, blockState, t) -> {
            if (t instanceof SolarPanelBlockEntity tile) {
                tile.serverTick();
            }
        };
    }

}
