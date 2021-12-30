package me.hypherionmc.hyperlighting.mixin.coloredwater;

import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.common.fluids.ColoredWater;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {

    @ModifyArg(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V"), method = "renderUnderwaterOverlay")
    private static Identifier setUnderwaterTexture(Identifier texture) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;

        if (player != null) {
            FluidState fluidState = player.world.getFluidState(player.getBlockPos());

            if (fluidState.getFluid() instanceof ColoredWater coloredWater) {
                texture = new Identifier(ModConstants.MOD_ID, "textures/overlay/" + coloredWater.getColor().getName().toLowerCase() + "_overlay.png");
            }
        }

        return texture;
    }
}
