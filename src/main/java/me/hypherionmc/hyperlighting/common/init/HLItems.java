package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.common.items.*;
import net.minecraft.item.Item;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HLItems {

    public static final List<Item> ITEMS = new ArrayList<>();
    public static final HashMap<DyeColor, Item> WATER_BOTTLES = new HashMap<>();
    public static final HashMap<DyeColor, Item> GLOWING_WATER_BOTTLES = new HashMap<>();

    public static final Item LIGHTER_TOOL = register("lighter_tool", new LighterTool());
    public static final Item WIRELESS_POWERCARD = register("wireless_powercard", new WirelessPowerCard());
    public static final Item WIRELESS_SWITCH_CARD = register("wireless_switch_card", new WirelessSwitchCard());
    public static final Item LIGHT_SABER = register("light_saber", new LightSaber());
    public static final Item FOG_REMOTE = register("fog_remote", new FogMachineRemote());

    public static Item register(String name, Item item) {
        Item itm = Registry.register(Registry.ITEM, new Identifier(ModConstants.MOD_ID, name), item);
        ITEMS.add(itm);
        return itm;
    }

    public static void registerWaterBottles() {
        for (DyeColor color : DyeColor.values()) {
            WATER_BOTTLES.put(color, register(color.getName().toLowerCase() + "_water_bottle", new ColoredWaterBottle(color, false)));
            GLOWING_WATER_BOTTLES.put(color, register(color.getName().toLowerCase() + "_glowing_water_bottle", new ColoredWaterBottle(color, true)));
        }
    }

    public static Item getWaterBottle(DyeColor color, boolean isGlowing) {
        return isGlowing ? GLOWING_WATER_BOTTLES.get(color) : WATER_BOTTLES.get(color);
    }

}
