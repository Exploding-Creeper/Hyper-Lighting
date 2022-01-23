package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.hyperlighting.HyperLightingFabric;
import me.hypherionmc.hyperlighting.common.blockentities.FogMachineBlockEntity;
import me.hypherionmc.hyperlighting.common.network.FogMachinePacketType;
import me.hypherionmc.hyperlighting.common.network.NetworkHandler;
import me.hypherionmc.hyperlighting.utils.ModUtils;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.entity.BlockEntity;
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

public class FogMachineRemote extends Item {

    public FogMachineRemote() {
        super(new FabricItemSettings().group(HyperLightingFabric.machinesTab));
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        super.onCraft(stack, world, player);
        if (!stack.hasNbt()) {
            stack.setNbt(new NbtCompound());
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient && context.getPlayer() != null) {
            ItemStack stack = context.getPlayer().getStackInHand(context.getHand());
            if (stack.getItem() instanceof FogMachineRemote) {
                BlockEntity be = context.getWorld().getBlockEntity(context.getBlockPos());
                if (be instanceof FogMachineBlockEntity) {
                    NbtCompound tag = stack.getNbt() == null ? new NbtCompound() : stack.getNbt();
                    ModUtils.writeBlockPosToNBT(context.getBlockPos(), tag);
                    stack.setNbt(tag);
                    context.getPlayer().sendMessage(new LiteralText("Linked to " + context.getBlockPos().toString()), true);
                    return ActionResult.SUCCESS;
                }
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient) {
            NbtCompound compound = stack.getNbt();
            if (compound != null) {
                BlockPos poss = ModUtils.readBlockPosFromNBT(stack.getNbt());
                if (world.getBlockEntity(poss) instanceof FogMachineBlockEntity tileFogMachine) {
                    NetworkHandler.sendFogMachinePacket(poss, 0, FogMachinePacketType.FIRE);
                } else {
                    user.sendMessage(new LiteralText("Not linked"), true);
                }
                return TypedActionResult.success(stack);
            } else {
                user.sendMessage(new LiteralText("Not linked"), true);
                return TypedActionResult.success(stack);
            }
        }
        return TypedActionResult.success(stack);
    }
}
