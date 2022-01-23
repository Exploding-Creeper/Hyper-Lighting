package me.hypherionmc.hyperlighting.client.particles;

import me.hypherionmc.hyperlighting.common.handlers.ParticleRegistryHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

public class ParticleClientRegistryHandler {

    @Environment(value = EnvType.CLIENT)
    public static void registerClient() {
        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> {
            registry.register(new Identifier("hyperlighting", "particle/flame_base"));
            registry.register(new Identifier("hyperlighting", "particle/bubble_particle"));

            registry.register(new Identifier("hyperlighting", "particle/splash_0"));
            registry.register(new Identifier("hyperlighting", "particle/splash_1"));
            registry.register(new Identifier("hyperlighting", "particle/splash_2"));
            registry.register(new Identifier("hyperlighting", "particle/splash_3"));
        }));
        ParticleFactoryRegistry.getInstance().register(ParticleRegistryHandler.CUSTOM_FLAME, CustomFlameParticle.Factory::new);

        ParticleRegistryHandler.COLORED_WATER_BUBBLES.forEach((color, defaultParticleType) -> {
            float[] colors = color.getColorComponents();
            ParticleFactoryRegistry.getInstance().register(defaultParticleType, (factory) -> new ColoredWaterBubbleParticle.Factory(factory, colors[0], colors[1], colors[2]));
        });

        ParticleRegistryHandler.COLORED_WATER_SPLASH.forEach((color, defaultParticleType) -> {
            float[] colors = color.getColorComponents();
            ParticleFactoryRegistry.getInstance().register(defaultParticleType, (factory) -> new ColoredWaterSplashParticle.SplashFactory(factory, colors[0], colors[1], colors[2]));
        });

        ParticleRegistryHandler.FOG_MACHINE_PARTICLES.forEach((color, fog) -> {
            ParticleFactoryRegistry.getInstance().register(fog, pSprites -> new FogMachineSmoke.Provider(pSprites, color));
        });
    }

}
