package me.hypherionmc.hyperlighting.common.containers;

import me.hypherionmc.hyperlighting.common.init.HLContainers;
import me.hypherionmc.hyperlighting.common.tile.TileFogMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerFogMachine extends AbstractContainerMenu {

    private final TileFogMachine te;
    private IItemHandler handler;

    public ContainerFogMachine(int windowID, Level world, BlockPos pos, Inventory inventory, Player player) {
        super(HLContainers.FOG_MACHINE_CONTAINER.get(), windowID);
        this.te = (TileFogMachine) world.getBlockEntity(pos);

        if (te != null) {
            te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(h -> {
                handler = h;
            });
        }

        this.addSlot(new SlotItemHandler(handler, 0, 22, 16));

        for(int y = 0; y < 3; y++) {
            for(int x = 0; x < 9; x++) {
                this.addSlot(new Slot(inventory, x + y * 9 + 9, 8 + x*18, 71 + y * 18));
            }
        }
        for(int x = 0; x < 9; x++) {
            this.addSlot(new Slot(inventory, x, 8 + x * 18, 129));
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

    public TileFogMachine getTe() {
        return te;
    }
}
