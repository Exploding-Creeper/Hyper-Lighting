package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.hyperlighting.HyperLighting;
import me.hypherionmc.hyperlighting.common.network.PacketHandler;
import me.hypherionmc.hyperlighting.common.network.packets.FogMachineFirePacket;
import me.hypherionmc.hyperlighting.common.tile.TileFogMachine;
import me.hypherionmc.hyperlighting.util.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class FogMachineRemote extends Item {

    public FogMachineRemote(String name) {
        super(new Properties().tab(HyperLighting.machinesTab));
    }

    @Override
    public void onCraftedBy(ItemStack pStack, Level pLevel, Player pPlayer) {
        super.onCraftedBy(pStack, pLevel, pPlayer);
        if (!pStack.hasTag()) {
            CompoundTag tag = new CompoundTag();
            pStack.setTag(tag);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (!pContext.getLevel().isClientSide) {
            ItemStack stack = pContext.getPlayer().getItemInHand(pContext.getHand());
            if (stack.getItem() instanceof FogMachineRemote) {
                BlockEntity be = pContext.getLevel().getBlockEntity(pContext.getClickedPos());
                if (be instanceof TileFogMachine) {
                    CompoundTag tag = stack.getTag() == null ? new CompoundTag() : stack.getTag();
                    ModUtils.writeBlockPosToNBT(pContext.getClickedPos(), tag);
                    stack.setTag(tag);
                    pContext.getPlayer().displayClientMessage(new TextComponent("Linked to " + pContext.getClickedPos().toString()), true);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (!pLevel.isClientSide) {
            CompoundTag compound = stack.getTag();
            if (compound != null) {
                BlockPos poss = ModUtils.readBlockPosFromNBT(stack.getTag());
                if (pLevel.getBlockEntity(poss) instanceof TileFogMachine tileFogMachine) {
                    FogMachineFirePacket firePacket = new FogMachineFirePacket(tileFogMachine.getBlockPos());
                    PacketHandler.sendToServer(firePacket);
                } else {
                    pPlayer.displayClientMessage(new TextComponent("Not linked"), true);
                }
                return new InteractionResultHolder(InteractionResult.SUCCESS, stack);
            } else {
                pPlayer.displayClientMessage(new TextComponent("Not linked"), true);
                return new InteractionResultHolder(InteractionResult.SUCCESS, stack);
            }
        }
        return new InteractionResultHolder(InteractionResult.PASS, stack);
    }
}
