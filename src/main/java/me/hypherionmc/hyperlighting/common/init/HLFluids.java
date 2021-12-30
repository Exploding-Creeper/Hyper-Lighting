package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.hyperlighting.HyperLightingFabric;
import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.common.blocks.ColoredWaterBlock;
import me.hypherionmc.hyperlighting.common.fluids.ColoredWater;
import me.hypherionmc.hyperlighting.common.items.ColoredWaterBucketItem;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;

public class HLFluids {

    public static final HashMap<DyeColor, ColoredWaterEntry> COLORED_WATER = new HashMap<>();
    public static final HashMap<DyeColor, ColoredWaterEntry> GLOWING_COLORED_WATER = new HashMap<>();

    public static void registerWaterColors() {
        for (DyeColor color : DyeColor.values()) {
            COLORED_WATER.put(color, new ColoredWaterEntry(color, false));
            GLOWING_COLORED_WATER.put(color, new ColoredWaterEntry(color, true));
        }
        registerFluidBlocks();
    }

    private static void registerFluidBlocks() {
        COLORED_WATER.forEach((color, coloredWaterEntry) -> {
            coloredWaterEntry.setBLOCK(HLBlocks.register(color.getName().toLowerCase() + "_colored_water", new ColoredWaterBlock(coloredWaterEntry.STILL, FabricBlockSettings.copy(Blocks.WATER))));
        });

        GLOWING_COLORED_WATER.forEach((color, coloredWaterEntry) -> {
            coloredWaterEntry.setBLOCK(HLBlocks.register(color.getName().toLowerCase() + "_colored_glowing_water", new ColoredWaterBlock(coloredWaterEntry.STILL, FabricBlockSettings.copy(Blocks.WATER).luminance((state) -> 15))));
        });
    }

    public static FlowableFluid getFluidFlowing(DyeColor color, boolean isGlowing) {
        return isGlowing ? GLOWING_COLORED_WATER.get(color).FLOWING : COLORED_WATER.get(color).FLOWING;
    }

    public static FlowableFluid getFluidStill(DyeColor color, boolean isGlowing) {
        return isGlowing ? GLOWING_COLORED_WATER.get(color).STILL : COLORED_WATER.get(color).STILL;
    }

    public static Block getFluidBlock(DyeColor color, boolean isGlowing) {
        return isGlowing ? GLOWING_COLORED_WATER.get(color).BLOCK : COLORED_WATER.get(color).BLOCK;
    }

    public static Item getFluidBucket(DyeColor color, boolean isGlowing) {
        return isGlowing ? GLOWING_COLORED_WATER.get(color).BUCKET : COLORED_WATER.get(color).BUCKET;
    }

    public static HashMap<DyeColor, ColoredWaterEntry> getFluidMap() {
        return COLORED_WATER;
    }
    public static HashMap<DyeColor, ColoredWaterEntry> getGlowingFluidMap() {
        return GLOWING_COLORED_WATER;
    }

    public static class ColoredWaterEntry {
        private final FlowableFluid STILL;
        private final FlowableFluid FLOWING;
        private Block BLOCK = null;
        private final Item BUCKET;

        public ColoredWaterEntry(DyeColor color, boolean isGlowing) {
            STILL = Registry.register(Registry.FLUID, new Identifier(ModConstants.MOD_ID, color.getName().toLowerCase() + (isGlowing ? "_colored_glowing_water_still" : "_colored_water_still")), new ColoredWater.Still(color, isGlowing));
            FLOWING = Registry.register(Registry.FLUID, new Identifier(ModConstants.MOD_ID, color.getName().toLowerCase() + (isGlowing ? "_colored_glowing_water_flowing" : "_colored_water_flowing")), new ColoredWater.Flowing(color, isGlowing));
            BUCKET = HLItems.register(color.getName().toLowerCase() + (isGlowing ? "_colored_glowing_water_bucket" : "_colored_water_bucket"), new ColoredWaterBucketItem(STILL, new Item.Settings().recipeRemainder(Items.BUCKET).group(HyperLightingFabric.fluidsTab).maxCount(1), color, isGlowing));
        }

        public FlowableFluid getFLOWING() {
            return FLOWING;
        }

        public FlowableFluid getSTILL() {
            return STILL;
        }

        public Block getBLOCK() {
            return BLOCK;
        }

        public void setBLOCK(Block BLOCK) {
            this.BLOCK = BLOCK;
        }

        public Item getBUCKET() {
            return BUCKET;
        }
    }
}
