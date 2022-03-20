package me.hypherionmc.hyperlighting.mixin;

import me.hypherionmc.hyperlighting.HyperLightingFabric;
import me.hypherionmc.hyperlighting.common.network.NetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class MixinServerWorld {

    @Inject(at = @At("HEAD"), method = "addPlayer")
    public void onAddPlayer(ServerPlayerEntity entity, CallbackInfo ci) {
        HyperLightingFabric.logger.info("Sending Config Packets to " + entity.getDisplayName().getString());
        NetworkHandler.sendConfigPacket(entity);
    }
}
