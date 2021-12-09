package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.HyperLightingFabric;
import me.hypherionmc.hyperlighting.api.DyeAble;
import me.hypherionmc.hyperlighting.api.Lightable;
import me.hypherionmc.hyperlighting.common.handlers.ParticleRegistryHandler;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.items.BlockItemColor;
import me.hypherionmc.hyperlighting.utils.BlockUtils;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class TikiTorch extends Block implements DyeAble, Lightable {

    public static final EnumProperty<DyeColor> COLOR = EnumProperty.of("color", DyeColor.class);
    protected static final VoxelShape STANDING_AABB = Block.createCuboidShape(6, 0, 6, 10, 16, 10);
    public static BooleanProperty LIT = Properties.LIT;

    public TikiTorch(String name, DyeColor color, ItemGroup group) {
        super(Settings.of(Material.DECORATION).noCollision().breakInstantly().luminance(BlockUtils.createLightLevelFromLitBlockState(15)));
        this.setDefaultState(this.getDefaultState().with(LIT, HyperLightingFabric.hyperLightingConfig.tikiOnByDefault).with(COLOR, color));

        HLItems.register(name, new BlockItemColor(this, new FabricItemSettings().group(group)));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return STANDING_AABB;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT, COLOR);
        super.appendProperties(builder);
    }

    @Override
    public void toggleLight(World worldIn, BlockState state, BlockPos pos) {
        state = state.with(LIT, !state.get(LIT));
        worldIn.setBlockState(pos, state, 2);
        if (!state.get(LIT)) {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.3f, 1.0f);
        }
        worldIn.updateNeighbors(pos, this);
    }

    @Override
    public BlockColorProvider dyeHandler() {
        return (state, world, pos, tintIndex) -> {
            if (state.get(LIT)) {
                return state.get(COLOR).getMapColor().color;
            } else {
                return DyeColor.BLACK.getMapColor().color;
            }
        };
    }

    @Override
    public DyeColor defaultDyeColor() {
        return this.getDefaultState().get(COLOR);
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
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(new LiteralText(Formatting.YELLOW + "Dyable"));
        tooltip.add(new LiteralText(Formatting.GREEN + "Color: " + defaultDyeColor().name()));
        tooltip.add(new LiteralText(Formatting.BLUE + "Colored Lighting Supported"));
        super.appendTooltip(stack, world, tooltip, options);
    }

    @Override
    public void randomDisplayTick(BlockState stateIn, World worldIn, BlockPos pos, Random random) {
        if (stateIn.get(LIT)) {
            DyeColor color = stateIn.get(COLOR);

            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY() + 0.6D;
            double d2 = (double) pos.getZ() + 0.5D;

            worldIn.addParticle(ParticleTypes.SMOKE, d0, d1 + 0.4f, d2, 0.0D, 0.0D, 0.0D);
            worldIn.addParticle(ParticleRegistryHandler.CUSTOM_FLAME, d0, d1 + 0.4f, d2, color.getColorComponents()[0], color.getColorComponents()[1], color.getColorComponents()[2]);
        }
    }

}
