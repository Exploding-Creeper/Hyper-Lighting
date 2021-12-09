package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.hyperlighting.HyperLightingFabric;
import me.hypherionmc.hyperlighting.api.SwitchModule;
import me.hypherionmc.hyperlighting.common.blocks.SolarPanel;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WirelessPowerCard extends Item implements SwitchModule {

    public WirelessPowerCard() {
        super(new FabricItemSettings().group(HyperLightingFabric.machinesTab));
    }

    @Override
    public void onCraft(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        super.onCraft(stack, worldIn, playerIn);
        if (!stack.hasNbt()) {
            NbtCompound compound = new NbtCompound();
            stack.setNbt(compound);
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World worldIn = context.getWorld();
        PlayerEntity player = context.getPlayer();
        BlockPos pos = context.getBlockPos();
        Hand hand = context.getHand();

        if (!worldIn.isClient) {
            if (player.getStackInHand(hand).getItem() instanceof WirelessPowerCard wirelessPowerCard) {
                ItemStack stack = player.getStackInHand(hand);
                Block blk = worldIn.getBlockState(pos).getBlock();
                if (blk instanceof SolarPanel panel) {
                    NbtCompound compound = stack.getNbt();
                    if (compound == null) {
                        compound = new NbtCompound();
                    }
                    compound.putInt("blockx", pos.getX());
                    compound.putInt("blocky", pos.getY());
                    compound.putInt("blockz", pos.getZ());
                    stack.setNbt(compound);
                    player.sendMessage(new LiteralText("Linked to " + pos), true);
                    return ActionResult.PASS;
                }
            } else {
                ItemStack stack = player.getStackInHand(hand);
                NbtCompound compound = stack.getNbt();
                if (compound != null) {
                    int x, y, z;
                    x = compound.getInt("blockx");
                    y = compound.getInt("blocky");
                    z = compound.getInt("blockz");
                    BlockPos poss = new BlockPos(x, y, z);
                    player.sendMessage(new LiteralText("Linked to block " + poss), true);
                    return ActionResult.PASS;
                } else {
                    player.sendMessage(new LiteralText("Not linked"), true);
                    return ActionResult.PASS;
                }
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            ItemStack stack = user.getStackInHand(hand);
            NbtCompound compound = stack.getNbt();
            if (compound != null) {
                int x, y, z;
                x = compound.getInt("blockx");
                y = compound.getInt("blocky");
                z = compound.getInt("blockz");
                BlockPos poss = new BlockPos(x, y, z);
                user.sendMessage(new LiteralText("Linked to block " + poss), true);
                return new TypedActionResult<>(ActionResult.SUCCESS, stack);
            } else {
                user.sendMessage(new LiteralText("Not linked"), true);
                return new TypedActionResult<>(ActionResult.SUCCESS, stack);
            }
        }
        return new TypedActionResult<>(ActionResult.SUCCESS, user.getStackInHand(hand));
    }
}
