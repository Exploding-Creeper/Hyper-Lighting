package me.hypherionmc.hyperlighting.common.handlers;

import me.hypherionmc.hyperlighting.client.particles.ColoredWaterBubbleParticle;
import me.hypherionmc.hyperlighting.client.particles.ColoredWaterSplashParticle;
import me.hypherionmc.hyperlighting.client.particles.CustomFlameParticle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
