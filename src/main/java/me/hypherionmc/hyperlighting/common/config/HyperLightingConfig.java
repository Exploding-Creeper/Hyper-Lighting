package me.hypherionmc.hyperlighting.common.config;

import me.hypherionmc.nightconfig.core.conversion.Path;
import me.hypherionmc.nightconfig.core.conversion.SpecComment;

public class HyperLightingConfig {

    @Path("torchconfig.coloredlight")
    @SpecComment("Use Colored Lighting on Torches with RGBLib installed")
    public boolean torchColor = true;
    @Path("torchconfig.litonplaced")
    @SpecComment("Must torches be lit when placed")
    public boolean torchOnByDefault = false;

    @Path("laternconfig.coloredlight")
    @SpecComment("Use Colored Lighting on Lanterns with RGBLib installed")
    public boolean lanternColor = true;
    @Path("laternconfig.litonplaced")
    @SpecComment("Must lanterns be lit when placed")
    public boolean lanternOnByDefault = false;

    @Path("tikiconfig.coloredlight")
    @SpecComment("Use Colored Lighting on Tiki Torches with RGBLib installed")
    public boolean tikiColor = true;
    @Path("tikiconfig.litonplaced")
    @SpecComment("Must Tiki Torches be lit when placed")
    public boolean tikiOnByDefault = false;

    @Path("candleconfig.coloredlight")
    @SpecComment("Use Colored Lighting on Candles with RGBLib installed")
    public boolean candleColor = true;
    @Path("candleconfig.litonplaced")
    @SpecComment("Must candles be lit when placed")
    public boolean candleOnByDefault = false;

    @Path("redstoneconfig.coloredlight")
    @SpecComment("Use Colored Lighting on Redstone Lamps with RGBLib installed")
    public boolean redstoneColor = true;

    @Path("batteryconfig.coloredlight")
    @SpecComment("Use Colored Lighting on Battery Neon with RGBLib installed")
    public boolean batteryColor = true;
    @Path("batteryconfig.litonplaced")
    @SpecComment("Must battery neon be lit when placed")
    public boolean batteryOnByDefault = false;

    @Path("campfireconfig.coloredlight")
    @SpecComment("Use Colored Lighting on Campfires with RGBLib installed")
    public boolean campfireColor = true;
    @Path("campfireconfig.litonplaced")
    @SpecComment("Must Campfires be lit when placed")
    public boolean campfireOnByDefault = false;

    @Path("jackconfig.coloredlight")
    @SpecComment("Use Colored Lighting on Jack O Lanterns with RGBLib installed")
    public boolean jackColor = true;
    @Path("jackconfig.litonplaced")
    @SpecComment("Must Jack O Lanterns be lit when placed")
    public boolean jackOnByDefault = false;

    @Path("soulfireconfig.coloredlight")
    @SpecComment("Use Colored Lighting on Underwater Lights with RGBLib installed")
    public boolean underwaterColor = true;

    @Path("soulfireconfig.litonplaced")
    @SpecComment("Must Underwater Lights be lit when placed")
    public boolean underwaterOnByDefault = false;

    @Path("worldgen.genColoredLakes")
    @SpecComment("Must colored water lakes be generated")
    public boolean genColoredLakes = true;

    @Path("worldgen.genColoredGlowingLakes")
    @SpecComment("Must glowing colored water lakes be generated")
    public boolean genColoredGlowingLakes = true;

}
