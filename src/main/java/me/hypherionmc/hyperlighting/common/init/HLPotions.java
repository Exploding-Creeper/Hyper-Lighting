package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.hyperlighting.ModConstants;
import net.minecraft.potion.Potion;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class HLPotions {

    public static final List<Potion> WATER_POTIONS = new ArrayList<>();
    public static final List<Potion> GLOWING_WATER_POTIONS = new ArrayList<>();

    public static void registerWaterPotions() {
        /*HLItems.WATER_BOTTLES.forEach((color, item) -> {
            WATER_POTIONS.add(register(color.getName().toLowerCase() + "_water_potion", new Potion()));
        });

        HLItems.GLOWING_WATER_BOTTLES.forEach((color, item) -> {
            GLOWING_WATER_POTIONS.add(register(color.getName().toLowerCase() + "_glowing_water_potion", new Potion()));
        });*/
    }

    private static Potion register(String name, Potion potion) {
        return Registry.register(Registry.POTION, new Identifier(ModConstants.MOD_ID, name), potion);
    }

}
