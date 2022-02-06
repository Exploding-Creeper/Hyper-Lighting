package me.hypherionmc.hyperlighting.common.containers;

import me.hypherionmc.hyperlighting.common.init.HLContainers;
import me.hypherionmc.hyperlighting.common.items.WirelessPowerCard;
import me.hypherionmc.hyperlighting.common.items.WirelessSwitchCard;
import me.hypherionmc.hyperlighting.common.tile.TileSwitchBoard;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerSwitchBoard extends AbstractContainerMenu {

    private final TileSwitchBoard te;
    IItemHandler handler;

    public ContainerSwitchBoard(int windowID, Level world, BlockPos pos, Inventory inventory, Player player) {
        super(HLContainers.SWITCHBOARD_CONTAINER.get(), windowID);
        this.te = (TileSwitchBoard) world.getBlockEntity(pos);

        if (te != null) {
            te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(h -> {
                handler = h;
            });
        }

        this.addSlot(new SlotItemHandler(handler, 0, 25, 14));
        this.addSlot(new SlotItemHandler(handler, 1, 74, 14));
        this.addSlot(new SlotItemHandler(handler, 2, 123, 14));
        this.addSlot(new SlotItemHandler(handler, 3, 25, 44));
        this.addSlot(new SlotItemHandler(handler, 4, 74, 44));
        this.addSlot(new SlotItemHandler(handler, 5, 123, 44));

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(inventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(inventory, x, 8 + x * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < 6) {
                if (!this.moveItemStackTo(itemstack1, index, 42, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else {
                if (itemstack1.getItem() instanceof WirelessSwitchCard) {
                    for (int i = 0; i < 6; i++) {
                        if (!slots.get(i).hasItem()) {
                            if (!this.moveItemStackTo(itemstack1, i, 6, false)) {
                                return ItemStack.EMPTY;
                            }
                        }
                    }
                }
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    public TileSwitchBoard getTe() {
        return te;
    }
}
