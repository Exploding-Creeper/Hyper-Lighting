package me.hypherionmc.hyperlighting.client.gui;

import me.hypherionmc.hyperlighting.HyperLightingFabric;
import me.hypherionmc.hyperlighting.common.config.ConfigHandler;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;

public class ClothConfigScreen {

    public static Screen openGUI(Screen screen) {

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(screen)
                .setTitle(new LiteralText("Hyper Lighting Config"));

        ConfigCategory torch_config = builder.getOrCreateCategory(new LiteralText("Torch Config"));
        ConfigEntryBuilder configEntryBuilder = builder.entryBuilder();

        torch_config.addEntry(configEntryBuilder.startBooleanToggle(new LiteralText("Disabled Colored Lighting"), HyperLightingFabric.hyperLightingConfig.torchColor)
                .setDefaultValue(false)
                .setTooltip(new LiteralText("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingFabric.hyperLightingConfig.torchColor = newValue)
                .build());

        torch_config.addEntry(configEntryBuilder.startBooleanToggle(new LiteralText("Lit By Default"), HyperLightingFabric.hyperLightingConfig.torchOnByDefault)
                .setDefaultValue(false)
                .setTooltip(new LiteralText("Should Lights be lit when placed"))
                .setSaveConsumer(newValue -> HyperLightingFabric.hyperLightingConfig.torchOnByDefault = newValue)
                .build());

        ConfigCategory lantern_config = builder.getOrCreateCategory(new LiteralText("Lantern Config"));
        lantern_config.addEntry(configEntryBuilder.startBooleanToggle(new LiteralText("Disabled Colored Lighting"), HyperLightingFabric.hyperLightingConfig.lanternColor)
                .setDefaultValue(false)
                .setTooltip(new LiteralText("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingFabric.hyperLightingConfig.lanternColor = newValue)
                .build());

        lantern_config.addEntry(configEntryBuilder.startBooleanToggle(new LiteralText("Lit By Default"), HyperLightingFabric.hyperLightingConfig.lanternOnByDefault)
                .setDefaultValue(false)
                .setTooltip(new LiteralText("Should Lights be lit when placed"))
                .setSaveConsumer(newValue -> HyperLightingFabric.hyperLightingConfig.lanternOnByDefault = newValue)
                .build());

        ConfigCategory tiki_torch_config = builder.getOrCreateCategory(new LiteralText("Tiki Torch Config"));
        tiki_torch_config.addEntry(configEntryBuilder.startBooleanToggle(new LiteralText("Disabled Colored Lighting"), HyperLightingFabric.hyperLightingConfig.tikiColor)
                .setDefaultValue(false)
                .setTooltip(new LiteralText("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingFabric.hyperLightingConfig.tikiColor = newValue)
                .build());

        tiki_torch_config.addEntry(configEntryBuilder.startBooleanToggle(new LiteralText("Lit By Default"), HyperLightingFabric.hyperLightingConfig.tikiOnByDefault)
                .setDefaultValue(false)
                .setTooltip(new LiteralText("Should Lights be lit when placed"))
                .setSaveConsumer(newValue -> HyperLightingFabric.hyperLightingConfig.tikiOnByDefault = newValue)
                .build());

        ConfigCategory candle_config = builder.getOrCreateCategory(new LiteralText("Candle Config"));
        candle_config.addEntry(configEntryBuilder.startBooleanToggle(new LiteralText("Disabled Colored Lighting"), HyperLightingFabric.hyperLightingConfig.candleColor)
                .setDefaultValue(false)
                .setTooltip(new LiteralText("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingFabric.hyperLightingConfig.candleColor = newValue)
                .build());

        candle_config.addEntry(configEntryBuilder.startBooleanToggle(new LiteralText("Lit By Default"), HyperLightingFabric.hyperLightingConfig.candleOnByDefault)
                .setDefaultValue(false)
                .setTooltip(new LiteralText("Should Lights be lit when placed"))
                .setSaveConsumer(newValue -> HyperLightingFabric.hyperLightingConfig.candleOnByDefault = newValue)
                .build());

        ConfigCategory redstone_config = builder.getOrCreateCategory(new LiteralText("Redstone Config"));
        redstone_config.addEntry(configEntryBuilder.startBooleanToggle(new LiteralText("Disabled Colored Lighting"), HyperLightingFabric.hyperLightingConfig.redstoneColor)
                .setDefaultValue(false)
                .setTooltip(new LiteralText("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingFabric.hyperLightingConfig.redstoneColor = newValue)
                .build());

        ConfigCategory battery_lights_config = builder.getOrCreateCategory(new LiteralText("Battery Lights Config"));
        battery_lights_config.addEntry(configEntryBuilder.startBooleanToggle(new LiteralText("Disabled Colored Lighting"), HyperLightingFabric.hyperLightingConfig.batteryColor)
                .setDefaultValue(false)
                .setTooltip(new LiteralText("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingFabric.hyperLightingConfig.batteryColor = newValue)
                .build());

        battery_lights_config.addEntry(configEntryBuilder.startBooleanToggle(new LiteralText("Lit By Default"), HyperLightingFabric.hyperLightingConfig.batteryOnByDefault)
                .setDefaultValue(false)
                .setTooltip(new LiteralText("Should Lights be lit when placed"))
                .setSaveConsumer(newValue -> HyperLightingFabric.hyperLightingConfig.batteryOnByDefault = newValue)
                .build());

        ConfigCategory campfire_config = builder.getOrCreateCategory(new LiteralText("Campfire Config"));
        campfire_config.addEntry(configEntryBuilder.startBooleanToggle(new LiteralText("Disabled Colored Lighting"), HyperLightingFabric.hyperLightingConfig.campfireColor)
                .setDefaultValue(false)
                .setTooltip(new LiteralText("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingFabric.hyperLightingConfig.campfireColor = newValue)
                .build());

        campfire_config.addEntry(configEntryBuilder.startBooleanToggle(new LiteralText("Lit By Default"), HyperLightingFabric.hyperLightingConfig.campfireOnByDefault)
                .setDefaultValue(false)
                .setTooltip(new LiteralText("Should Lights be lit when placed"))
                .setSaveConsumer(newValue -> HyperLightingFabric.hyperLightingConfig.campfireOnByDefault = newValue)
                .build());

        ConfigCategory jack_o_lantern_config = builder.getOrCreateCategory(new LiteralText("Jack O Lantern Config"));
        jack_o_lantern_config.addEntry(configEntryBuilder.startBooleanToggle(new LiteralText("Disabled Colored Lighting"), HyperLightingFabric.hyperLightingConfig.jackColor)
                .setDefaultValue(false)
                .setTooltip(new LiteralText("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingFabric.hyperLightingConfig.jackColor = newValue)
                .build());

        jack_o_lantern_config.addEntry(configEntryBuilder.startBooleanToggle(new LiteralText("Lit By Default"), HyperLightingFabric.hyperLightingConfig.jackOnByDefault)
                .setDefaultValue(false)
                .setTooltip(new LiteralText("Should Lights be lit when placed"))
                .setSaveConsumer(newValue -> HyperLightingFabric.hyperLightingConfig.jackOnByDefault = newValue)
                .build());

        ConfigCategory underwater_lights_config = builder.getOrCreateCategory(new LiteralText("Underwater Lights Config"));
        underwater_lights_config.addEntry(configEntryBuilder.startBooleanToggle(new LiteralText("Disabled Colored Lighting"), HyperLightingFabric.hyperLightingConfig.underwaterColor)
                .setDefaultValue(false)
                .setTooltip(new LiteralText("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingFabric.hyperLightingConfig.underwaterColor = newValue)
                .build());

        underwater_lights_config.addEntry(configEntryBuilder.startBooleanToggle(new LiteralText("Lit By Default"), HyperLightingFabric.hyperLightingConfig.underwaterOnByDefault)
                .setDefaultValue(false)
                .setTooltip(new LiteralText("Should Lights be lit when placed"))
                .setSaveConsumer(newValue -> HyperLightingFabric.hyperLightingConfig.underwaterOnByDefault = newValue)
                .build());

        ConfigCategory woldgenConfig = builder.getOrCreateCategory(new LiteralText("World Gen Config"));
        woldgenConfig.addEntry(configEntryBuilder.startBooleanToggle(new LiteralText("Generate Colored Water Lakes"), HyperLightingFabric.hyperLightingConfig.genColoredLakes)
                .setDefaultValue(true)
                .requireRestart()
                .setTooltip(new LiteralText("Enable/Disable Colored Water lakes from spawning"))
                .setSaveConsumer(newValue -> HyperLightingFabric.hyperLightingConfig.genColoredLakes = newValue)
                .build());

        woldgenConfig.addEntry(configEntryBuilder.startBooleanToggle(new LiteralText("Generate Glowing Colored Water Lakes"), HyperLightingFabric.hyperLightingConfig.genColoredGlowingLakes)
                .setDefaultValue(true)
                .requireRestart()
                .setTooltip(new LiteralText("Enable/Disable Glowing Colored Water lakes from spawning"))
                .setSaveConsumer(newValue -> HyperLightingFabric.hyperLightingConfig.genColoredGlowingLakes = newValue)
                .build());

        builder.setSavingRunnable(() -> {
            ConfigHandler.saveConfig(HyperLightingFabric.hyperLightingConfig);
        });

        return builder.build();
    }

}
