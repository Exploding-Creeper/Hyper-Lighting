package me.hypherionmc.hyperlighting.mixin;

import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.init.HLSounds;
import me.hypherionmc.hyperlighting.common.network.NetworkHandler;
import me.hypherionmc.hyperlighting.utils.ModUtils;
import net.fabricmc.fabric.mixin.tool.attribute.LivingEntityMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    // TODO: FIND A BETTER WAY OF DOING THIS... Seriously

    @Shadow public ClientPlayerEntity player;

    @Inject(method = "doAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;swingHand(Lnet/minecraft/util/Hand;)V"))
    public void swingHand(CallbackInfo ci) {
        if (player.getStackInHand(player.getActiveHand()).getItem() == HLItems.LIGHT_SABER) {
            NetworkHandler.sendLightSaberPacket(player.getBlockPos());
        }
    }
}