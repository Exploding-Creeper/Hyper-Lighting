package me.hypherionmc.hyperlighting.mixin.coloredwater;

import me.hypherionmc.hyperlighting.common.fluids.ColoredWater;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.WaterFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WaterFluid.class)
public class WaterFluidMixin {

    @Inject(at = @At("RETURN"), method = "matchesType", cancellable = true)
    public void matchesType(Fluid fluid, CallbackInfoReturnable<Boolean> cir) {
        if (fluid instanceof ColoredWater) {
            cir.setReturnValue(true);
        }
    }

}
