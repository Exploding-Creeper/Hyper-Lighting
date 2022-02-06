package me.hypherionmc.hyperlighting.client.gui;

import me.hypherionmc.hyperlighting.common.config.HyperLightingConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

public class ClothConfigGUI {

    public static Screen openGUI(Screen screen) {

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(screen)
                .setTitle(new TextComponent("Hyper Lighting Config"));

        ConfigCategory torch_config = builder.getOrCreateCategory(new TextComponent("Torch Config"));
        ConfigEntryBuilder configEntryBuilder = builder.entryBuilder();

        torch_config.addEntry(configEntryBuilder.startBooleanToggle(new TextComponent("Disabled Colored Lighting"), HyperLightingConfig.torchColor.get())
                .setDefaultValue(false)
                .setTooltip(new TextComponent("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingConfig.torchColor.set(newValue))
                .build());

        torch_config.addEntry(configEntryBuilder.startBooleanToggle(new TextComponent("Lit By Default"), HyperLightingConfig.torchOnByDefault.get())
                .setDefaultValue(false)
                .setTooltip(new TextComponent("Should Lights be lit when placed"))
                .setSaveConsumer(newValue -> HyperLightingConfig.torchOnByDefault.set(newValue))
                .build());

        ConfigCategory lantern_config = builder.getOrCreateCategory(new TextComponent("Lantern Config"));
        lantern_config.addEntry(configEntryBuilder.startBooleanToggle(new TextComponent("Disabled Colored Lighting"), HyperLightingConfig.lanternColor.get())
                .setDefaultValue(false)
                .setTooltip(new TextComponent("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingConfig.lanternColor.set(newValue))
                .build());

        lantern_config.addEntry(configEntryBuilder.startBooleanToggle(new TextComponent("Lit By Default"), HyperLightingConfig.lanternOnByDefault.get())
                .setDefaultValue(false)
                .setTooltip(new TextComponent("Should Lights be lit when placed"))
                .setSaveConsumer(newValue -> HyperLightingConfig.lanternOnByDefault.set(newValue))
                .build());

        ConfigCategory tiki_torch_config = builder.getOrCreateCategory(new TextComponent("Tiki Torch Config"));
        tiki_torch_config.addEntry(configEntryBuilder.startBooleanToggle(new TextComponent("Disabled Colored Lighting"), HyperLightingConfig.tikiColor.get())
                .setDefaultValue(false)
                .setTooltip(new TextComponent("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingConfig.tikiColor.set(newValue))
                .build());

        tiki_torch_config.addEntry(configEntryBuilder.startBooleanToggle(new TextComponent("Lit By Default"), HyperLightingConfig.tikiOnByDefault.get())
                .setDefaultValue(false)
                .setTooltip(new TextComponent("Should Lights be lit when placed"))
                .setSaveConsumer(newValue -> HyperLightingConfig.tikiOnByDefault.set(newValue))
                .build());

        ConfigCategory candle_config = builder.getOrCreateCategory(new TextComponent("Candle Config"));
        candle_config.addEntry(configEntryBuilder.startBooleanToggle(new TextComponent("Disabled Colored Lighting"), HyperLightingConfig.candleColor.get())
                .setDefaultValue(false)
                .setTooltip(new TextComponent("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingConfig.candleColor.set(newValue))
                .build());

        candle_config.addEntry(configEntryBuilder.startBooleanToggle(new TextComponent("Lit By Default"), HyperLightingConfig.candleOnByDefault.get())
                .setDefaultValue(false)
                .setTooltip(new TextComponent("Should Lights be lit when placed"))
                .setSaveConsumer(newValue -> HyperLightingConfig.candleOnByDefault.set(newValue))
                .build());

        ConfigCategory redstone_config = builder.getOrCreateCategory(new TextComponent("Redstone Config"));
        redstone_config.addEntry(configEntryBuilder.startBooleanToggle(new TextComponent("Disabled Colored Lighting"), HyperLightingConfig.redstoneColor.get())
                .setDefaultValue(false)
                .setTooltip(new TextComponent("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingConfig.redstoneColor.set(newValue))
                .build());

        ConfigCategory battery_lights_config = builder.getOrCreateCategory(new TextComponent("Battery Lights Config"));
        battery_lights_config.addEntry(configEntryBuilder.startBooleanToggle(new TextComponent("Disabled Colored Lighting"), HyperLightingConfig.batteryColor.get())
                .setDefaultValue(false)
                .setTooltip(new TextComponent("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingConfig.batteryColor.set(newValue))
                .build());

        battery_lights_config.addEntry(configEntryBuilder.startBooleanToggle(new TextComponent("Lit By Default"), HyperLightingConfig.batteryOnByDefault.get())
                .setDefaultValue(false)
                .setTooltip(new TextComponent("Should Lights be lit when placed"))
                .setSaveConsumer(newValue -> HyperLightingConfig.batteryOnByDefault.set(newValue))
                .build());

        ConfigCategory campfire_config = builder.getOrCreateCategory(new TextComponent("Campfire Config"));
        campfire_config.addEntry(configEntryBuilder.startBooleanToggle(new TextComponent("Disabled Colored Lighting"), HyperLightingConfig.campfireColor.get())
                .setDefaultValue(false)
                .setTooltip(new TextComponent("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingConfig.campfireColor.set(newValue))
                .build());

        campfire_config.addEntry(configEntryBuilder.startBooleanToggle(new TextComponent("Lit By Default"), HyperLightingConfig.campfireOnByDefault.get())
                .setDefaultValue(false)
                .setTooltip(new TextComponent("Should Lights be lit when placed"))
                .setSaveConsumer(newValue -> HyperLightingConfig.campfireOnByDefault.set(newValue))
                .build());

        ConfigCategory jack_o_lantern_config = builder.getOrCreateCategory(new TextComponent("Jack O Lantern Config"));
        jack_o_lantern_config.addEntry(configEntryBuilder.startBooleanToggle(new TextComponent("Disabled Colored Lighting"), HyperLightingConfig.jackColor.get())
                .setDefaultValue(false)
                .setTooltip(new TextComponent("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingConfig.jackColor.set(newValue))
                .build());

        jack_o_lantern_config.addEntry(configEntryBuilder.startBooleanToggle(new TextComponent("Lit By Default"), HyperLightingConfig.jackOnByDefault.get())
                .setDefaultValue(false)
                .setTooltip(new TextComponent("Should Lights be lit when placed"))
                .setSaveConsumer(newValue -> HyperLightingConfig.jackOnByDefault.set(newValue))
                .build());

        ConfigCategory underwater_lights_config = builder.getOrCreateCategory(new TextComponent("Underwater Lights Config"));
        underwater_lights_config.addEntry(configEntryBuilder.startBooleanToggle(new TextComponent("Disabled Colored Lighting"), HyperLightingConfig.underwaterColor.get())
                .setDefaultValue(false)
                .setTooltip(new TextComponent("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingConfig.underwaterColor.set(newValue))
                .build());

        underwater_lights_config.addEntry(configEntryBuilder.startBooleanToggle(new TextComponent("Lit By Default"), HyperLightingConfig.underwaterOnByDefault.get())
                .setDefaultValue(false)
                .setTooltip(new TextComponent("Should Lights be lit when placed"))
                .setSaveConsumer(newValue -> HyperLightingConfig.underwaterOnByDefault.set(newValue))
                .build());

        ConfigCategory woldgenConfig = builder.getOrCreateCategory(new TextComponent("World Gen Config"));
        woldgenConfig.addEntry(configEntryBuilder.startBooleanToggle(new TextComponent("Generate Colored Water Lakes"), HyperLightingConfig.genColoredWater.get())
                .setDefaultValue(true)
                .requireRestart()
                .setTooltip(new TextComponent("Enable/Disable Colored Water lakes from spawning"))
                .setSaveConsumer(newValue -> HyperLightingConfig.genColoredWater.set(newValue))
                .build());

        woldgenConfig.addEntry(configEntryBuilder.startBooleanToggle(new TextComponent("Generate Glowing Colored Water Lakes"), HyperLightingConfig.genGlowingColoredWater.get())
                .setDefaultValue(true)
                .requireRestart()
                .setTooltip(new TextComponent("Enable/Disable Glowing Colored Water lakes from spawning"))
                .setSaveConsumer(newValue -> HyperLightingConfig.genGlowingColoredWater.set(newValue))
                .build());

        return builder.build();
    }
}
