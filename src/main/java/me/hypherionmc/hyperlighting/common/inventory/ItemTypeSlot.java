package me.hypherionmc.hyperlighting.common.inventory;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class ItemTypeSlot extends Slot {

    private final Item validItem;
    private final int maxSize;

    public ItemTypeSlot(Inventory inventory, int index, int x, int y, Item validItem, int maxSize) {
        super(inventory, index, x, y);
        this.validItem = validItem;
        this.maxSize = Math.min(maxSize, 64);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return validItem.getClass().isAssignableFrom(stack.getItem().getClass());
    }

    @Override
    public int getMaxItemCount() {
        return this.maxSize;
    }

    @Override
    public int getMaxItemCount(ItemStack stack) {
        return this.maxSize;
    }


}
