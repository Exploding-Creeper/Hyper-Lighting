package me.hypherionmc.hyperlighting.mixin.coloredwater;

import me.hypherionmc.hyperlighting.common.fluids.ColoredWater;
import me.hypherionmc.hyperlighting.common.handlers.ParticleRegistryHandler;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Entity.class)
public class EntityMixin {

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"), method = "doWaterSplashEffect")
    public ParticleOptions addColoredWaterParticles(ParticleOptions effect) {
        Entity entity = (Entity) (Object) this;

        if (entity.level.isClientSide()) {
            FluidState fluidState = entity.level.getFluidState(entity.getOnPos());

            if (fluidState.getType() instanceof ColoredWater coloredWater) {
                if (effect.equals(ParticleTypes.BUBBLE)) {
                    return ParticleRegistryHandler.COLORED_WATER_BUBBLES.get(coloredWater.getColor()).get();
                } else if (effect.equals(ParticleTypes.SPLASH)) {
                    return ParticleRegistryHandler.COLORED_WATER_SPLASH.get(coloredWater.getColor()).get();
                }
            }
        }

        return effect;
    }
}
