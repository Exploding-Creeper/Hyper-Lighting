package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.hyperlighting.HyperLighting;
import me.hypherionmc.hyperlighting.util.ModUtils;
import me.hypherionmc.rgblib.api.APIUtils;
import me.hypherionmc.rgblib.api.ColoredLightManager;
import me.hypherionmc.rgblib.api.RGBLight;
import net.minecraft.entity.Entity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CandleInAJar extends Item {

    public CandleInAJar() {
        super(new Properties().group(HyperLighting.mainTab));

        if (ModUtils.isRGBLibPresent()) {
            ColoredLightManager.registerProvider(this, this::produceColoredLight);
        }
    }

    private RGBLight produceColoredLight(Entity entity, ItemStack stack) {
        return RGBLight.builder().pos(APIUtils.entityPos(entity)).color(DyeColor.YELLOW.getColorValue(), false).radius(14).build();
    }

}
