package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.hyperlighting.HyperLighting;
import me.hypherionmc.hyperlighting.api.RemoteSwitchable;
import me.hypherionmc.hyperlighting.api.SwitchModule;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

public class WirelessSwitchCard extends Item implements SwitchModule {

    public WirelessSwitchCard() {
        super(new Properties().tab(HyperLighting.machinesTab));
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level worldIn, Player playerIn) {
        super.onCraftedBy(stack, worldIn, playerIn);
        if (!stack.hasTag()) {
            CompoundTag compound = new CompoundTag();
            stack.setTag(compound);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level worldIn = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        InteractionHand hand = context.getHand();

        if (!worldIn.isClientSide) {
            if (player.getItemInHand(hand).getItem() instanceof WirelessSwitchCard) {
                ItemStack stack = player.getItemInHand(hand);
                Block blk = worldIn.getBlockState(pos).getBlock();
                if (blk instanceof RemoteSwitchable) {
                    CompoundTag compound = stack.getTag();
                    if (compound == null) {
                        compound = new CompoundTag();
                    }
                    compound.putInt("blockx", pos.getX());
                    compound.putInt("blocky", pos.getY());
                    compound.putInt("blockz", pos.getZ());
                    stack.setTag(compound);
                    player.displayClientMessage(new TranslatableComponent("Linked to " + pos), true);
                    return InteractionResult.PASS;
                }
            } else {
                ItemStack stack = player.getItemInHand(hand);
                CompoundTag compound = stack.getTag();
                if (compound != null) {
                    int x, y, z;
                    x = compound.getInt("blockx");
                    y = compound.getInt("blocky");
                    z = compound.getInt("blockz");
                    BlockPos poss = new BlockPos(x, y, z);
                    player.displayClientMessage(new TranslatableComponent("Linked to block " + poss), true);
                    return InteractionResult.PASS;
                } else {
                    player.displayClientMessage(new TranslatableComponent("Not linked"), true);
                    return InteractionResult.PASS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (!worldIn.isClientSide) {
            ItemStack stack = playerIn.getItemInHand(handIn);
            CompoundTag compound = stack.getTag();
            if (compound != null) {
                int x, y, z;
                x = compound.getInt("blockx");
                y = compound.getInt("blocky");
                z = compound.getInt("blockz");
                BlockPos poss = new BlockPos(x, y, z);
                playerIn.displayClientMessage(new TranslatableComponent("Linked to block " + poss), true);
                return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(handIn));
            } else {
                playerIn.displayClientMessage(new TranslatableComponent("Not linked"), true);
                return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(handIn));
            }
        }
        return new InteractionResultHolder<>(InteractionResult.PASS, playerIn.getItemInHand(handIn));
    }
}