package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.hyperlighting.HyperLightingFabric;
import me.hypherionmc.hyperlighting.common.init.HLSounds;
import me.hypherionmc.hyperlighting.utils.ModUtils;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Rarity;

public class LightSaber extends SwordItem {

    public LightSaber() {
        super(ToolMaterials.DIAMOND, 1, 1, new FabricItemSettings().group(HyperLightingFabric.mainTab).fireproof().rarity(Rarity.UNCOMMON));
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!target.isFireImmune()) {
            target.setOnFireFor(10);
            if (!attacker.world.isClient) {
                attacker.world.playSound(null, attacker.getBlockPos(), HLSounds.SABER_HIT, SoundCategory.VOICE, 0.4f, ModUtils.floatInRange(0.8f, 1.0f));
            }
            return true;
        }
        return super.postHit(stack, target, attacker);
    }
}
