package me.hypherionmc.hyperlighting.common.handlers.screen;

import me.hypherionmc.hyperlighting.common.init.HLScreenHandlers;
import me.hypherionmc.hyperlighting.common.items.WirelessSwitchCard;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class SwitchBoardScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private BlockPos pos;

    public SwitchBoardScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new SimpleInventory(6));
        this.pos = buf.readBlockPos();
    }

    public SwitchBoardScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(HLScreenHandlers.SWITCH_BOARD_HANDLER, syncId);
        checkSize(inventory, 6);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);
        pos = BlockPos.ORIGIN;

        int m;
        int l;

        this.addSlot(new Slot(inventory, 0, 25, 14));
        this.addSlot(new Slot(inventory, 1, 74, 14));
        this.addSlot(new Slot(inventory, 2, 123, 14));
        this.addSlot(new Slot(inventory, 3, 25, 44));
        this.addSlot(new Slot(inventory, 4, 74, 44));
        this.addSlot(new Slot(inventory, 5, 123, 44));

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

            if (index < 6) {
                if (!this.insertItem(itemstack1, index, 42, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(itemstack1, itemstack);
            } else {
                if (itemstack1.getItem() instanceof WirelessSwitchCard) {
                    for (int i = 0; i < 6; i++) {
                        if (slots.get(i).getStack() != ItemStack.EMPTY) {
                            if (!this.insertItem(itemstack1, i, 6, false)) {
                                return ItemStack.EMPTY;
                            }
                        }
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
