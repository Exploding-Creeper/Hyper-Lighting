package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.hyperlighting.HyperLighting;
import me.hypherionmc.hyperlighting.util.ModUtils;
import net.minecraft.world.item.Item;

public class CandleInAJar extends Item {

    public CandleInAJar() {
        super(new Properties().tab(HyperLighting.mainTab));

        if (ModUtils.isRGBLibPresent()) {
            //ColoredLightManager.registerProvider(this, this::produceColoredLight);
        }
    }

    /*private RGBLight produceColoredLight(Entity entity, ItemStack stack) {
        //return RGBLight.builder().pos(APIUtils.entityPos(entity)).color(DyeColor.YELLOW.getMaterialColor().col, false).radius(14).build();
        return null;
    }*/

}
