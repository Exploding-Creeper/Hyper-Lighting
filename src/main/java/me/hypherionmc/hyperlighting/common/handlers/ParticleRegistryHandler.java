package me.hypherionmc.hyperlighting.common.handlers;

import me.hypherionmc.hyperlighting.client.particles.FlameParticle;
import me.hypherionmc.hyperlighting.HyperLighting;
import me.hypherionmc.hyperlighting.ModConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = ModConstants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleRegistryHandler {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ModConstants.MODID);

    public static final RegistryObject<BasicParticleType> CUSTOM_FLAME = PARTICLES.register("custom_flame", () -> new BasicParticleType(false));

    @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ModConstants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class FactoryHandler {

        @SubscribeEvent
        public static void registerFactories(ParticleFactoryRegisterEvent event) {
            HyperLighting.logger.info("Registering particles...");
            Minecraft.getInstance().particles.registerFactory(ParticleRegistryHandler.CUSTOM_FLAME.get(), FlameParticle.FACTORY::new);
        }

    }
}
