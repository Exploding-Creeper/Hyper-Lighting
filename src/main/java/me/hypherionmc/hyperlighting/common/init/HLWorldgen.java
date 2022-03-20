package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.hyperlighting.HyperLightingFabric;
import me.hypherionmc.hyperlighting.ModConstants;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.block.Blocks;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.BiomePlacementModifier;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier;
import net.minecraft.world.gen.placementmodifier.SquarePlacementModifier;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import java.util.HashMap;

public class HLWorldgen {

    private static final HashMap<DyeColor, RegistryEntry<PlacedFeature>> COLORED_WATER_GEN = new HashMap<>();
    private static final HashMap<DyeColor, RegistryEntry<PlacedFeature>> GLOWING_COLORED_WATER_GEN = new HashMap<>();

    public static void register() {
        HLFluids.COLORED_WATER.forEach((color, coloredWaterEntry) -> {
            RegistryEntry<ConfiguredFeature<LakeFeature.Config, ?>> COLORED_WATER_CF = ConfiguredFeatures.register(new Identifier(ModConstants.MOD_ID, color.getName().toLowerCase() + "_colored_water_lake").toString(), Feature.LAKE, new LakeFeature.Config(BlockStateProvider.of(coloredWaterEntry.getBLOCK().getDefaultState()), BlockStateProvider.of(Blocks.DIRT)));
            RegistryEntry<PlacedFeature> COLORED_WATER_PF = PlacedFeatures.register(new Identifier(ModConstants.MOD_ID, color.getName().toLowerCase() + "_colored_water_lake").toString(), COLORED_WATER_CF, RarityFilterPlacementModifier.of(70 * (color.getId() + 1)), SquarePlacementModifier.of(), PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of());
            COLORED_WATER_GEN.put(color, COLORED_WATER_PF);
        });

        HLFluids.GLOWING_COLORED_WATER.forEach((color, coloredWaterEntry) -> {
            RegistryEntry<ConfiguredFeature<LakeFeature.Config, ?>> COLORED_WATER_CF = ConfiguredFeatures.register(new Identifier(ModConstants.MOD_ID, color.getName().toLowerCase() + "_colored_glowing_water_lake").toString(), Feature.LAKE, new LakeFeature.Config(BlockStateProvider.of(coloredWaterEntry.getBLOCK().getDefaultState()), BlockStateProvider.of(Blocks.DIRT)));
            RegistryEntry<PlacedFeature> COLORED_WATER_PF = PlacedFeatures.register(new Identifier(ModConstants.MOD_ID, color.getName().toLowerCase() + "_colored_glowing_water_lake").toString(), COLORED_WATER_CF, RarityFilterPlacementModifier.of(70 * (color.getId() + 1)), SquarePlacementModifier.of(), PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP, BiomePlacementModifier.of());
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
                            Biome.Category category = Biome.getCategory(context.getBiomeRegistryEntry());
                            return category != Biome.Category.NETHER && category != Biome.Category.THEEND && category != Biome.Category.NONE;
                        },
                        (context) -> context.getGenerationSettings().addBuiltInFeature(GenerationStep.Feature.LAKES, placedFeature.value())
                );
            });
        }

        if (HyperLightingFabric.hyperLightingConfig.genColoredGlowingLakes) {
            GLOWING_COLORED_WATER_GEN.forEach((color, placedFeature) -> {
                BiomeModifications.create(new Identifier(ModConstants.MOD_ID, "add_" + color.getName().toLowerCase() + "_glowing_colored_water")).add(
                        ModificationPhase.ADDITIONS,
                        (context) -> {
                            Biome.Category category = Biome.getCategory(context.getBiomeRegistryEntry());
                            return category != Biome.Category.NETHER && category != Biome.Category.THEEND && category != Biome.Category.NONE;
                        },
                        (context) -> context.getGenerationSettings().addBuiltInFeature(GenerationStep.Feature.LAKES, placedFeature.value())
                );
            });
        }
    }

}
