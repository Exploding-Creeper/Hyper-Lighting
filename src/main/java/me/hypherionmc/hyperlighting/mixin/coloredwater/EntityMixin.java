package me.hypherionmc.hyperlighting.mixin.coloredwater;

import me.hypherionmc.hyperlighting.common.fluids.ColoredWater;
import me.hypherionmc.hyperlighting.common.handlers.ParticleRegistryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Entity.class)
public class EntityMixin {

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"), method = "onSwimmingStart")
    public ParticleEffect addWaterParticles(ParticleEffect effect) {
        Entity entity = (Entity) (Object) this;

        if (entity.world.isClient) {
            FluidState state = entity.world.getFluidState(entity.getBlockPos());

            if (state.getFluid() instanceof ColoredWater coloredWater) {
                if (effect.equals(ParticleTypes.BUBBLE)) {
                    return ParticleRegistryHandler.COLORED_WATER_BUBBLES.get(coloredWater.getColor());
                } else if (effect.equals(ParticleTypes.SPLASH)) {
                    return ParticleRegistryHandler.COLORED_WATER_SPLASH.get(coloredWater.getColor());
                }
            }
        }

        return effect;
    }
}
