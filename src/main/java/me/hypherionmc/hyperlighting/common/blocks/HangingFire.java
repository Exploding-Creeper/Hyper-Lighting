package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.api.DyeAble;
import me.hypherionmc.hyperlighting.api.Lightable;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.items.BlockItemColor;
import me.hypherionmc.hyperlighting.utils.BlockUtils;
import me.hypherionmc.hyperlighting.utils.CustomRenderType;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
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
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HangingFire extends Block implements CustomRenderType, DyeAble, Lightable {

    public static final EnumProperty<DyeColor> COLOR = EnumProperty.of("color", DyeColor.class);
    public static EnumProperty<WallMountLocation> ATTACH_FACE = Properties.WALL_MOUNT_LOCATION;
    public static BooleanProperty LIT = Properties.LIT;

    public HangingFire(String name, DyeColor color, ItemGroup group) {
        super(Settings.of(Material.METAL).nonOpaque().noCollision().sounds(BlockSoundGroup.METAL).luminance(BlockUtils.createLightLevelFromLitBlockState(15)));
        this.setDefaultState(this.getDefaultState().with(ATTACH_FACE, WallMountLocation.CEILING).with(LIT, true).with(COLOR, color));

        HLItems.register(name, new BlockItemColor(this, new FabricItemSettings().group(group)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ATTACH_FACE, LIT, COLOR);
        super.appendProperties(builder);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        if (ctx.getSide() == Direction.DOWN) {
            return this.getDefaultState().with(ATTACH_FACE, WallMountLocation.CEILING);
        }
        return Blocks.AIR.getDefaultState();
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction != Direction.DOWN) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public RenderLayer getCustomRenderType() {
        return RenderLayer.getCutoutMipped();
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

}
