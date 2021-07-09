package me.hypherionmc.hyperlighting.util;

import net.minecraftforge.fml.ModList;

public class ModUtils {

    public static boolean isRGBLibPresent() {
        return ModList.get().isLoaded("rgblib");
    }
    public static boolean isClothConfigPresent() {
        return ModList.get().isLoaded("cloth-config");
    }

}
