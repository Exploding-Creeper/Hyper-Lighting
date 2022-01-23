package me.hypherionmc.hyperlighting.utils;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

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

    public static void putFluid(NbtCompound compound, String key, FluidVariant fluidVariant) {
        NbtCompound savedTag = new NbtCompound();
        savedTag.put("fk", fluidVariant.toNbt());
        compound.put(key, savedTag);
    }

    public static FluidVariant getFluidCompatible(NbtCompound tag, String key) {
        if (tag == null || !tag.contains(key))
            return FluidVariant.blank();

        if (tag.get(key) instanceof NbtString) {
            return FluidVariant.of(Registry.FLUID.get(new Identifier(tag.getString(key))));
        } else {
            NbtCompound compound = tag.getCompound(key);
            if (compound.contains("fk")) {
                return FluidVariant.fromNbt(compound.getCompound("fk"));
            } else {
                return FluidVariant.of(readLbaTag(tag.getCompound(key)));
            }
        }
    }

    private static Fluid readLbaTag(NbtCompound tag) {
        if (tag.contains("ObjName") && tag.getString("Registry").equals("f")) {
            return Registry.FLUID.get(new Identifier(tag.getString("ObjName")));
        } else {
            return Fluids.EMPTY;
        }
    }

    public static void writeBlockPosToNBT(BlockPos pos, NbtCompound tag) {
        tag.putInt("block_x", pos.getX());
        tag.putInt("block_y", pos.getY());
        tag.putInt("block_z", pos.getZ());
    }

    public static BlockPos readBlockPosFromNBT(NbtCompound tag) {
        int x, y, z;
        x = tag.getInt("block_x");
        y = tag.getInt("block_y");
        z = tag.getInt("block_z");
        return new BlockPos(x, y, z);
    }

}
