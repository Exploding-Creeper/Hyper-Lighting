package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.utils.BlockUtils;
import me.hypherionmc.hyperlighting.utils.CustomRenderType;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class LavaLamp extends Block implements CustomRenderType {

    private static final VoxelShape BOUNDING_BOX = Block.createCuboidShape(6, 0, 6, 10, 12, 10);
    public static BooleanProperty LIT = Properties.LIT;

    public LavaLamp(String name, ItemGroup group) {
        super(Settings.of(Material.FIRE).ticksRandomly().luminance(BlockUtils.createLightLevelFromLitBlockState(10)));
        this.setDefaultState(this.getDefaultState().with(LIT, false));

        HLItems.register(name, new BlockItem(this, new FabricItemSettings().group(group)));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return BOUNDING_BOX;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
        super.appendProperties(builder);
    }

    @Override
    public ActionResult onUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!worldIn.isClient) {
            state = state.cycle(LIT);
            worldIn.setBlockState(pos, state, 2);
        }
        return ActionResult.CONSUME;
    }

    @Override
    public RenderLayer getCustomRenderType() {
        return RenderLayer.getCutoutMipped();
    }
}
