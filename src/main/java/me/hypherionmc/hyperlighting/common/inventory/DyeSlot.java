package me.hypherionmc.hyperlighting.common.inventory;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class DyeSlot extends Slot {

    public DyeSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem() instanceof DyeItem;
    }

    @Override
    public int getMaxItemCount(ItemStack stack) {
        return 1;
    }
}
