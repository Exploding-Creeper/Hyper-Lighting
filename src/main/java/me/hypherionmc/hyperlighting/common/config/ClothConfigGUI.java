package me.hypherionmc.hyperlighting.common.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

public class ClothConfigGUI {

    public static Screen openGUI(Screen screen) {

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(screen)
                .setTitle(new StringTextComponent("Hyper Lighting Config"));

        ConfigCategory torch_config = builder.getOrCreateCategory(new StringTextComponent("Torch Config"));
        ConfigEntryBuilder configEntryBuilder = builder.entryBuilder();

        torch_config.addEntry(configEntryBuilder.startBooleanToggle(new StringTextComponent("Disabled Colored Lighting"), HyperLightingConfig.torchColor.get())
                .setDefaultValue(false)
                .setTooltip(new StringTextComponent("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingConfig.torchColor.set(newValue))
                .build());

        torch_config.addEntry(configEntryBuilder.startBooleanToggle(new StringTextComponent("Lit By Default"), HyperLightingConfig.torchOnByDefault.get())
                .setDefaultValue(false)
                .setTooltip(new StringTextComponent("Should Lights be lit when placed"))
                .setSaveConsumer(newValue -> HyperLightingConfig.torchOnByDefault.set(newValue))
                .build());

        ConfigCategory lantern_config = builder.getOrCreateCategory(new StringTextComponent("Lantern Config"));
        lantern_config.addEntry(configEntryBuilder.startBooleanToggle(new StringTextComponent("Disabled Colored Lighting"), HyperLightingConfig.lanternColor.get())
                .setDefaultValue(false)
                .setTooltip(new StringTextComponent("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingConfig.lanternColor.set(newValue))
                .build());

        lantern_config.addEntry(configEntryBuilder.startBooleanToggle(new StringTextComponent("Lit By Default"), HyperLightingConfig.lanternOnByDefault.get())
                .setDefaultValue(false)
                .setTooltip(new StringTextComponent("Should Lights be lit when placed"))
                .setSaveConsumer(newValue -> HyperLightingConfig.lanternOnByDefault.set(newValue))
                .build());

        ConfigCategory tiki_torch_config = builder.getOrCreateCategory(new StringTextComponent("Tiki Torch Config"));
        tiki_torch_config.addEntry(configEntryBuilder.startBooleanToggle(new StringTextComponent("Disabled Colored Lighting"), HyperLightingConfig.tikiColor.get())
                .setDefaultValue(false)
                .setTooltip(new StringTextComponent("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingConfig.tikiColor.set(newValue))
                .build());

        tiki_torch_config.addEntry(configEntryBuilder.startBooleanToggle(new StringTextComponent("Lit By Default"), HyperLightingConfig.tikiOnByDefault.get())
                .setDefaultValue(false)
                .setTooltip(new StringTextComponent("Should Lights be lit when placed"))
                .setSaveConsumer(newValue -> HyperLightingConfig.tikiOnByDefault.set(newValue))
                .build());

        ConfigCategory candle_config = builder.getOrCreateCategory(new StringTextComponent("Candle Config"));
        candle_config.addEntry(configEntryBuilder.startBooleanToggle(new StringTextComponent("Disabled Colored Lighting"), HyperLightingConfig.candleColor.get())
                .setDefaultValue(false)
                .setTooltip(new StringTextComponent("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingConfig.candleColor.set(newValue))
                .build());

        candle_config.addEntry(configEntryBuilder.startBooleanToggle(new StringTextComponent("Lit By Default"), HyperLightingConfig.candleOnByDefault.get())
                .setDefaultValue(false)
                .setTooltip(new StringTextComponent("Should Lights be lit when placed"))
                .setSaveConsumer(newValue -> HyperLightingConfig.candleOnByDefault.set(newValue))
                .build());

        ConfigCategory redstone_config = builder.getOrCreateCategory(new StringTextComponent("Redstone Config"));
        redstone_config.addEntry(configEntryBuilder.startBooleanToggle(new StringTextComponent("Disabled Colored Lighting"), HyperLightingConfig.redstoneColor.get())
                .setDefaultValue(false)
                .setTooltip(new StringTextComponent("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingConfig.redstoneColor.set(newValue))
                .build());

        ConfigCategory battery_lights_config = builder.getOrCreateCategory(new StringTextComponent("Battery Lights Config"));
        battery_lights_config.addEntry(configEntryBuilder.startBooleanToggle(new StringTextComponent("Disabled Colored Lighting"), HyperLightingConfig.batteryColor.get())
                .setDefaultValue(false)
                .setTooltip(new StringTextComponent("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingConfig.batteryColor.set(newValue))
                .build());

        battery_lights_config.addEntry(configEntryBuilder.startBooleanToggle(new StringTextComponent("Lit By Default"), HyperLightingConfig.batteryOnByDefault.get())
                .setDefaultValue(false)
                .setTooltip(new StringTextComponent("Should Lights be lit when placed"))
                .setSaveConsumer(newValue -> HyperLightingConfig.batteryOnByDefault.set(newValue))
                .build());

        ConfigCategory campfire_config = builder.getOrCreateCategory(new StringTextComponent("Campfire Config"));
        campfire_config.addEntry(configEntryBuilder.startBooleanToggle(new StringTextComponent("Disabled Colored Lighting"), HyperLightingConfig.campfireColor.get())
                .setDefaultValue(false)
                .setTooltip(new StringTextComponent("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingConfig.campfireColor.set(newValue))
                .build());

        campfire_config.addEntry(configEntryBuilder.startBooleanToggle(new StringTextComponent("Lit By Default"), HyperLightingConfig.campfireOnByDefault.get())
                .setDefaultValue(false)
                .setTooltip(new StringTextComponent("Should Lights be lit when placed"))
                .setSaveConsumer(newValue -> HyperLightingConfig.campfireOnByDefault.set(newValue))
                .build());

        ConfigCategory jack_o_lantern_config = builder.getOrCreateCategory(new StringTextComponent("Jack O Lantern Config"));
        jack_o_lantern_config.addEntry(configEntryBuilder.startBooleanToggle(new StringTextComponent("Disabled Colored Lighting"), HyperLightingConfig.jackColor.get())
                .setDefaultValue(false)
                .setTooltip(new StringTextComponent("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingConfig.jackColor.set(newValue))
                .build());

        jack_o_lantern_config.addEntry(configEntryBuilder.startBooleanToggle(new StringTextComponent("Lit By Default"), HyperLightingConfig.jackOnByDefault.get())
                .setDefaultValue(false)
                .setTooltip(new StringTextComponent("Should Lights be lit when placed"))
                .setSaveConsumer(newValue -> HyperLightingConfig.jackOnByDefault.set(newValue))
                .build());

        ConfigCategory underwater_lights_config = builder.getOrCreateCategory(new StringTextComponent("Underwater Lights Config"));
        underwater_lights_config.addEntry(configEntryBuilder.startBooleanToggle(new StringTextComponent("Disabled Colored Lighting"), HyperLightingConfig.underwaterColor.get())
                .setDefaultValue(false)
                .setTooltip(new StringTextComponent("Enable/Disable Colored Lighting Support"))
                .setSaveConsumer(newValue -> HyperLightingConfig.underwaterColor.set(newValue))
                .build());

        underwater_lights_config.addEntry(configEntryBuilder.startBooleanToggle(new StringTextComponent("Lit By Default"), HyperLightingConfig.underwaterOnByDefault.get())
                .setDefaultValue(false)
                .setTooltip(new StringTextComponent("Should Lights be lit when placed"))
                .setSaveConsumer(newValue -> HyperLightingConfig.underwaterOnByDefault.set(newValue))
                .build());

        return builder.build();

    }

}
