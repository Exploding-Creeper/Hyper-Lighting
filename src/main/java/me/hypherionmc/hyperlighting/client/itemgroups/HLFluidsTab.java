package me.hypherionmc.hyperlighting.client.itemgroups;

import me.hypherionmc.hyperlighting.common.init.HLFluids;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

public class HLFluidsTab extends CreativeModeTab {

    public HLFluidsTab(String label) {
        super(label);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(HLFluids.getWaterBottle(DyeColor.CYAN, false));
    }
}
