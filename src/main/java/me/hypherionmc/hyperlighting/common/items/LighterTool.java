package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.hyperlighting.HyperLighting;
import me.hypherionmc.hyperlighting.api.Lightable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;

import net.minecraft.world.item.Item.Properties;

public class LighterTool extends Item {

    public LighterTool() {
        super(new Properties().stacksTo(1).tab(HyperLighting.mainTab));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide) {
            BlockState block = context.getLevel().getBlockState(context.getClickedPos());
            if (block.getBlock() instanceof Lightable) {
                ((Lightable) block.getBlock()).toggleLight(context.getLevel(), block, context.getClickedPos());
            }
        }
        return InteractionResult.CONSUME;
    }
}
