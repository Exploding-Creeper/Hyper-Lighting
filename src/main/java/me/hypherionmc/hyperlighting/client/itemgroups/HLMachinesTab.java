package me.hypherionmc.hyperlighting.client.itemgroups;

import me.hypherionmc.hyperlighting.common.init.HLBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class HLMachinesTab extends ItemGroup {

    public HLMachinesTab(String label) {
        super(label);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Item.getItemFromBlock(HLBlocks.SOLAR_PANEL.get()));
    }
}
