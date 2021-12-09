package me.hypherionmc.hyperlighting;

import me.hypherionmc.hyperlighting.common.config.HyperLightingConfig;
import me.hypherionmc.hyperlighting.common.handlers.RegistryHandler;
import me.hypherionmc.hyperlighting.common.init.HLBlocks;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HyperLightingFabric implements ModInitializer {

    public static final Logger logger = LogManager.getLogger(ModConstants.MOD_ID);
    public static HyperLightingConfig hyperLightingConfig = new HyperLightingConfig();

    public static ItemGroup mainTab = FabricItemGroupBuilder.build(
            new Identifier(ModConstants.MOD_ID, "hyperlighting"),
            () -> new ItemStack(Item.fromBlock(HLBlocks.ADVANCED_LANTERN_BLUE)));

    public static ItemGroup machinesTab = FabricItemGroupBuilder.build(
            new Identifier(ModConstants.MOD_ID, "hyperlighting_machines"),
            () -> new ItemStack(Item.fromBlock(HLBlocks.SOLAR_PANEL)));

    @Override
    public void onInitialize() {
        RegistryHandler.init();
    }
}
