package me.hypherionmc.hyperlighting.common.handlers;

import me.hypherionmc.hyperlighting.HyperLighting;
import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.client.particles.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;

@Mod.EventBusSubscriber(modid = ModConstants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleRegistryHandler {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ModConstants.MODID);
    public static final HashMap<DyeColor, RegistryObject<SimpleParticleType>> COLORED_WATER_BUBBLES = new HashMap<>();
    public static final HashMap<DyeColor, RegistryObject<SimpleParticleType>> COLORED_WATER_SPLASH = new HashMap<>();
    public static final HashMap<DyeColor, RegistryObject<SimpleParticleType>> FOG_MACHINE_PARTICLES = new HashMap<>();

    public static RegistryObject<SimpleParticleType> CUSTOM_FLAME;
    public static RegistryObject<SimpleParticleType> CANDLE_FLAME;

    public static void registerParticles(IEventBus bus) {
        CUSTOM_FLAME = PARTICLES.register("custom_flame", () -> new SimpleParticleType(false));
        CANDLE_FLAME = PARTICLES.register("candle_flame", () -> new SimpleParticleType(false));
        for (DyeColor color : DyeColor.values()) {
            RegistryObject<SimpleParticleType> BUBBLE = PARTICLES.register(color.name().toLowerCase() + "_bubble", () -> new SimpleParticleType(false));
            RegistryObject<SimpleParticleType> SPLASH = PARTICLES.register(color.name().toLowerCase() + "_splash", () -> new SimpleParticleType(false));
            COLORED_WATER_BUBBLES.put(color, BUBBLE);
            COLORED_WATER_SPLASH.put(color, SPLASH);

            RegistryObject<SimpleParticleType> COLORED_FOG = PARTICLES.register(color.getName().toLowerCase() + "_fog", () -> new SimpleParticleType(false));
            FOG_MACHINE_PARTICLES.put(color, COLORED_FOG);
        }

        PARTICLES.register(bus);
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ModConstants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class FactoryHandler {

        @SubscribeEvent
        public static void registerFactories(ParticleFactoryRegisterEvent event) {
            HyperLighting.logger.info("Registering particles...");
            registerColoredWaterParticles();
            Minecraft.getInstance().particleEngine.register(ParticleRegistryHandler.CUSTOM_FLAME.get(), FlameParticle.FACTORY::new);
            Minecraft.getInstance().particleEngine.register(ParticleRegistryHandler.CANDLE_FLAME.get(), CandleFlame.FACTORY::new);
        }

        private static void registerColoredWaterParticles() {
            COLORED_WATER_BUBBLES.forEach((color, bubble) -> {
                float[] colorVal = color.getTextureDiffuseColors();
                Minecraft.getInstance().particleEngine.register(bubble.get(), pSprites -> new ColoredWaterBubbleParticle.Provider(pSprites, colorVal[0], colorVal[1], colorVal[2]));
            });

            COLORED_WATER_SPLASH.forEach((color, splash) -> {
                float[] colorVal = color.getTextureDiffuseColors();
                Minecraft.getInstance().particleEngine.register(splash.get(), pSprites -> new ColoredWaterSplashParticle.Provider(pSprites, colorVal[0], colorVal[1], colorVal[2]));
            });

            FOG_MACHINE_PARTICLES.forEach((color, fog) -> {
                Minecraft.getInstance().particleEngine.register(fog.get(), pSprites -> new FogMachineSmoke.Provider(pSprites, color));
            });
        }
    }
}
