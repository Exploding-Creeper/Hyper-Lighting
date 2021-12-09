package me.hypherionmc.hyperlighting.common.inventory;

import me.hypherionmc.hyperlighting.common.init.HLItems;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class PowerCardSlot extends Slot {

    public PowerCardSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem() == HLItems.WIRELESS_POWERCARD;
    }

    @Override
    public int getMaxItemCount(ItemStack stack) {
        return 1;
    }
}
