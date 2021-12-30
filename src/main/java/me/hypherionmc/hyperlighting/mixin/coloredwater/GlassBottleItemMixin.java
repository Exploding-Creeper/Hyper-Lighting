package me.hypherionmc.hyperlighting.mixin.coloredwater;

import me.hypherionmc.hyperlighting.common.fluids.ColoredWater;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GlassBottleItem.class)
public class GlassBottleItemMixin {

    @Inject(at = @At("HEAD"), method = "use", cancellable = true)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack itemStack = user.getStackInHand(hand);

        BlockHitResult areaEffectCloudEntity = GlassBottleItem.raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
        if (areaEffectCloudEntity.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = areaEffectCloudEntity.getBlockPos();
            if (world.getFluidState(blockPos).isIn(FluidTags.WATER) && world.getFluidState(blockPos).getFluid() instanceof ColoredWater water) {
                world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0f, 1.0f);
                world.emitGameEvent(user, GameEvent.FLUID_PICKUP, blockPos);
                cir.setReturnValue(TypedActionResult.success(ItemUsage.exchangeStack(itemStack, user, new ItemStack(HLItems.getWaterBottle(water.getColor(), water.isGlowing()))), world.isClient()));
            }
        }

    }
}
