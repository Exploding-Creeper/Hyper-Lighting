package me.hypherionmc.hyperlighting.common.containers;

import me.hypherionmc.hyperlighting.common.init.HLContainers;
import me.hypherionmc.hyperlighting.common.tile.TileSwitchBoard;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
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
            te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, (Direction) null).ifPresent(h -> {
                handler = h;
            });
        }

        this.addSlot(new SlotItemHandler(handler, 0, 25, 14));
        this.addSlot(new SlotItemHandler(handler, 1, 74, 14));
        this.addSlot(new SlotItemHandler(handler, 2, 123, 14));
        this.addSlot(new SlotItemHandler(handler, 3, 25, 44));
        this.addSlot(new SlotItemHandler(handler, 4, 74, 44));
        this.addSlot(new SlotItemHandler(handler, 5, 123, 44));

        for(int y = 0; y < 3; y++)
        {
            for(int x = 0; x < 9; x++)
            {
                this.addSlot(new Slot(inventory, x + y*9 + 9, 8 + x*18, 84 + y*18));
            }
        }

        for(int x = 0; x < 9; x++)
        {
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
        return super.quickMoveStack(playerIn, index);
    }

    public TileSwitchBoard getTe() {
        return te;
    }
}