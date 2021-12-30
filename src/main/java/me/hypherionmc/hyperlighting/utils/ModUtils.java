package me.hypherionmc.hyperlighting.utils;

import net.minecraft.util.DyeColor;

import java.util.Random;

public class ModUtils {

    public static float floatInRange(float min, float max) {
        Random r = new Random();
        return (min + (max - min) * r.nextFloat());
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

    public static int fluidColorFromDye(DyeColor color) {
        return color.getMapColor().color | 0xFF000000;
    }

}
