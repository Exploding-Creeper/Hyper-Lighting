package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.hyperlighting.HyperLighting;
import me.hypherionmc.hyperlighting.common.init.HLSounds;
import me.hypherionmc.hyperlighting.util.ModUtils;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;

public class LightSaber extends SwordItem {

    public LightSaber() {
        super(Tiers.DIAMOND, 1, 1, new Properties().tab(HyperLighting.mainTab).fireResistant().rarity(Rarity.UNCOMMON).setNoRepair());

        if (ModUtils.isRGBLibPresent()) {
            //ColoredLightManager.registerProvider(this, this::produceColoredLight);
        }

    }

    /*private RGBLight produceColoredLight(Entity entity, ItemStack stack) {
        //return RGBLight.builder().pos(APIUtils.entityPos(entity)).color(DyeColor.LIGHT_BLUE.getMaterialColor().col, false).radius(10).build();
        return null;
    }*/

    @Override
    public boolean canBeDepleted() {
        return false;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!target.fireImmune()) {
            target.setSecondsOnFire(10);
            if (!attacker.level.isClientSide) {
                attacker.level.playSound((Player) null, attacker.blockPosition(), HLSounds.SABER_HIT.get(), SoundSource.VOICE, 0.4f, ModUtils.floatInRange(0.8f, 1.0f));
            }
            return true;
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (!entity.level.isClientSide) {
            entity.level.playSound((Player) null, entity.blockPosition(), HLSounds.SABER_USE.get(), SoundSource.VOICE, 0.4f, ModUtils.floatInRange(0.8f, 1.0f));
        }
        return super.onEntitySwing(stack, entity);
    }



}
