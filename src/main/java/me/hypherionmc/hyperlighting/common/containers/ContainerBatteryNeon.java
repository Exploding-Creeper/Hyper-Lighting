package me.hypherionmc.hyperlighting.common.containers;

import me.hypherionmc.hyperlighting.common.init.HLContainers;
import me.hypherionmc.hyperlighting.common.tile.TileBatteryNeon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerBatteryNeon extends AbstractContainerMenu {

    private final TileBatteryNeon te;

    public ContainerBatteryNeon(int windowID, Level world, BlockPos pos, Inventory inventory, Player player) {
        super(HLContainers.BATTERY_NEON_CONTAINER.get(), windowID);
        this.te = (TileBatteryNeon) world.getBlockEntity(pos);
        IItemHandler handler = te.getItemStackHandler();
        IItemHandler dyeHandler = te.getDyeHandler();

        this.addSlot(new SlotItemHandler(handler, 0, 27, 20));
        this.addSlot(new SlotItemHandler(dyeHandler, 0, 7, 20));

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

    public TileBatteryNeon getTe() {
        return te;
    }
}