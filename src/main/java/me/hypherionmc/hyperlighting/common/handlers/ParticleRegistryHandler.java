package me.hypherionmc.hyperlighting.common.handlers;

import me.hypherionmc.hyperlighting.client.particles.ColoredWaterBubbleParticle;
import me.hypherionmc.hyperlighting.client.particles.ColoredWaterSplashParticle;
import me.hypherionmc.hyperlighting.client.particles.CustomFlameParticle;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;

public class ParticleRegistryHandler {

    public static final HashMap<DyeColor, DefaultParticleType> COLORED_WATER_BUBBLES = new HashMap<>();
    public static final HashMap<DyeColor, DefaultParticleType> COLORED_WATER_SPLASH = new HashMap<>();

    public static final DefaultParticleType CUSTOM_FLAME = FabricParticleTypes.simple();

    public static void register() {
        Registry.register(Registry.PARTICLE_TYPE, new Identifier("hyperlighting", "custom_flame"), CUSTOM_FLAME);

        registerWaterParticles();
    }

    public static void registerClient() {
        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> {
            registry.register(new Identifier("hyperlighting", "particle/flame_base"));
            registry.register(new Identifier("hyperlighting", "particle/bubble_particle"));

            registry.register(new Identifier("hyperlighting", "particle/splash_0"));
            registry.register(new Identifier("hyperlighting", "particle/splash_1"));
            registry.register(new Identifier("hyperlighting", "particle/splash_2"));
            registry.register(new Identifier("hyperlighting", "particle/splash_3"));
        }));
        ParticleFactoryRegistry.getInstance().register(CUSTOM_FLAME, CustomFlameParticle.Factory::new);

        COLORED_WATER_BUBBLES.forEach((color, defaultParticleType) -> {
            float[] colors = color.getColorComponents();
            ParticleFactoryRegistry.getInstance().register(defaultParticleType, (factory) -> new ColoredWaterBubbleParticle.Factory(factory, colors[0], colors[1], colors[2]));
        });

        COLORED_WATER_SPLASH.forEach((color, defaultParticleType) -> {
            float[] colors = color.getColorComponents();
            ParticleFactoryRegistry.getInstance().register(defaultParticleType, (factory) -> new ColoredWaterSplashParticle.SplashFactory(factory, colors[0], colors[1], colors[2]));
        });
    }

    private static void registerWaterParticles() {
        for (DyeColor color : DyeColor.values()) {
            COLORED_WATER_BUBBLES.put(color, registerParticle(color.name().toLowerCase() + "_bubble", FabricParticleTypes.simple()));
            COLORED_WATER_SPLASH.put(color, registerParticle(color.name().toLowerCase() + "_splash", FabricParticleTypes.simple()));
        }
    }

    private static DefaultParticleType registerParticle(String name, DefaultParticleType type) {
        return Registry.register(Registry.PARTICLE_TYPE, new Identifier("hyperlighting", name), type);
    }
}
