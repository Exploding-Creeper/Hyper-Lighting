package me.hypherionmc.hyperlighting.client.events;

import me.hypherionmc.hyperlighting.common.config.ClothConfigGUI;
import me.hypherionmc.hyperlighting.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class ClientTickEvent {

    private static KeyBinding configToggle;
    private long nextKeyTriggerTime;

    public static void registerKeybinds() {
        configToggle = new KeyBinding("Hyper Lighting Config", GLFW.GLFW_KEY_HOME, "key.categories.gameplay");
        ClientRegistry.registerKeyBinding(configToggle);
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
