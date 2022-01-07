package me.hypherionmc.hyperlighting.common.handlers;

import me.hypherionmc.hyperlighting.HyperLighting;
import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.api.DyeAble;
import me.hypherionmc.hyperlighting.api.ItemDyable;
import me.hypherionmc.hyperlighting.client.gui.FogMachineGui;
import me.hypherionmc.hyperlighting.client.gui.GuiBatteryNeon;
import me.hypherionmc.hyperlighting.client.gui.GuiSwitchBoard;
import me.hypherionmc.hyperlighting.client.renderers.tile.TileCampFireRenderer;
import me.hypherionmc.hyperlighting.client.renderers.tile.TileFogMachineRenderer;
import me.hypherionmc.hyperlighting.common.init.*;
import me.hypherionmc.hyperlighting.common.integration.top.TOPIntegration;
import me.hypherionmc.hyperlighting.common.network.PacketHandler;
import me.hypherionmc.hyperlighting.util.CustomRenderType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class RegistryHandler {

    public static void init() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        HyperLighting.logger.info("Registering Blocks...");
        HLBlocks.BLOCKS.register(eventBus);

        HyperLighting.logger.info("Registering Items...");
        HLItems.ITEMS.register(eventBus);

        HyperLighting.logger.info("Registering Fluids...");
        HLFluids.registerWaterColors(eventBus);

        HyperLighting.logger.info("Registering Containers...");
        HLContainers.CONTAINERS.register(eventBus);

        ParticleRegistryHandler.registerParticles(eventBus);

        HyperLighting.logger.info("Registering Tile Entities...");
        HLTileEntities.TILES.register(eventBus);

        HyperLighting.logger.info("Registering Network Packets...");
        PacketHandler.registerMessages();

        HyperLighting.logger.info("Registering Sounds...");
        HLSounds.SOUNDS.register(eventBus);

        if (ModList.get().isLoaded(ModConstants.THE_ONE_PROBE)) {
            new TOPIntegration().setup();
        }
    }

    public static void initClient() {

        HyperLighting.logger.info("Registering TESRs...");
        BlockEntityRenderers.register(HLTileEntities.TILE_CAMPFIRE.get(), TileCampFireRenderer::new);
        BlockEntityRenderers.register(HLTileEntities.TILE_FOG_MACHINE.get(), TileFogMachineRenderer::new);

        HLBlocks.BLOCKS.getEntries().forEach(blk -> {
            if (blk.get() instanceof CustomRenderType) {
                ItemBlockRenderTypes.setRenderLayer(blk.get(), ((CustomRenderType) blk.get()).getRenderType());
            }
        });

        HLFluids.getFluidMap().forEach((dyeColor, entry) -> {
            ItemBlockRenderTypes.setRenderLayer(entry.getBLOCK().get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(entry.getSTILL(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(entry.getFLOWING(), RenderType.translucent());
        });

        HLFluids.getGlowingFluidMap().forEach((dyeColor, entry) -> {
            ItemBlockRenderTypes.setRenderLayer(entry.getBLOCK().get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(entry.getSTILL(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(entry.getFLOWING(), RenderType.translucent());
        });

        HyperLighting.logger.info("Registering Containers...");
        MenuScreens.register(HLContainers.BATTERY_NEON_CONTAINER.get(), GuiBatteryNeon::new);
        MenuScreens.register(HLContainers.SWITCHBOARD_CONTAINER.get(), GuiSwitchBoard::new);
        MenuScreens.register(HLContainers.FOG_MACHINE_CONTAINER.get(), FogMachineGui::new);

    }

    public static void registerBlockColors() {
        BlockColors colors = Minecraft.getInstance().getBlockColors();

        HyperLighting.logger.info("Registering DyeColor handlers...");
        HLBlocks.BLOCKS.getEntries().forEach(blockRegistryObject -> {
            if (blockRegistryObject.get() instanceof DyeAble) {
                colors.register(((DyeAble) blockRegistryObject.get()).dyeHandler(), blockRegistryObject.get());
            }
        });

    }

    public static void registerItemColors() {
        ItemColors itemColors = Minecraft.getInstance().getItemColors();

        HyperLighting.logger.info("Registering Item DyeColor handlers...");
        HLItems.ITEMS.getEntries().forEach(itemRegistryObject -> {
            if (itemRegistryObject.get() instanceof ItemDyable itemDyable) {
                itemColors.register(itemDyable.dyeHandler(), itemRegistryObject.get());
            }
        });

    }

    public static void commonSetup(FMLCommonSetupEvent event) {
        HyperLighting.logger.info("Registering Worldgen...");
        HLWorldGen.register();
    }
}
