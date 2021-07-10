package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.common.items.*;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class HLItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModConstants.MODID);

    public static final RegistryObject<Item> LIGHTER_TOOL = ITEMS.register("lighter_tool", LighterTool::new);
    public static final RegistryObject<Item> WIRELESS_POWERCARD = ITEMS.register("wireless_powercard", WirelessPowerCard::new);
    public static final RegistryObject<Item> WIRELESS_SWITCH_POWERCARD = ITEMS.register("wireless_switch_card", WirelessSwitchCard::new);
    public static final RegistryObject<Item> BATTERY = ITEMS.register("battery", () -> new Battery(500, 100, 150));

    public static final RegistryObject<Item> LIGHT_SABER = ITEMS.register("light_saber", LightSaber::new);

}
