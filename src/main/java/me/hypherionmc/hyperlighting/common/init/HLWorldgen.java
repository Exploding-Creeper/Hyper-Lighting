package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.hyperlighting.HyperLightingFabric;
import me.hypherionmc.hyperlighting.ModConstants;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.block.Blocks;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.BiomePlacementModifier;
import net.minecraft.world.gen.decorator.RarityFilterPlacementModifier;
import net.minecraft.world.gen.decorator.SquarePlacementModifier;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import java.util.HashMap;

public class HLWorldgen {

    private static final HashMap<DyeColor, PlacedFeature> COLORED_WATER_GEN = new HashMap<>();
    private static final HashMap<DyeColor, PlacedFeature> GLOWING_COLORED_WATER_GEN = new HashMap<>();

    public static void register() {
        HLFluids.COLORED_WATER.forEach((color, coloredWaterEntry) -> {
            ConfiguredFeature<?, ?> COLORED_WATER_CF = Feature.LAKE.configure(new LakeFeature.Config(BlockStateProvider.of(coloredWaterEntry.getBLOCK().getDefaultState()), BlockStateProvider.of(Blocks.DIRT)));
            PlacedFeature COLORED_WATER_PF = COLORED_WATER_CF.withPlacement(RarityFilterPlacementModifier.of(70 * (color.getId() + 1)), SquarePlacementModifier.of(), PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of());

            Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(ModConstants.MOD_ID, color.getName().toLowerCase() + "_colored_water_lake"), COLORED_WATER_CF);
            Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier(ModConstants.MOD_ID, color.getName().toLowerCase() + "_colored_water_lake"), COLORED_WATER_PF);
            COLORED_WATER_GEN.put(color, COLORED_WATER_PF);
        });

        HLFluids.GLOWING_COLORED_WATER.forEach((color, coloredWaterEntry) -> {
            ConfiguredFeature<?, ?> COLORED_WATER_CF = Feature.LAKE.configure(new LakeFeature.Config(BlockStateProvider.of(coloredWaterEntry.getBLOCK().getDefaultState()), BlockStateProvider.of(Blocks.DIRT)));
            PlacedFeature COLORED_WATER_PF = COLORED_WATER_CF.withPlacement(RarityFilterPlacementModifier.of(90 * (color.getId() + 1)), SquarePlacementModifier.of(), PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of());

            Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(ModConstants.MOD_ID, color.getName().toLowerCase() + "_colored_glowing_water_lake"), COLORED_WATER_CF);
            Registry.register(BuiltinRegistries.PLACED_FEATURE, new Identifier(ModConstants.MOD_ID, color.getName().toLowerCase() + "_colored_glowing_water_lake"), COLORED_WATER_PF);
            GLOWING_COLORED_WATER_GEN.put(color, COLORED_WATER_PF);
        });

        addWaterWorldgen();
    }

    private static void addWaterWorldgen() {
        if (HyperLightingFabric.hyperLightingConfig.genColoredLakes) {
            COLORED_WATER_GEN.forEach((color, placedFeature) -> {
                BiomeModifications.create(new Identifier(ModConstants.MOD_ID, "add_" + color.getName().toLowerCase() + "_colored_water")).add(
                        ModificationPhase.ADDITIONS,
                        (context) -> {
                            Biome.Category category = context.getBiome().getCategory();
                            return category != Biome.Category.NETHER && category != Biome.Category.THEEND && category != Biome.Category.NONE;
                        },
                        (context) -> context.getGenerationSettings().addBuiltInFeature(GenerationStep.Feature.LAKES, placedFeature)
                );
            });
        }

        if (HyperLightingFabric.hyperLightingConfig.genColoredGlowingLakes) {
            GLOWING_COLORED_WATER_GEN.forEach((color, placedFeature) -> {
                BiomeModifications.create(new Identifier(ModConstants.MOD_ID, "add_" + color.getName().toLowerCase() + "_glowing_colored_water")).add(
                        ModificationPhase.ADDITIONS,
                        (context) -> {
                            Biome.Category category = context.getBiome().getCategory();
                            return category != Biome.Category.NETHER && category != Biome.Category.THEEND && category != Biome.Category.NONE;
                        },
                        (context) -> context.getGenerationSettings().addBuiltInFeature(GenerationStep.Feature.LAKES, placedFeature)
                );
            });
        }
    }

}
