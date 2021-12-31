package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.util.CustomRenderType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LavaLamp extends Block implements CustomRenderType {

    private static final VoxelShape BOUNDING_BOX = Block.box(6, 0, 6, 10, 12, 10);
    public static BooleanProperty LIT = BlockStateProperties.LIT;

    public LavaLamp(String name, CreativeModeTab group) {
        super(Properties.of(Material.FIRE).randomTicks());
        this.registerDefaultState(this.defaultBlockState().setValue(LIT, false));

        HLItems.ITEMS.register(name, () -> new BlockItem(this, new Item.Properties().tab(group)));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return BOUNDING_BOX;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player playerIn, InteractionHand handIn, BlockHitResult hitFace) {
        if (!worldIn.isClientSide) {
            state = state.cycle(LIT);
            worldIn.setBlock(pos, state, 2);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        if (state.getValue(LIT)) {
            return 10;
        }
        return 0;
    }

    @Override
    public RenderType getRenderType() {
        return RenderType.cutoutMipped();
    }

}
