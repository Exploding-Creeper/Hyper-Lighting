package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.hyperlighting.HyperLightingFabric;
import me.hypherionmc.hyperlighting.api.Lightable;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class LighterTool extends Item {

    public LighterTool() {
        super(new FabricItemSettings().maxCount(1).group(HyperLightingFabric.mainTab));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient) {
            BlockState block = context.getWorld().getBlockState(context.getBlockPos());
            if (block.getBlock() instanceof Lightable) {
                ((Lightable) block.getBlock()).toggleLight(context.getWorld(), block, context.getBlockPos());
            }
        }
        return ActionResult.CONSUME;
    }
}
