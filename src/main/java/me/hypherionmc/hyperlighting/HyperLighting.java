package me.hypherionmc.hyperlighting;

import me.hypherionmc.hyperlighting.client.itemgroups.HLLightingTab;
import me.hypherionmc.hyperlighting.client.itemgroups.HLMachinesTab;
import me.hypherionmc.hyperlighting.common.config.ClothConfigGUI;
import me.hypherionmc.hyperlighting.common.config.HyperLightingConfig;
import me.hypherionmc.hyperlighting.common.handlers.RegistryHandler;
import me.hypherionmc.hyperlighting.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

@Mod(ModConstants.MODID)
public class HyperLighting {

    public static final Logger logger = LogManager.getLogger(ModConstants.MODID);
    public static HLLightingTab mainTab = new HLLightingTab("hyperlighting");
    public static HLMachinesTab machinesTab = new HLMachinesTab("hyperlighting_machines");

    private KeyBinding configToggle;
    private long nextKeyTriggerTime;

    public HyperLighting() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupComplete);
        RegistryHandler.init();
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, HyperLightingConfig.spec);
    }

    private void setup(final FMLCommonSetupEvent event) {
        logger.info("Initializing Mod");
        String checkRGB = ModUtils.isRGBLibPresent() ? "RGBLib is installed. Colored lighting will be supported" : "RGBLib not found. Colored Lighting will not be supported";
        logger.info(checkRGB);
    }

    public void clientSetup(final FMLClientSetupEvent event) {
        RegistryHandler.initClient();
        RegistryHandler.registerBlockColors(Minecraft.getInstance().getBlockColors());
        RegistryHandler.registerItemColors(Minecraft.getInstance().getItemColors());

        configToggle = new KeyBinding("Hyper Lighting Config", GLFW.GLFW_KEY_HOME, "key.categories.gameplay");
        ClientRegistry.registerKeyBinding(configToggle);

        if (ModUtils.isClothConfigPresent()) {
            logger.info("Adding Cloth Config support");
            ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, screen) -> ClothConfigGUI.openGUI(screen));
        }
    }

    public void setupComplete(final FMLLoadCompleteEvent event) {
        logger.info("Mod Setup Complete");
    }

    @SubscribeEvent
    public void clientTick(TickEvent.PlayerTickEvent event) {

        if (event.phase == TickEvent.Phase.START) {
            if (Minecraft.getInstance().world != null) {
                if (Minecraft.getInstance().currentScreen == null && configToggle.isPressed() && System.currentTimeMillis() >= nextKeyTriggerTime) {
                    nextKeyTriggerTime = System.currentTimeMillis() + 1000L;
                    if (ModUtils.isClothConfigPresent()) {
                        Minecraft.getInstance().displayGuiScreen(ClothConfigGUI.openGUI(null));
                    } else {
                        Minecraft.getInstance().player.sendStatusMessage(new StringTextComponent("To access the in-game config, please install Cloth Config. JSON Lights will still be reloaded"), false);
                    }
                }
            }
        }

    }

}
