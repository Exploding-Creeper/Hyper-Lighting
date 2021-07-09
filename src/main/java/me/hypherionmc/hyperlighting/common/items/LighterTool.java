package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.hyperlighting.HyperLighting;
import me.hypherionmc.hyperlighting.api.Lightable;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

public class LighterTool extends Item {

    public LighterTool() {
        super(new Properties().maxStackSize(1).group(HyperLighting.mainTab));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if (!context.getWorld().isRemote) {
            BlockState block = context.getWorld().getBlockState(context.getPos());
            if (block.getBlock() instanceof Lightable) {
                ((Lightable) block.getBlock()).toggleLight(context.getWorld(), block, context.getPos());
            }
        }
        return ActionResultType.CONSUME;
    }
}
