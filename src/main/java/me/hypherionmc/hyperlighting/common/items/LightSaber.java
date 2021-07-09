package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.hyperlighting.HyperLighting;
import me.hypherionmc.hyperlighting.common.init.HLSounds;
import me.hypherionmc.hyperlighting.util.ModUtils;
import me.hypherionmc.rgblib.api.APIUtils;
import me.hypherionmc.rgblib.api.ColoredLightManager;
import me.hypherionmc.rgblib.api.RGBLight;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.SoundCategory;

public class LightSaber extends SwordItem {

    public LightSaber() {
        super(ItemTier.DIAMOND, 1, 1, new Properties().group(HyperLighting.mainTab).isImmuneToFire().rarity(Rarity.UNCOMMON).setNoRepair());

        if (ModUtils.isRGBLibPresent()) {
            ColoredLightManager.registerProvider(this, this::produceColoredLight);
        }

    }

    private RGBLight produceColoredLight(Entity entity, ItemStack stack) {
        return RGBLight.builder().pos(APIUtils.entityPos(entity)).color(DyeColor.LIGHT_BLUE.getColorValue(), false).radius(10).build();
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!target.isImmuneToFire()) {
            target.setFire(10);
            if (!attacker.world.isRemote) {
                attacker.world.playSound((PlayerEntity) null, attacker.getPosition(), HLSounds.SABER_HIT.get(), SoundCategory.VOICE, 0.4f, ModUtils.floatInRange(0.8f, 1.0f));
            }
            return true;
        }
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (!entity.world.isRemote) {
            entity.world.playSound((PlayerEntity) null, entity.getPosition(), HLSounds.SABER_USE.get(), SoundCategory.VOICE, 0.4f, ModUtils.floatInRange(0.8f, 1.0f));
        }
        return super.onEntitySwing(stack, entity);
    }



}
