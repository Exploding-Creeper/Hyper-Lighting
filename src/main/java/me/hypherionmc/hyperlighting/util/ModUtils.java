package me.hypherionmc.hyperlighting.util;

import net.minecraftforge.fml.ModList;

import java.util.Random;

public class ModUtils {

    public static boolean isRGBLibPresent() {
        return ModList.get().isLoaded("rgblib");
    }
    public static boolean isClothConfigPresent() {
        return ModList.get().isLoaded("cloth-config");
    }

    public static float floatInRange(float min, float max)
    {
        Random r = new Random();
        return(min + (max - min) * r.nextFloat());
    }

}
