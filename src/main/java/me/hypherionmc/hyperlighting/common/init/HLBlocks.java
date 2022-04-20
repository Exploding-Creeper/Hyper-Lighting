package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.hyperlighting.HyperLightingFabric;
import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.common.blocks.*;
import net.minecraft.block.Block;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class HLBlocks {

    public static final List<Block> BLOCKS = new ArrayList<>();

    /***
     * TORCHES
     */
    public static final Block ADVANCED_TORCH_ORANGE = register("advanced_torch_orange", new AdvancedTorchBlock("advanced_torch_orange", DyeColor.ORANGE, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_TORCH_BLUE = register("advanced_torch_blue", new AdvancedTorchBlock("advanced_torch_blue", DyeColor.BLUE, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_TORCH_RED = register("advanced_torch_red", new AdvancedTorchBlock("advanced_torch_red", DyeColor.RED, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_TORCH_PURPLE = register("advanced_torch_purple", new AdvancedTorchBlock("advanced_torch_purple", DyeColor.PURPLE, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_TORCH_MAGENTA = register("advanced_torch_magenta", new AdvancedTorchBlock("advanced_torch_magenta", DyeColor.MAGENTA, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_TORCH_GREEN = register("advanced_torch_green", new AdvancedTorchBlock("advanced_torch_green", DyeColor.GREEN, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_TORCH_LIME = register("advanced_torch_lime", new AdvancedTorchBlock("advanced_torch_lime", DyeColor.LIME, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_TORCH_CYAN = register("advanced_torch_cyan", new AdvancedTorchBlock("advanced_torch_cyan", DyeColor.CYAN, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_TORCH_YELLOW = register("advanced_torch_yellow", new AdvancedTorchBlock("advanced_torch_yellow", DyeColor.YELLOW, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_TORCH_LIGHT_GRAY = register("advanced_torch_light_gray", new AdvancedTorchBlock("advanced_torch_light_gray", DyeColor.LIGHT_GRAY, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_TORCH_GRAY = register("advanced_torch_gray", new AdvancedTorchBlock("advanced_torch_gray", DyeColor.GRAY, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_TORCH_WHITE = register("advanced_torch_white", new AdvancedTorchBlock("advanced_torch_white", DyeColor.WHITE, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_TORCH_BLACK = register("advanced_torch_black", new AdvancedTorchBlock("advanced_torch_black", DyeColor.BLACK, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_TORCH_LIGHT_BLUE = register("advanced_torch_light_blue", new AdvancedTorchBlock("advanced_torch_light_blue", DyeColor.LIGHT_BLUE, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_TORCH_BROWN = register("advanced_torch_brown", new AdvancedTorchBlock("advanced_torch_brown", DyeColor.BROWN, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_TORCH_PINK = register("advanced_torch_pink", new AdvancedTorchBlock("advanced_torch_pink", DyeColor.PINK, HyperLightingFabric.mainTab));

    /**
     * LANTERNS
     */
    public static final Block ADVANCED_LANTERN_WHITE = register("advanced_lantern_white", new AdvancedLantern("advanced_lantern_white", DyeColor.WHITE, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_LANTERN_ORANGE = register("advanced_lantern_orange", new AdvancedLantern("advanced_lantern_orange", DyeColor.ORANGE, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_LANTERN_MAGENTA = register("advanced_lantern_magenta", new AdvancedLantern("advanced_lantern_magenta", DyeColor.MAGENTA, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_LANTERN_LIGHT_BLUE = register("advanced_lantern_light_blue", new AdvancedLantern("advanced_lantern_light_blue", DyeColor.LIGHT_BLUE, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_LANTERN_YELLOW = register("advanced_lantern_yellow", new AdvancedLantern("advanced_lantern_yellow", DyeColor.YELLOW, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_LANTERN_LIME = register("advanced_lantern_lime", new AdvancedLantern("advanced_lantern_lime", DyeColor.LIME, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_LANTERN_PINK = register("advanced_lantern_pink", new AdvancedLantern("advanced_lantern_pink", DyeColor.PINK, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_LANTERN_GRAY = register("advanced_lantern_gray", new AdvancedLantern("advanced_lantern_gray", DyeColor.GRAY, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_LANTERN_LIGHT_GRAY = register("advanced_lantern_light_gray", new AdvancedLantern("advanced_lantern_light_gray", DyeColor.LIGHT_GRAY, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_LANTERN_CYAN = register("advanced_lantern_cyan", new AdvancedLantern("advanced_lantern_cyan", DyeColor.CYAN, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_LANTERN_PURPLE = register("advanced_lantern_purple", new AdvancedLantern("advanced_lantern_purple", DyeColor.PURPLE, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_LANTERN_BLUE = register("advanced_lantern_blue", new AdvancedLantern("advanced_lantern_blue", DyeColor.BLUE, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_LANTERN_BROWN = register("advanced_lantern_brown", new AdvancedLantern("advanced_lantern_brown", DyeColor.BROWN, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_LANTERN_GREEN = register("advanced_lantern_green", new AdvancedLantern("advanced_lantern_green", DyeColor.GREEN, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_LANTERN_RED = register("advanced_lantern_red", new AdvancedLantern("advanced_lantern_red", DyeColor.RED, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_LANTERN_BLACK = register("advanced_lantern_black", new AdvancedLantern("advanced_lantern_black", DyeColor.BLACK, HyperLightingFabric.mainTab));

    /**
     * CANDLES
     */
    public static final Block ADVANCED_CANDLE_WHITE = register("advanced_candle_white", new AdvancedCandle("advanced_candle_white", DyeColor.WHITE, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_CANDLE_ORANGE = register("advanced_candle_orange", new AdvancedCandle("advanced_candle_orange", DyeColor.ORANGE, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_CANDLE_MAGENTA = register("advanced_candle_magenta", new AdvancedCandle("advanced_candle_magenta", DyeColor.MAGENTA, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_CANDLE_LIGHT_BLUE = register("advanced_candle_light_blue", new AdvancedCandle("advanced_candle_light_blue", DyeColor.LIGHT_BLUE, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_CANDLE_YELLOW = register("advanced_candle_yellow", new AdvancedCandle("advanced_candle_yellow", DyeColor.YELLOW, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_CANDLE_LIME = register("advanced_candle_lime", new AdvancedCandle("advanced_candle_lime", DyeColor.LIME, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_CANDLE_PINK = register("advanced_candle_pink", new AdvancedCandle("advanced_candle_pink", DyeColor.PINK, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_CANDLE_GRAY = register("advanced_candle_gray", new AdvancedCandle("advanced_candle_gray", DyeColor.GRAY, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_CANDLE_LIGHT_GRAY = register("advanced_candle_light_gray", new AdvancedCandle("advanced_candle_light_gray", DyeColor.LIGHT_GRAY, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_CANDLE_CYAN = register("advanced_candle_cyan", new AdvancedCandle("advanced_candle_cyan", DyeColor.CYAN, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_CANDLE_PURPLE = register("advanced_candle_purple", new AdvancedCandle("advanced_candle_purple", DyeColor.PURPLE, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_CANDLE_BLUE = register("advanced_candle_blue", new AdvancedCandle("advanced_candle_blue", DyeColor.BLUE, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_CANDLE_BROWN = register("advanced_candle_brown", new AdvancedCandle("advanced_candle_brown", DyeColor.BROWN, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_CANDLE_GREEN = register("advanced_candle_green", new AdvancedCandle("advanced_candle_green", DyeColor.GREEN, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_CANDLE_RED = register("advanced_candle_red", new AdvancedCandle("advanced_candle_red", DyeColor.RED, HyperLightingFabric.mainTab));
    public static final Block ADVANCED_CANDLE_BLACK = register("advanced_candle_black", new AdvancedCandle("advanced_candle_black", DyeColor.BLACK, HyperLightingFabric.mainTab));

    /**
     * TIKI TORCHES
     */
    public static final Block TIKI_TORCH_WHITE = register("tiki_torch_white", new TikiTorch("tiki_torch_white", DyeColor.WHITE, HyperLightingFabric.mainTab));
    public static final Block TIKI_TORCH_ORANGE = register("tiki_torch_orange", new TikiTorch("tiki_torch_orange", DyeColor.ORANGE, HyperLightingFabric.mainTab));
    public static final Block TIKI_TORCH_MAGENTA = register("tiki_torch_magenta", new TikiTorch("tiki_torch_magenta", DyeColor.MAGENTA, HyperLightingFabric.mainTab));
    public static final Block TIKI_TORCH_LIGHT_BLUE = register("tiki_torch_light_blue", new TikiTorch("tiki_torch_light_blue", DyeColor.LIGHT_BLUE, HyperLightingFabric.mainTab));
    public static final Block TIKI_TORCH_YELLOW = register("tiki_torch_yellow", new TikiTorch("tiki_torch_yellow", DyeColor.YELLOW, HyperLightingFabric.mainTab));
    public static final Block TIKI_TORCH_LIME = register("tiki_torch_lime", new TikiTorch("tiki_torch_lime", DyeColor.LIME, HyperLightingFabric.mainTab));
    public static final Block TIKI_TORCH_PINK = register("tiki_torch_pink", new TikiTorch("tiki_torch_pink", DyeColor.PINK, HyperLightingFabric.mainTab));
    public static final Block TIKI_TORCH_GRAY = register("tiki_torch_gray", new TikiTorch("tiki_torch_gray", DyeColor.GRAY, HyperLightingFabric.mainTab));
    public static final Block TIKI_TORCH_LIGHT_GRAY = register("tiki_torch_light_gray", new TikiTorch("tiki_torch_light_gray", DyeColor.LIGHT_GRAY, HyperLightingFabric.mainTab));
    public static final Block TIKI_TORCH_CYAN = register("tiki_torch_cyan", new TikiTorch("tiki_torch_cyan", DyeColor.CYAN, HyperLightingFabric.mainTab));
    public static final Block TIKI_TORCH_PURPLE = register("tiki_torch_purple", new TikiTorch("tiki_torch_purple", DyeColor.PURPLE, HyperLightingFabric.mainTab));
    public static final Block TIKI_TORCH_BLUE = register("tiki_torch_blue", new TikiTorch("tiki_torch_blue", DyeColor.BLUE, HyperLightingFabric.mainTab));
    public static final Block TIKI_TORCH_BROWN = register("tiki_torch_brown", new TikiTorch("tiki_torch_brown", DyeColor.BROWN, HyperLightingFabric.mainTab));
    public static final Block TIKI_TORCH_GREEN = register("tiki_torch_green", new TikiTorch("tiki_torch_green", DyeColor.GREEN, HyperLightingFabric.mainTab));
    public static final Block TIKI_TORCH_RED = register("tiki_torch_red", new TikiTorch("tiki_torch_red", DyeColor.RED, HyperLightingFabric.mainTab));
    public static final Block TIKI_TORCH_BLACK = register("tiki_torch_black", new TikiTorch("tiki_torch_black", DyeColor.BLACK, HyperLightingFabric.mainTab));

    /**
     * Redstone Lamps
     */
    public static final Block COLORED_REDSTONE_WHITE = register("colored_redstone_white", new ColoredRedstoneLamp("colored_redstone_white", DyeColor.WHITE, HyperLightingFabric.mainTab));
    public static final Block COLORED_REDSTONE_ORANGE = register("colored_redstone_orange", new ColoredRedstoneLamp("colored_redstone_orange", DyeColor.ORANGE, HyperLightingFabric.mainTab));
    public static final Block COLORED_REDSTONE_MAGENTA = register("colored_redstone_magenta", new ColoredRedstoneLamp("colored_redstone_magenta", DyeColor.MAGENTA, HyperLightingFabric.mainTab));
    public static final Block COLORED_REDSTONE_LIGHT_BLUE = register("colored_redstone_light_blue", new ColoredRedstoneLamp("colored_redstone_light_blue", DyeColor.LIGHT_BLUE, HyperLightingFabric.mainTab));
    public static final Block COLORED_REDSTONE_YELLOW = register("colored_redstone_yellow", new ColoredRedstoneLamp("colored_redstone_yellow", DyeColor.YELLOW, HyperLightingFabric.mainTab));
    public static final Block COLORED_REDSTONE_LIME = register("colored_redstone_lime", new ColoredRedstoneLamp("colored_redstone_lime", DyeColor.LIME, HyperLightingFabric.mainTab));
    public static final Block COLORED_REDSTONE_PINK = register("colored_redstone_pink", new ColoredRedstoneLamp("colored_redstone_pink", DyeColor.PINK, HyperLightingFabric.mainTab));
    public static final Block COLORED_REDSTONE_GRAY = register("colored_redstone_gray", new ColoredRedstoneLamp("colored_redstone_gray", DyeColor.GRAY, HyperLightingFabric.mainTab));
    public static final Block COLORED_REDSTONE_LIGHT_GRAY = register("colored_redstone_light_gray", new ColoredRedstoneLamp("colored_redstone_light_gray", DyeColor.LIGHT_GRAY, HyperLightingFabric.mainTab));
    public static final Block COLORED_REDSTONE_CYAN = register("colored_redstone_cyan", new ColoredRedstoneLamp("colored_redstone_cyan", DyeColor.CYAN, HyperLightingFabric.mainTab));
    public static final Block COLORED_REDSTONE_PURPLE = register("colored_redstone_purple", new ColoredRedstoneLamp("colored_redstone_purple", DyeColor.PURPLE, HyperLightingFabric.mainTab));
    public static final Block COLORED_REDSTONE_BLUE = register("colored_redstone_blue", new ColoredRedstoneLamp("colored_redstone_blue", DyeColor.BLUE, HyperLightingFabric.mainTab));
    public static final Block COLORED_REDSTONE_BROWN = register("colored_redstone_brown", new ColoredRedstoneLamp("colored_redstone_brown", DyeColor.BROWN, HyperLightingFabric.mainTab));
    public static final Block COLORED_REDSTONE_GREEN = register("colored_redstone_green", new ColoredRedstoneLamp("colored_redstone_green", DyeColor.GREEN, HyperLightingFabric.mainTab));
    public static final Block COLORED_REDSTONE_RED = register("colored_redstone_red", new ColoredRedstoneLamp("colored_redstone_red", DyeColor.RED, HyperLightingFabric.mainTab));
    public static final Block COLORED_REDSTONE_BLACK = register("colored_redstone_black", new ColoredRedstoneLamp("colored_redstone_black", DyeColor.BLACK, HyperLightingFabric.mainTab));

    /**
     * Glowstone
     */
    public static final Block COLORED_GLOWSTONE_WHITE = register("colored_glowstone_white", new ColoredGlowstone("colored_glowstone_white", DyeColor.WHITE, HyperLightingFabric.mainTab));
    public static final Block COLORED_GLOWSTONE_ORANGE = register("colored_glowstone_orange", new ColoredGlowstone("colored_glowstone_orange", DyeColor.ORANGE, HyperLightingFabric.mainTab));
    public static final Block COLORED_GLOWSTONE_MAGENTA = register("colored_glowstone_magenta", new ColoredGlowstone("colored_glowstone_magenta", DyeColor.MAGENTA, HyperLightingFabric.mainTab));
    public static final Block COLORED_GLOWSTONE_LIGHT_BLUE = register("colored_glowstone_light_blue", new ColoredGlowstone("colored_glowstone_light_blue", DyeColor.LIGHT_BLUE, HyperLightingFabric.mainTab));
    public static final Block COLORED_GLOWSTONE_YELLOW = register("colored_glowstone_yellow", new ColoredGlowstone("colored_glowstone_yellow", DyeColor.YELLOW, HyperLightingFabric.mainTab));
    public static final Block COLORED_GLOWSTONE_LIME = register("colored_glowstone_lime", new ColoredGlowstone("colored_glowstone_lime", DyeColor.LIME, HyperLightingFabric.mainTab));
    public static final Block COLORED_GLOWSTONE_PINK = register("colored_glowstone_pink", new ColoredGlowstone("colored_glowstone_pink", DyeColor.PINK, HyperLightingFabric.mainTab));
    public static final Block COLORED_GLOWSTONE_GRAY = register("colored_glowstone_gray", new ColoredGlowstone("colored_glowstone_gray", DyeColor.GRAY, HyperLightingFabric.mainTab));
    public static final Block COLORED_GLOWSTONE_LIGHT_GRAY = register("colored_glowstone_light_gray", new ColoredGlowstone("colored_glowstone_light_gray", DyeColor.LIGHT_GRAY, HyperLightingFabric.mainTab));
    public static final Block COLORED_GLOWSTONE_CYAN = register("colored_glowstone_cyan", new ColoredGlowstone("colored_glowstone_cyan", DyeColor.CYAN, HyperLightingFabric.mainTab));
    public static final Block COLORED_GLOWSTONE_PURPLE = register("colored_glowstone_purple", new ColoredGlowstone("colored_glowstone_purple", DyeColor.PURPLE, HyperLightingFabric.mainTab));
    public static final Block COLORED_GLOWSTONE_BLUE = register("colored_glowstone_blue", new ColoredGlowstone("colored_glowstone_blue", DyeColor.BLUE, HyperLightingFabric.mainTab));
    public static final Block COLORED_GLOWSTONE_BROWN = register("colored_glowstone_brown", new ColoredGlowstone("colored_glowstone_brown", DyeColor.BROWN, HyperLightingFabric.mainTab));
    public static final Block COLORED_GLOWSTONE_GREEN = register("colored_glowstone_green", new ColoredGlowstone("colored_glowstone_green", DyeColor.GREEN, HyperLightingFabric.mainTab));
    public static final Block COLORED_GLOWSTONE_RED = register("colored_glowstone_red", new ColoredGlowstone("colored_glowstone_red", DyeColor.RED, HyperLightingFabric.mainTab));
    public static final Block COLORED_GLOWSTONE_BLACK = register("colored_glowstone_black", new ColoredGlowstone("colored_glowstone_black", DyeColor.BLACK, HyperLightingFabric.mainTab));

    /**
     * Sea Lanterns
     */
    public static final Block COLORED_SEALANTERN_WHITE = register("colored_sealantern_white", new ColoredSeaLantern("colored_sealantern_white", DyeColor.WHITE, HyperLightingFabric.mainTab));
    public static final Block COLORED_SEALANTERN_ORANGE = register("colored_sealantern_orange", new ColoredSeaLantern("colored_sealantern_orange", DyeColor.ORANGE, HyperLightingFabric.mainTab));
    public static final Block COLORED_SEALANTERN_MAGENTA = register("colored_sealantern_magenta", new ColoredSeaLantern("colored_sealantern_magenta", DyeColor.MAGENTA, HyperLightingFabric.mainTab));
    public static final Block COLORED_SEALANTERN_LIGHT_BLUE = register("colored_sealantern_light_blue", new ColoredSeaLantern("colored_sealantern_light_blue", DyeColor.LIGHT_BLUE, HyperLightingFabric.mainTab));
    public static final Block COLORED_SEALANTERN_YELLOW = register("colored_sealantern_yellow", new ColoredSeaLantern("colored_sealantern_yellow", DyeColor.YELLOW, HyperLightingFabric.mainTab));
    public static final Block COLORED_SEALANTERN_LIME = register("colored_sealantern_lime", new ColoredSeaLantern("colored_sealantern_lime", DyeColor.LIME, HyperLightingFabric.mainTab));
    public static final Block COLORED_SEALANTERN_PINK = register("colored_sealantern_pink", new ColoredSeaLantern("colored_sealantern_pink", DyeColor.PINK, HyperLightingFabric.mainTab));
    public static final Block COLORED_SEALANTERN_GRAY = register("colored_sealantern_gray", new ColoredSeaLantern("colored_sealantern_gray", DyeColor.GRAY, HyperLightingFabric.mainTab));
    public static final Block COLORED_SEALANTERN_LIGHT_GRAY = register("colored_sealantern_light_gray", new ColoredSeaLantern("colored_sealantern_light_gray", DyeColor.LIGHT_GRAY, HyperLightingFabric.mainTab));
    public static final Block COLORED_SEALANTERN_CYAN = register("colored_sealantern_cyan", new ColoredSeaLantern("colored_sealantern_cyan", DyeColor.CYAN, HyperLightingFabric.mainTab));
    public static final Block COLORED_SEALANTERN_PURPLE = register("colored_sealantern_purple", new ColoredSeaLantern("colored_sealantern_purple", DyeColor.PURPLE, HyperLightingFabric.mainTab));
    public static final Block COLORED_SEALANTERN_BLUE = register("colored_sealantern_blue", new ColoredSeaLantern("colored_sealantern_blue", DyeColor.BLUE, HyperLightingFabric.mainTab));
    public static final Block COLORED_SEALANTERN_BROWN = register("colored_sealantern_brown", new ColoredSeaLantern("colored_sealantern_brown", DyeColor.BROWN, HyperLightingFabric.mainTab));
    public static final Block COLORED_SEALANTERN_GREEN = register("colored_sealantern_green", new ColoredSeaLantern("colored_sealantern_green", DyeColor.GREEN, HyperLightingFabric.mainTab));
    public static final Block COLORED_SEALANTERN_RED = register("colored_sealantern_red", new ColoredSeaLantern("colored_sealantern_red", DyeColor.RED, HyperLightingFabric.mainTab));
    public static final Block COLORED_SEALANTERN_BLACK = register("colored_sealantern_black", new ColoredSeaLantern("colored_sealantern_black", DyeColor.BLACK, HyperLightingFabric.mainTab));


    /**
     * Single Lights
     */
    public static final Block FENCE_SOLAR = register("fence_solar", new FenceSolar("fence_solar", DyeColor.WHITE, HyperLightingFabric.mainTab));
    public static final Block BATTERY_NEON = register("battery_neon", new BatteryNeon("battery_neon"));
    public static final Block UNDERWATER_TORCH = register("advanced_torch_underwater", new UnderwaterTorch("advanced_torch_underwater", HyperLightingFabric.mainTab));
    public static final Block UNDERWATER_LANTERN = register("advanced_lantern_underwater", new UnderwaterLantern("advanced_lantern_underwater", HyperLightingFabric.mainTab));
    public static final Block SUSPICIOUS_LAMP = register("suspicious_lamp", new SuspiciousLamp("suspicious_lamp", HyperLightingFabric.mainTab));
    public static final Block DANGER_LAMP = register("danger_lamp", new DangerLamp("danger_lamp", HyperLightingFabric.mainTab));
    public static final Block WALL_LIGHT = register("wall_light", new WallLight("wall_light", HyperLightingFabric.mainTab));
    public static final Block UNCLEAR_GLASS = register("unclear_glass", new UnclearGlass("unclear_glass", DyeColor.WHITE, HyperLightingFabric.mainTab));
    public static final Block LAVA_LAMP = register("lava_lamp", new LavaLamp("lava_lamp", HyperLightingFabric.mainTab));
    public static final Block CAMPFIRE = register("campfire", new CampFireBlock("campfire", DyeColor.ORANGE, HyperLightingFabric.mainTab));
    public static final Block CAMPFIRE_UNDERWATER = register("campfire_underwater", new CampFireUnderwater("campfire_underwater", DyeColor.BLUE, HyperLightingFabric.mainTab));
    public static final Block PUMPKIN_TRIO = register("pumpkin_trio", new PumpkinTrio("pumpkin_trio"));
    public static final Block PUMPKIN_TRIO_INVERTED = register("pumpkin_trio_inverted", new PumpkinTrio("pumpkin_trio_inverted"));
    public static final Block HANGING_FIRE = register("hanging_fire", new HangingFire("hanging_fire", DyeColor.ORANGE, HyperLightingFabric.mainTab));

    /**
     * MACHINES
     */
    public static final Block SOLAR_PANEL = register("solar_panel", new SolarPanel("solar_panel"));
    public static final Block SWITCHBOARD = register("switch_board", new SwitchBoard("switch_board"));

    /**
     * OTHER
     */
    public static final Block TIKI_BASE = register("tiki_base", new TikiTorchStick("tiki_base"));
    public static final Block FOG_MACHINE = register("fog_machine", new FogMachineBlock("fog_machine"));

    public HLBlocks() {

    }

    public static Block register(String name, Block block) {
        Block blk = Registry.register(Registry.BLOCK, new Identifier(ModConstants.MOD_ID, name), block);
        BLOCKS.add(blk);
        return blk;
    }

}
