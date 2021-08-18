package me.hypherionmc.hyperlighting;

import me.hypherionmc.hyperlighting.client.events.ClientTickEvent;
import me.hypherionmc.hyperlighting.client.itemgroups.HLLightingTab;
import me.hypherionmc.hyperlighting.client.itemgroups.HLMachinesTab;
import me.hypherionmc.hyperlighting.common.config.HyperLightingConfig;
import me.hypherionmc.hyperlighting.common.handlers.RegistryHandler;
import me.hypherionmc.hyperlighting.util.ModUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ModConstants.MODID)
public class HyperLighting {

    public static final Logger logger = LogManager.getLogger(ModConstants.MODID);
    public static HLLightingTab mainTab = new HLLightingTab("hyperlighting");
    public static HLMachinesTab machinesTab = new HLMachinesTab("hyperlighting_machines");

    public HyperLighting() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupComplete);
        RegistryHandler.init();

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, HyperLightingConfig.spec);
    }

    private void setup(final FMLCommonSetupEvent event) {
        logger.info("Initializing Mod");
        String checkRGB = ModUtils.isRGBLibPresent() ? "RGBLib is installed. Colored lighting will be supported" : "RGBLib not found. Colored Lighting will not be supported";
        logger.info(checkRGB);
    }

    public void clientSetup(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new ClientTickEvent());
        RegistryHandler.initClient();
        RegistryHandler.registerBlockColors();
        RegistryHandler.registerItemColors();

        ClientTickEvent.registerKeybinds();

        /*if (ModUtils.isClothConfigPresent()) {
            logger.info("Adding Cloth Config support");
            ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, screen) -> ClothConfigGUI.openGUI(screen));
        }*/
    }

    public void setupComplete(final FMLLoadCompleteEvent event) {
        logger.info("Mod Setup Complete");
    }

}
