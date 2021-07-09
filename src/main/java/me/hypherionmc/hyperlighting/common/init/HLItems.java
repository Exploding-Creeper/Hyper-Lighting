package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.common.items.LighterTool;
import me.hypherionmc.hyperlighting.common.items.WirelessPowerCard;
import me.hypherionmc.hyperlighting.common.items.WirelessSwitchCard;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class HLItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModConstants.MODID);

    public static final RegistryObject<Item> LIGHTER_TOOL = ITEMS.register("lighter_tool", LighterTool::new);
    public static final RegistryObject<Item> WIRELESS_POWERCARD = ITEMS.register("wireless_powercard", WirelessPowerCard::new);
    public static final RegistryObject<Item> WIRELESS_SWITCH_POWERCARD = ITEMS.register("wireless_switch_card", WirelessSwitchCard::new);

}
