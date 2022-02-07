package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.hyperlighting.HyperLightingFabric;
import me.hypherionmc.hyperlighting.api.ItemDyable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.PotionUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;

public class ColoredWaterBottle extends DyeItem implements ItemDyable {

    private static final int MAX_USE_TIME = 32;

    private final DyeColor color;
    private final boolean isGlowing;

    public ColoredWaterBottle(DyeColor color, boolean isGlowing) {
        super(color, new Item.Settings().maxCount(1).group(HyperLightingFabric.fluidsTab));
        this.color = color;
        this.isGlowing = isGlowing;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return isGlowing;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public ItemColorProvider dyeHandler() {
        return new ItemColorProvider() {
            @Override
            public int getColor(ItemStack stack, int tintIndex) {
                return color.getMapColor().color;
            }
        };
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        PlayerEntity playerEntity;
        PlayerEntity playerEntity2 = playerEntity = user instanceof PlayerEntity ? (PlayerEntity) user : null;
        if (playerEntity instanceof ServerPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity) playerEntity, stack);
        }
        if (!world.isClient) {
            List<StatusEffectInstance> list = PotionUtil.getPotionEffects(stack);
            for (StatusEffectInstance statusEffectInstance : list) {
                if (statusEffectInstance.getEffectType().isInstant()) {
                    statusEffectInstance.getEffectType().applyInstantEffect(playerEntity, playerEntity, user, statusEffectInstance.getAmplifier(), 1.0);
                    continue;
                }
                user.addStatusEffect(new StatusEffectInstance(statusEffectInstance));
            }
            if (stack.getItem() == this && isGlowing) {
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 3600));
            }
        }
        if (playerEntity != null) {
            playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
            if (!playerEntity.getAbilities().creativeMode) {
                stack.decrement(1);
            }
        }
        if (playerEntity == null || !playerEntity.getAbilities().creativeMode) {
            if (stack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }
            if (playerEntity != null) {
                playerEntity.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
            }
        }
        world.emitGameEvent(user, GameEvent.DRINKING_FINISH, user.getCameraBlockPos());
        return stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

}
