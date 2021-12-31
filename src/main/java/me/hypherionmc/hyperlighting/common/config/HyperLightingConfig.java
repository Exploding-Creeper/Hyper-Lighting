package me.hypherionmc.hyperlighting.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class HyperLightingConfig {

    public static ForgeConfigSpec spec;

    public static ForgeConfigSpec.BooleanValue torchColor;
    public static ForgeConfigSpec.BooleanValue torchOnByDefault;

    public static ForgeConfigSpec.BooleanValue lanternColor;
    public static ForgeConfigSpec.BooleanValue lanternOnByDefault;

    public static ForgeConfigSpec.BooleanValue tikiColor;
    public static ForgeConfigSpec.BooleanValue tikiOnByDefault;

    public static ForgeConfigSpec.BooleanValue candleColor;
    public static ForgeConfigSpec.BooleanValue candleOnByDefault;

    public static ForgeConfigSpec.BooleanValue redstoneColor;

    public static ForgeConfigSpec.BooleanValue batteryColor;
    public static ForgeConfigSpec.BooleanValue batteryOnByDefault;

    public static ForgeConfigSpec.BooleanValue campfireColor;
    public static ForgeConfigSpec.BooleanValue campfireOnByDefault;

    public static ForgeConfigSpec.BooleanValue jackColor;
    public static ForgeConfigSpec.BooleanValue jackOnByDefault;

    public static ForgeConfigSpec.BooleanValue underwaterColor;
    public static ForgeConfigSpec.BooleanValue underwaterOnByDefault;

    public static ForgeConfigSpec.BooleanValue genColoredWater;
    public static ForgeConfigSpec.BooleanValue genGlowingColoredWater;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Hyper Lighting Config");

        builder.push("Torch Config");
        torchColor = builder
                .comment("Disables/Enables Colored lighting on Torches")
                .define("disableColorLighting", false);

        torchOnByDefault = builder
                .comment("Should Torches be lit when placed")
                .define("onByDefault", false);

        builder.pop();

        builder.push("Lantern Config");
        lanternColor = builder
                .comment("Disables/Enables Colored lighting on Lanterns")
                .define("disableColorLighting", false);

        lanternOnByDefault = builder
                .comment("Should Lanterns be lit when placed")
                .define("onByDefault", false);

        builder.pop();

        builder.push("Tiki Torch Config");
        tikiColor = builder
                .comment("Disables/Enables Colored lighting on Tiki Torches")
                .define("disableColorLighting", false);

        tikiOnByDefault = builder
                .comment("Should Tiki Torches be lit when placed")
                .define("onByDefault", false);

        builder.pop();

        builder.push("Candle Config");
        candleColor = builder
                .comment("Disables/Enables Colored lighting on Candles")
                .define("disableColorLighting", false);

        candleOnByDefault = builder
                .comment("Should Candles be lit when placed")
                .define("onByDefault", false);

        builder.pop();

        builder.push("Redstone Config");
        redstoneColor = builder
                .comment("Disables/Enables Colored lighting on Redstone Lamps")
                .define("disableColorLighting", false);

        builder.pop();

        builder.push("Battery Light Config");
        batteryColor = builder
                .comment("Disables/Enables Colored lighting on Battery Lights")
                .define("disableColorLighting", false);

        batteryOnByDefault = builder
                .comment("Should Battery Lights be lit when placed")
                .define("onByDefault", false);

        builder.pop();

        builder.push("Campfire Config");
        campfireColor = builder
                .comment("Disables/Enables Colored lighting on Campfires")
                .define("disableColorLighting", false);

        campfireOnByDefault = builder
                .comment("Should Campfires be lit when placed")
                .define("onByDefault", false);

        builder.pop();

        builder.push("Jack O Lantern Config");
        jackColor = builder
                .comment("Disables/Enables Colored lighting on Jack O Lanterns")
                .define("disableColorLighting", false);

        jackOnByDefault = builder
                .comment("Should Jack O Lanterns be lit when placed")
                .define("onByDefault", false);

        builder.pop();

        builder.push("Underwater Lights Config");
        underwaterColor = builder
                .comment("Disables/Enables Colored lighting on Underwater Lights")
                .define("disableColorLighting", false);

        underwaterOnByDefault = builder
                .comment("Should Underwater Lights be lit when placed")
                .define("onByDefault", false);

        builder.pop();

        builder.push("World Gen Config");
        genColoredWater = builder
                .comment("Disables/Enables Glowing Colored Water from spawning")
                .define("genColoredWater", true);

        genGlowingColoredWater = builder
                .comment("Disables/Enables Glowing Colored Water from spawning")
                .define("genGlowingColoredWater", true);

        builder.pop();
        spec = builder.build();

    }

}
