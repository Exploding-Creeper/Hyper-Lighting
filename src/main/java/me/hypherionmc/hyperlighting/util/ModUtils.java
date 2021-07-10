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

    public static int[] splitIntoParts(final int whole, final int parts) {
        final int[] arr = new int[parts];
        int remain = whole;
        int partsLeft = parts;
        for (int i = 0; partsLeft > 0; i++) {
            final int size = ((remain + partsLeft) - 1) / partsLeft; // rounded up, aka ceiling
            arr[i] = size;
            remain -= size;
            partsLeft--;
        }
        return arr;
    }

}
