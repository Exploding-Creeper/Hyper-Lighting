package me.hypherionmc.hyperlighting.common.handlers;

import me.hypherionmc.hyperlighting.HyperLighting;
import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.api.DyeAble;
import me.hypherionmc.hyperlighting.client.gui.GuiBatteryNeon;
import me.hypherionmc.hyperlighting.client.gui.GuiSwitchBoard;
import me.hypherionmc.hyperlighting.client.renderers.tile.TileCampFireRenderer;
import me.hypherionmc.hyperlighting.common.blocks.AdvancedTorchBlock;
import me.hypherionmc.hyperlighting.common.init.*;
import me.hypherionmc.hyperlighting.common.integration.top.TOPIntegration;
import me.hypherionmc.hyperlighting.common.items.BlockItemColor;
import me.hypherionmc.hyperlighting.common.network.PacketHandler;
import me.hypherionmc.hyperlighting.util.CustomRenderType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class RegistryHandler {

    public static void init() {
        HyperLighting.logger.info("Registering Blocks...");
        HLBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());

        HyperLighting.logger.info("Registering Items...");
        HLItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

        HyperLighting.logger.info("Registering Containers...");
        HLContainers.CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());

        ParticleRegistryHandler.PARTICLES.register(FMLJavaModLoadingContext.get().getModEventBus());

        HyperLighting.logger.info("Registering Tile Entities...");
        HLTileEntities.TILES.register(FMLJavaModLoadingContext.get().getModEventBus());

        HyperLighting.logger.info("Registering Network Packets...");
        PacketHandler.registerMessages();

        HyperLighting.logger.info("Registering Sounds");
        HLSounds.SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());

        if (ModList.get().isLoaded(ModConstants.THE_ONE_PROBE)) {
            new TOPIntegration().setup();
        }
    }

    public static void initClient() {

        HyperLighting.logger.info("Registering TESRs...");
        ClientRegistry.bindTileEntityRenderer(HLTileEntities.TILE_CAMPFIRE.get(), TileCampFireRenderer::new);

        HLBlocks.BLOCKS.getEntries().forEach(blk -> {
            if (blk.get() instanceof CustomRenderType) {
                RenderTypeLookup.setRenderLayer(blk.get(), ((CustomRenderType)blk.get()).getRenderType());
            }
        });

        HyperLighting.logger.info("Registering Containers...");
        ScreenManager.registerFactory(HLContainers.BATTERY_NEON_CONTAINER.get(), GuiBatteryNeon::new);
        ScreenManager.registerFactory(HLContainers.SWITCHBOARD_CONTAINER.get(), GuiSwitchBoard::new);

    }

    public static void registerBlockColors(BlockColors colors) {

        HyperLighting.logger.info("Registering DyeColor handlers");
        HLBlocks.BLOCKS.getEntries().forEach(blockRegistryObject -> {
            if (blockRegistryObject.get() instanceof DyeAble) {
                colors.register(((DyeAble)blockRegistryObject.get()).dyeHandler(), blockRegistryObject.get());
            }
        });

    }

    public static void registerItemColors(ItemColors itemColors) {

        HyperLighting.logger.info("Registering Item DyeColor handlers");
        HLItems.ITEMS.getEntries().forEach(itemRegistryObject -> {
            if (itemRegistryObject.get() instanceof BlockItemColor) {
                itemColors.register((IItemColor) itemRegistryObject.get(), itemRegistryObject.get());
            }
        });

    }
}
