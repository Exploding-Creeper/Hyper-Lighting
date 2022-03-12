package me.hypherionmc.hyperlighting.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.fml.ModList;

import java.util.Random;

public class ModUtils {

    public static boolean isRGBLibPresent() {
        return ModList.get().isLoaded("rgblib");
    }

    public static boolean isClothConfigPresent() {
        return ModList.get().isLoaded("cloth-config");
    }

    public static float floatInRange(float min, float max) {
        Random r = new Random();
        return (min + (max - min) * r.nextFloat());
    }

    public static int fluidColorFromDye(DyeColor color) {
        return color.getMaterialColor().col | 0xFF000000;
    }

    public static void writeBlockPosToNBT(BlockPos pos, CompoundTag tag) {
        tag.putInt("block_x", pos.getX());
        tag.putInt("block_y", pos.getY());
        tag.putInt("block_z", pos.getZ());
    }

    public static BlockPos readBlockPosFromNBT(CompoundTag tag) {
        int x, y, z;
        x = tag.getInt("block_x");
        y = tag.getInt("block_y");
        z = tag.getInt("block_z");
        return new BlockPos(x, y, z);
    }

}
