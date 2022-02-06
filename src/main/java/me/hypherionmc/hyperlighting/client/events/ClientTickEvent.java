package me.hypherionmc.hyperlighting.client.events;

import me.hypherionmc.hyperlighting.client.gui.ClothConfigGUI;
import me.hypherionmc.hyperlighting.util.ModUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class ClientTickEvent {

    private static KeyMapping configToggle;
    private long nextKeyTriggerTime;

    public static void registerKeybinds() {
        configToggle = new KeyMapping("Hyper Lighting Config", GLFW.GLFW_KEY_HOME, "key.categories.gameplay");
        ClientRegistry.registerKeyBinding(configToggle);
    }

    @SubscribeEvent
    public void clientTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (Minecraft.getInstance().level != null) {
                if (Minecraft.getInstance().screen == null && configToggle.consumeClick() && System.currentTimeMillis() >= nextKeyTriggerTime) {
                    nextKeyTriggerTime = System.currentTimeMillis() + 1000L;
                    if (ModUtils.isClothConfigPresent()) {
                        Minecraft.getInstance().setScreen(ClothConfigGUI.openGUI(null));
                    } else {
                        Minecraft.getInstance().player.displayClientMessage(new TextComponent("To access the in-game config, please install Cloth Config"), false);
                    }
                }
            }
        }
    }

}
