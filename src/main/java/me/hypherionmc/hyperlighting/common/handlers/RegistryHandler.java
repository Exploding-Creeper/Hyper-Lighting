package me.hypherionmc.hyperlighting.common.handlers;

import me.hypherionmc.hyperlighting.HyperLightingFabric;
import me.hypherionmc.hyperlighting.api.DyeAble;
import me.hypherionmc.hyperlighting.api.ItemDyable;
import me.hypherionmc.hyperlighting.client.particles.ParticleClientRegistryHandler;
import me.hypherionmc.hyperlighting.client.renderers.CampFireRenderer;
import me.hypherionmc.hyperlighting.client.renderers.FogMachineRenderer;
import me.hypherionmc.hyperlighting.common.config.ConfigHandler;
import me.hypherionmc.hyperlighting.common.init.*;
import me.hypherionmc.hyperlighting.common.network.NetworkHandler;
import me.hypherionmc.hyperlighting.utils.CustomRenderType;
import me.hypherionmc.hyperlighting.utils.ModUtils;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class RegistryHandler {

    public static void init() {

        HyperLightingFabric.hyperLightingConfig = ConfigHandler.initConfig();

        HyperLightingFabric.logger.info("Registering Fluids...");
        HLFluids.registerWaterColors();

        HyperLightingFabric.logger.info("Registering Blocks...");
        new HLBlocks();

        HyperLightingFabric.logger.info("Registering Particles (Server)...");
        ParticleRegistryHandler.register();

        HyperLightingFabric.logger.info("Registering Items...");
        HLItems.ITEMS.size();
        HLItems.registerWaterBottles();

        HyperLightingFabric.logger.info("Registering Tile Entities...");
        HLBlockEntities.register();

        HyperLightingFabric.logger.info("Registering Sounds...");
        HLSounds.SABER_EQUIP.toString();

        HyperLightingFabric.logger.info("Registering ScreenHandlers...");
        HLScreenHandlers.registerServer();

        HyperLightingFabric.logger.info("Registering Server Packets...");
        NetworkHandler.registerServer();

        HyperLightingFabric.logger.info("Registering Worldgen...");
        HLWorldgen.register();
    }

    public static void initClient() {
        HyperLightingFabric.logger.info("Registering Particles (Client)...");
        ParticleClientRegistryHandler.registerClient();

        HyperLightingFabric.logger.info("Registering BESRs...");
        BlockEntityRendererRegistry.register(HLBlockEntities.TILE_CAMPFIRE, CampFireRenderer::new);
        BlockEntityRendererRegistry.register(HLBlockEntities.TILE_FOG_MACHINE, FogMachineRenderer::new);

        HyperLightingFabric.logger.info("Registering Screens...");
        HLScreenHandlers.registerClient();

        HyperLightingFabric.logger.info("Registering Fluid Renderers...");
        HLFluids.getFluidMap().forEach((color, coloredWaterEntry) -> {
            FluidRenderHandlerRegistry.INSTANCE.register(coloredWaterEntry.getSTILL(), coloredWaterEntry.getFLOWING(), new SimpleFluidRenderHandler(
                    new Identifier("minecraft:block/water_still"),
                    new Identifier("minecraft:block/water_flow"),
                    ModUtils.fluidColorFromDye(color)
            ));
            BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), coloredWaterEntry.getFLOWING(), coloredWaterEntry.getSTILL());
        });

        HLFluids.getGlowingFluidMap().forEach((color, coloredWaterEntry) -> {
            FluidRenderHandlerRegistry.INSTANCE.register(coloredWaterEntry.getSTILL(), coloredWaterEntry.getFLOWING(), new SimpleFluidRenderHandler(
                    new Identifier("minecraft:block/water_still"),
                    new Identifier("minecraft:block/water_flow"),
                    ModUtils.fluidColorFromDye(color)
            ));
            BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), coloredWaterEntry.getFLOWING(), coloredWaterEntry.getSTILL());
        });

        HLBlocks.BLOCKS.forEach(block -> {
            if (block instanceof CustomRenderType customRenderType) {
                BlockRenderLayerMap.INSTANCE.putBlock(block, customRenderType.getCustomRenderType());
            }
        });

        registerBlockColors();
        registerItemColors();

        HyperLightingFabric.logger.info("Registering Client Packets...");
        NetworkHandler.registerClient();
    }

    public static void registerBlockColors() {
        HyperLightingFabric.logger.info("Registering Block DyeColor handlers...");
        HLBlocks.BLOCKS.forEach(block -> {
            if (block instanceof DyeAble dyeAble) {
                ColorProviderRegistry.BLOCK.register(dyeAble.dyeHandler(), block);
            }
        });
    }

    public static void registerItemColors() {
        HyperLightingFabric.logger.info("Registering Item DyeColor handlers...");
        HLItems.ITEMS.forEach(item -> {
            if (item instanceof ItemDyable dyeAble) {
                ColorProviderRegistry.ITEM.register(dyeAble.dyeHandler(), item);
            }
        });
    }
}
