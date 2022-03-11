package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.common.config.HyperLightingConfig;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.HashMap;

public class HLWorldGen {
    /*
    private static final HashMap<DyeColor, PlacedFeature> COLORED_WATER_GEN = new HashMap<>();
    private static final HashMap<DyeColor, PlacedFeature> GLOWING_COLORED_WATER_GEN = new HashMap<>();

     public static void register() {
        HLFluids.COLORED_WATER.forEach((color, coloredWaterEntry) -> {
            ConfiguredFeature<?, ?> COLORED_WATER_CF = Feature.LAKE.configured(new LakeFeature.Configuration(BlockStateProvider.simple(coloredWaterEntry.getBLOCK().get()), BlockStateProvider.simple(Blocks.DIRT)));
            PlacedFeature COLORED_WATER_PF = COLORED_WATER_CF.placed(RarityFilter.onAverageOnceEvery(180 * (color.getId() + 1)), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());

            Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(ModConstants.MODID, color.getName().toLowerCase() + "_colored_water_lake"), COLORED_WATER_CF);
            Registry.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(ModConstants.MODID, color.getName().toLowerCase() + "_colored_water_lake"), COLORED_WATER_PF);
            COLORED_WATER_GEN.put(color, COLORED_WATER_PF);
        });

        HLFluids.GLOWING_COLORED_WATER.forEach((color, coloredWaterEntry) -> {
            ConfiguredFeature<?, ?> COLORED_WATER_CF = Feature.LAKE.configured(new LakeFeature.Configuration(BlockStateProvider.simple(coloredWaterEntry.getBLOCK().get()), BlockStateProvider.simple(Blocks.DIRT)));
            PlacedFeature COLORED_WATER_PF = COLORED_WATER_CF.placed(RarityFilter.onAverageOnceEvery(240 * (color.getId() + 1)), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());

            Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(ModConstants.MODID, color.getName().toLowerCase() + "_colored_glowing_water_lake"), COLORED_WATER_CF);
            Registry.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation(ModConstants.MODID, color.getName().toLowerCase() + "_colored_glowing_water_lake"), COLORED_WATER_PF);
            GLOWING_COLORED_WATER_GEN.put(color, COLORED_WATER_PF);
        });
    }

    public static void addWaterWorldgen(final BiomeLoadingEvent event) {
        Biome.BiomeCategory category = event.getCategory();

        if (HyperLightingConfig.genColoredWater.get()) {
            COLORED_WATER_GEN.forEach((color, placedFeature) -> {
                if (category != Biome.BiomeCategory.NETHER && category != Biome.BiomeCategory.THEEND && category != Biome.BiomeCategory.NONE) {
                    event.getGeneration().addFeature(GenerationStep.Decoration.LAKES, placedFeature);
                }
            });
        }

        if (HyperLightingConfig.genGlowingColoredWater.get()) {
            GLOWING_COLORED_WATER_GEN.forEach((color, placedFeature) -> {
                if (category != Biome.BiomeCategory.NETHER && category != Biome.BiomeCategory.THEEND && category != Biome.BiomeCategory.NONE) {
                    event.getGeneration().addFeature(GenerationStep.Decoration.LAKES, placedFeature);
                }
            });
        }
    }
    */
}

