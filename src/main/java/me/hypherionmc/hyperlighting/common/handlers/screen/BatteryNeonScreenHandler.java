package me.hypherionmc.hyperlighting.common.handlers.screen;

import me.hypherionmc.hyperlighting.common.init.HLScreenHandlers;
import me.hypherionmc.hyperlighting.common.inventory.DyeSlot;
import me.hypherionmc.hyperlighting.common.inventory.PowerCardSlot;
import me.hypherionmc.hyperlighting.common.items.WirelessPowerCard;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class BatteryNeonScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private BlockPos pos;

    public BatteryNeonScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new SimpleInventory(2));
        this.pos = buf.readBlockPos();
    }

    public BatteryNeonScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(HLScreenHandlers.BATTERY_NEON_HANDLER, syncId);
        checkSize(inventory, 2);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);
        pos = BlockPos.ORIGIN;

        int m;
        int l;

        this.addSlot(new PowerCardSlot(inventory, 1, 27, 20));
        this.addSlot(new DyeSlot(inventory, 0, 7, 20));

        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }

        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }

    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.getStack() != ItemStack.EMPTY) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 0) {
                if (!this.insertItem(itemstack1, 1, 38, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(itemstack1, itemstack);
            } if (index == 1) {
                if (!this.insertItem(itemstack1, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(itemstack1, itemstack);
            } else {
                if (itemstack1.getItem() instanceof DyeItem) {
                    if (!this.insertItem(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (itemstack1.getItem() instanceof WirelessPowerCard) {
                    if (!this.insertItem(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (itemstack1.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, itemstack1);
        }

        return itemstack;
    }

    public BlockPos getPos() {
        return pos;
    }
}
