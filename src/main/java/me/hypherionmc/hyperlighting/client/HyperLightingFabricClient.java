package me.hypherionmc.hyperlighting.client;

import me.hypherionmc.hyperlighting.common.handlers.RegistryHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class HyperLightingFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        RegistryHandler.initClient();
    }
}
