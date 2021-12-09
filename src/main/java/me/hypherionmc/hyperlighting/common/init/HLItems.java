package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.common.items.LightSaber;
import me.hypherionmc.hyperlighting.common.items.LighterTool;
import me.hypherionmc.hyperlighting.common.items.WirelessPowerCard;
import me.hypherionmc.hyperlighting.common.items.WirelessSwitchCard;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class HLItems {

    public static final List<Item> ITEMS = new ArrayList<>();

    public static final Item LIGHTER_TOOL = register("lighter_tool", new LighterTool());
    public static final Item WIRELESS_POWERCARD = register("wireless_powercard", new WirelessPowerCard());
    public static final Item WIRELESS_SWITCH_POWERCARD = register("wireless_switch_card", new WirelessSwitchCard());
    public static final Item LIGHT_SABER = register("light_saber", new LightSaber());

    public static Item register(String name, Item item) {
        Item itm = Registry.register(Registry.ITEM, new Identifier(ModConstants.MOD_ID, name), item);
        ITEMS.add(itm);
        return itm;
    }
}
