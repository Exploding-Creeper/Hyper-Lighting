package me.hypherionmc.hyperlighting.client.itemgroups;

import me.hypherionmc.hyperlighting.common.init.HLBlocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class HLLightingTab extends CreativeModeTab {

    public HLLightingTab(String label) {
        super(label);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(Item.byBlock(HLBlocks.ADVANCED_LANTERN_BLUE.get()));
    }
}
