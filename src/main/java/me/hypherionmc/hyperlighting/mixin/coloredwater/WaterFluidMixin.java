package me.hypherionmc.hyperlighting.mixin.coloredwater;

import me.hypherionmc.hyperlighting.common.fluids.ColoredWater;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.WaterFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WaterFluid.class)
public class WaterFluidMixin {

    @Inject(at = @At("RETURN"), method = "isSame", cancellable = true)
    public void isSame(Fluid pFluid, CallbackInfoReturnable<Boolean> cir) {
        if (pFluid instanceof ColoredWater) {
            cir.setReturnValue(true);
        }
    }

}
