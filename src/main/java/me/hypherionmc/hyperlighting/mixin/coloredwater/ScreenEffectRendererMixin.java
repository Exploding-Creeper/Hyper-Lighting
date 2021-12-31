package me.hypherionmc.hyperlighting.mixin.coloredwater;

import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.common.fluids.ColoredWater;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

    @ModifyArg(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V"), method = "renderWater")
    private static ResourceLocation setShaderTexture(ResourceLocation texture) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (player != null) {
            FluidState fluidState = player.level.getFluidState(player.getOnPos());

            if (fluidState.getType() instanceof ColoredWater coloredWater) {
                texture = new ResourceLocation(ModConstants.MODID, "textures/overlay/" + coloredWater.getColor().getName().toLowerCase() + "_overlay.png");
            }
        }

        return texture;
    }

}
