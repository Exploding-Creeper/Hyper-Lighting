package me.hypherionmc.hyperlighting.common.containers;

import me.hypherionmc.hyperlighting.common.init.HLContainers;
import me.hypherionmc.hyperlighting.common.tile.TileSwitchBoard;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerSwitchBoard extends Container {

    private final TileSwitchBoard te;
    IItemHandler handler;

    public ContainerSwitchBoard(int windowID, World world, BlockPos pos, PlayerInventory inventory, PlayerEntity player) {
        super(HLContainers.SWITCHBOARD_CONTAINER.get(), windowID);
        this.te = (TileSwitchBoard) world.getTileEntity(pos);


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

    public TileSwitchBoard getTe() {
        return te;
    }
}