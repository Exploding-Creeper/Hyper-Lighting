package me.hypherionmc.hyperlighting.common.handlers;

import me.hypherionmc.hyperlighting.client.particles.CustomFlameParticle;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ParticleRegistryHandler {

    public static final DefaultParticleType CUSTOM_FLAME = FabricParticleTypes.simple();

    public static void register() {
        Registry.register(Registry.PARTICLE_TYPE, new Identifier("hyperlighting", "custom_flame"), CUSTOM_FLAME);
    }

    public static void registerClient() {
        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> {
            registry.register(new Identifier("hyperlighting", "particle/flame_base"));
        }));
        ParticleFactoryRegistry.getInstance().register(CUSTOM_FLAME, CustomFlameParticle.Factory::new);
    }

}
