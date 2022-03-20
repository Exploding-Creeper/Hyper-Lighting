package me.hypherionmc.hyperlighting.common.handlers;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;

public class ParticleRegistryHandler {

    public static final HashMap<DyeColor, DefaultParticleType> COLORED_WATER_BUBBLES = new HashMap<>();
    public static final HashMap<DyeColor, DefaultParticleType> COLORED_WATER_SPLASH = new HashMap<>();
    public static final HashMap<DyeColor, DefaultParticleType> FOG_MACHINE_PARTICLES = new HashMap<>();

    public static final DefaultParticleType CUSTOM_FLAME = FabricParticleTypes.simple();
    public static final DefaultParticleType CANDLE_FLAME = FabricParticleTypes.simple();

    public static void register() {
        Registry.register(Registry.PARTICLE_TYPE, new Identifier("hyperlighting", "custom_flame"), CUSTOM_FLAME);
        Registry.register(Registry.PARTICLE_TYPE, new Identifier("hyperlighting", "candle_flame"), CANDLE_FLAME);

        registerWaterParticles();
    }

    private static void registerWaterParticles() {
        for (DyeColor color : DyeColor.values()) {
            COLORED_WATER_BUBBLES.put(color, registerParticle(color.name().toLowerCase() + "_bubble", FabricParticleTypes.simple()));
            COLORED_WATER_SPLASH.put(color, registerParticle(color.name().toLowerCase() + "_splash", FabricParticleTypes.simple()));

            DefaultParticleType COLORED_FOG = registerParticle(color.getName().toLowerCase() + "_fog", FabricParticleTypes.simple());
            FOG_MACHINE_PARTICLES.put(color, COLORED_FOG);
        }
    }

    private static DefaultParticleType registerParticle(String name, DefaultParticleType type) {
        return Registry.register(Registry.PARTICLE_TYPE, new Identifier("hyperlighting", name), type);
    }
}
