package me.hypherionmc.hyperlighting.common.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class CampFireUnderwater extends CampFireBlock {

    public CampFireUnderwater(String name, DyeColor color, CreativeModeTab group) {
        super(name, color, group);
    }

    @Override
    public boolean canBeLit(BlockState state, Level world, BlockPos pos) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("tooltip.camp_fire_soul"));
        tooltip.add(new TranslatableComponent("tooltip.camp_fire_soul_line1"));
        tooltip.add(new TextComponent(ChatFormatting.YELLOW + "Dyable"));
        tooltip.add(new TextComponent(ChatFormatting.BLUE + "Colored Lighting Supported"));
    }
}
