package me.hypherionmc.hyperlighting.common.handlers;

import me.hypherionmc.hyperlighting.HyperLightingFabric;
import me.hypherionmc.hyperlighting.api.DyeAble;
import me.hypherionmc.hyperlighting.client.renderers.CampFireRenderer;
import me.hypherionmc.hyperlighting.common.config.ConfigHandler;
import me.hypherionmc.hyperlighting.common.init.*;
import me.hypherionmc.hyperlighting.common.items.BlockItemColor;
import me.hypherionmc.hyperlighting.common.network.NetworkHandler;
import me.hypherionmc.hyperlighting.utils.CustomRenderType;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

public class RegistryHandler {

    public static void init() {

        HyperLightingFabric.hyperLightingConfig = ConfigHandler.initConfig();

        HyperLightingFabric.logger.info("Registering Blocks...");
        HLBlocks.BLOCKS.size();

        HyperLightingFabric.logger.info("Registering Items...");
        HLItems.ITEMS.size();

        HyperLightingFabric.logger.info("Registering Particles (Server)...");
        ParticleRegistryHandler.register();

        HyperLightingFabric.logger.info("Registering Tile Entities...");
        HLBlockEntities.register();

        HyperLightingFabric.logger.info("Registering Sounds...");
        HLSounds.SABER_EQUIP.toString();

        HyperLightingFabric.logger.info("Registering ScreenHandlers...");
        HLScreenHandlers.registerServer();

        HyperLightingFabric.logger.info("Registering Server Packets...");
        NetworkHandler.registerServer();

    }

    public static void initClient() {
        HyperLightingFabric.logger.info("Registering Particles (Client)...");
        ParticleRegistryHandler.registerClient();

        HyperLightingFabric.logger.info("Registering BESRs...");
        BlockEntityRendererRegistry.register(HLBlockEntities.TILE_CAMPFIRE, CampFireRenderer::new);

        HyperLightingFabric.logger.info("Registering Screens...");
        HLScreenHandlers.registerClient();

        HLBlocks.BLOCKS.forEach(block -> {
            if (block instanceof CustomRenderType customRenderType) {
                BlockRenderLayerMap.INSTANCE.putBlock(block, customRenderType.getCustomRenderType());
            }
        });

        registerBlockColors();
        registerItemColors();
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
            if (item instanceof BlockItemColor dyeAble) {
                ColorProviderRegistry.ITEM.register(dyeAble.dyeHandler(), item);
            }
        });
    }
}
