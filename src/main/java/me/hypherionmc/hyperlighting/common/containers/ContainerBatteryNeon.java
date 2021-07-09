package me.hypherionmc.hyperlighting.common.containers;

import me.hypherionmc.hyperlighting.common.init.HLContainers;
import me.hypherionmc.hyperlighting.common.tile.TileBatteryNeon;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerBatteryNeon extends Container {

    private final TileBatteryNeon te;

    public ContainerBatteryNeon(int windowID, World world, BlockPos pos, PlayerInventory inventory, PlayerEntity player) {
        super(HLContainers.BATTERY_NEON_CONTAINER.get(), windowID);
        this.te = (TileBatteryNeon) world.getTileEntity(pos);
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
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        return super.transferStackInSlot(playerIn, index);
    }

    public TileBatteryNeon getTe() {
        return te;
    }
}