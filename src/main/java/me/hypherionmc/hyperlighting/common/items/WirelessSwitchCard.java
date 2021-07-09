package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.hyperlighting.HyperLighting;
import me.hypherionmc.hyperlighting.api.RemoteSwitchable;
import me.hypherionmc.hyperlighting.api.SwitchModule;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.Properties;

public class WirelessSwitchCard extends Item implements SwitchModule {

    public WirelessSwitchCard() {
        super(new Properties().group(HyperLighting.machinesTab));
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        super.onCreated(stack, worldIn, playerIn);
        if (!stack.hasTag()) {
            CompoundNBT compound = new CompoundNBT();
            stack.setTag(compound);
        }
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World worldIn = context.getWorld();
        PlayerEntity player = context.getPlayer();
        BlockPos pos = context.getPos();
        Hand hand = context.getHand();

        if (!worldIn.isRemote) {
            if (player.getHeldItem(hand).getItem() instanceof WirelessSwitchCard) {
                ItemStack stack = player.getHeldItem(hand);
                Block blk = worldIn.getBlockState(pos).getBlock();
                if (blk instanceof RemoteSwitchable) {
                    CompoundNBT compound = stack.getTag();
                    if (compound == null) {
                        compound = new CompoundNBT();
                    }
                    compound.putInt("blockx", pos.getX());
                    compound.putInt("blocky", pos.getY());
                    compound.putInt("blockz", pos.getZ());
                    stack.setTag(compound);
                    player.sendStatusMessage(new TranslationTextComponent("Linked to " + pos), true);
                    return ActionResultType.PASS;
                }
            } else {
                ItemStack stack = player.getHeldItem(hand);
                CompoundNBT compound = stack.getTag();
                if (compound != null) {
                    int x, y, z;
                    x = compound.getInt("blockx");
                    y = compound.getInt("blocky");
                    z = compound.getInt("blockz");
                    BlockPos poss = new BlockPos(x, y, z);
                    player.sendStatusMessage(new TranslationTextComponent("Linked to block " + poss), true);
                    return ActionResultType.PASS;
                } else {
                    player.sendStatusMessage(new TranslationTextComponent("Not linked"), true);
                    return ActionResultType.PASS;
                }
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!worldIn.isRemote) {
            ItemStack stack = playerIn.getHeldItem(handIn);
            CompoundNBT compound = stack.getTag();
            if (compound != null) {
                int x, y, z;
                x = compound.getInt("blockx");
                y = compound.getInt("blocky");
                z = compound.getInt("blockz");
                BlockPos poss = new BlockPos(x, y, z);
                playerIn.sendStatusMessage(new TranslationTextComponent("Linked to block " + poss), true);
                return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
            } else {
                playerIn.sendStatusMessage(new TranslationTextComponent("Not linked"), true);
                return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
            }
        }
        return new ActionResult<>(ActionResultType.PASS, playerIn.getHeldItem(handIn));
    }
}