package me.hypherionmc.hyperlighting.client.itemgroups;

import me.hypherionmc.hyperlighting.common.init.HLBlocks;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class HLLightingTab extends ItemGroup {

    public HLLightingTab(String label) {
        super(label);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Item.getItemFromBlock(HLBlocks.ADVANCED_LANTERN_BLUE.get()));
    }
}
