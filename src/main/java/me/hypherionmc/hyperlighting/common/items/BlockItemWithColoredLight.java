package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.hyperlighting.util.ModUtils;
import net.minecraft.world.level.block.Block;

public class BlockItemWithColoredLight extends BlockItemColor {

    public BlockItemWithColoredLight(Block blockIn, Properties builder) {
        super(blockIn, builder);

        if (ModUtils.isRGBLibPresent()) {
            //ColoredLightManager.registerProvider(this, this::produceColoredLight);
        }
    }

    /*private RGBLight produceColoredLight(Entity entity, ItemStack stack) {
        if (stack.getItem() instanceof BlockItemWithColoredLight) {
            DyeColor color = ((DyeAble)((BlockItemWithColoredLight) stack.getItem()).getBlock()).defaultDyeColor();
            //return RGBLight.builder().pos(APIUtils.entityPos(entity)).color(color.getMaterialColor().col, false).radius(14).build();
        }
        return null;
    }*/
}
