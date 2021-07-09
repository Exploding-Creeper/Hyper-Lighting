package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.hyperlighting.HyperLighting;
import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.common.blocks.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.DyeColor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class HLBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ModConstants.MODID);
    /***
     * TORCHES
     */
    public static final RegistryObject<Block> ADVANCED_TORCH_ORANGE = BLOCKS.register("advanced_torch_orange", () -> new AdvancedTorchBlock("advanced_torch_orange", DyeColor.ORANGE, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_TORCH_BLUE = BLOCKS.register("advanced_torch_blue", () -> new AdvancedTorchBlock("advanced_torch_blue", DyeColor.BLUE, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_TORCH_RED = BLOCKS.register("advanced_torch_red", () -> new AdvancedTorchBlock("advanced_torch_red", DyeColor.RED, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_TORCH_PURPLE = BLOCKS.register("advanced_torch_purple", () -> new AdvancedTorchBlock("advanced_torch_purple", DyeColor.PURPLE, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_TORCH_MAGENTA = BLOCKS.register("advanced_torch_magenta", () -> new AdvancedTorchBlock("advanced_torch_magenta", DyeColor.MAGENTA, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_TORCH_GREEN = BLOCKS.register("advanced_torch_green", () -> new AdvancedTorchBlock("advanced_torch_green", DyeColor.GREEN, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_TORCH_LIME = BLOCKS.register("advanced_torch_lime", () -> new AdvancedTorchBlock("advanced_torch_lime", DyeColor.LIME, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_TORCH_CYAN = BLOCKS.register("advanced_torch_cyan", () -> new AdvancedTorchBlock("advanced_torch_cyan", DyeColor.CYAN, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_TORCH_YELLOW = BLOCKS.register("advanced_torch_yellow", () -> new AdvancedTorchBlock("advanced_torch_yellow", DyeColor.YELLOW, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_TORCH_LIGHT_GRAY = BLOCKS.register("advanced_torch_light_gray", () -> new AdvancedTorchBlock("advanced_torch_light_gray", DyeColor.LIGHT_GRAY, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_TORCH_GRAY = BLOCKS.register("advanced_torch_gray", () -> new AdvancedTorchBlock("advanced_torch_gray", DyeColor.GRAY, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_TORCH_WHITE = BLOCKS.register("advanced_torch_white", () -> new AdvancedTorchBlock("advanced_torch_white", DyeColor.WHITE, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_TORCH_BLACK = BLOCKS.register("advanced_torch_black", () -> new AdvancedTorchBlock("advanced_torch_black", DyeColor.BLACK, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_TORCH_LIGHT_BLUE = BLOCKS.register("advanced_torch_light_blue", () -> new AdvancedTorchBlock("advanced_torch_light_blue", DyeColor.LIGHT_BLUE, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_TORCH_BROWN = BLOCKS.register("advanced_torch_brown", () -> new AdvancedTorchBlock("advanced_torch_brown", DyeColor.BROWN, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_TORCH_PINK = BLOCKS.register("advanced_torch_pink", () -> new AdvancedTorchBlock("advanced_torch_pink", DyeColor.PINK, HyperLighting.mainTab));

    /**
     * LANTERNS
     */
    public static final RegistryObject<Block> ADVANCED_LANTERN_WHITE = BLOCKS.register("advanced_lantern_white", () -> new AdvancedLantern("advanced_lantern_white", DyeColor.WHITE, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_LANTERN_ORANGE = BLOCKS.register("advanced_lantern_orange", () -> new AdvancedLantern("advanced_lantern_orange", DyeColor.ORANGE, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_LANTERN_MAGENTA = BLOCKS.register("advanced_lantern_magenta", () -> new AdvancedLantern("advanced_lantern_magenta", DyeColor.MAGENTA, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_LANTERN_LIGHT_BLUE = BLOCKS.register("advanced_lantern_light_blue", () -> new AdvancedLantern("advanced_lantern_light_blue", DyeColor.LIGHT_BLUE, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_LANTERN_YELLOW = BLOCKS.register("advanced_lantern_yellow", () -> new AdvancedLantern("advanced_lantern_yellow", DyeColor.YELLOW, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_LANTERN_LIME = BLOCKS.register("advanced_lantern_lime", () -> new AdvancedLantern("advanced_lantern_lime", DyeColor.LIME, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_LANTERN_PINK = BLOCKS.register("advanced_lantern_pink", () -> new AdvancedLantern("advanced_lantern_pink", DyeColor.PINK, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_LANTERN_GRAY = BLOCKS.register("advanced_lantern_gray", () -> new AdvancedLantern("advanced_lantern_gray", DyeColor.GRAY, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_LANTERN_LIGHT_GRAY = BLOCKS.register("advanced_lantern_light_gray", () -> new AdvancedLantern("advanced_lantern_light_gray", DyeColor.LIGHT_GRAY, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_LANTERN_CYAN = BLOCKS.register("advanced_lantern_cyan", () -> new AdvancedLantern("advanced_lantern_cyan", DyeColor.CYAN, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_LANTERN_PURPLE = BLOCKS.register("advanced_lantern_purple", () -> new AdvancedLantern("advanced_lantern_purple", DyeColor.PURPLE, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_LANTERN_BLUE = BLOCKS.register("advanced_lantern_blue", () -> new AdvancedLantern("advanced_lantern_blue", DyeColor.BLUE, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_LANTERN_BROWN = BLOCKS.register("advanced_lantern_brown", () -> new AdvancedLantern("advanced_lantern_brown", DyeColor.BROWN, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_LANTERN_GREEN = BLOCKS.register("advanced_lantern_green", () -> new AdvancedLantern("advanced_lantern_green", DyeColor.GREEN, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_LANTERN_RED = BLOCKS.register("advanced_lantern_red", () -> new AdvancedLantern("advanced_lantern_red", DyeColor.RED, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_LANTERN_BLACK = BLOCKS.register("advanced_lantern_black", () -> new AdvancedLantern("advanced_lantern_black", DyeColor.BLACK, HyperLighting.mainTab));


    /**
     * CANDLES
     */
    public static final RegistryObject<Block> ADVANCED_CANDLE_WHITE = BLOCKS.register("advanced_candle_white", () -> new AdvancedCandle("advanced_candle_white", DyeColor.WHITE, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_CANDLE_ORANGE = BLOCKS.register("advanced_candle_orange", () -> new AdvancedCandle("advanced_candle_orange", DyeColor.ORANGE, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_CANDLE_MAGENTA = BLOCKS.register("advanced_candle_magenta", () -> new AdvancedCandle("advanced_candle_magenta", DyeColor.MAGENTA, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_CANDLE_LIGHT_BLUE = BLOCKS.register("advanced_candle_light_blue", () -> new AdvancedCandle("advanced_candle_light_blue", DyeColor.LIGHT_BLUE, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_CANDLE_YELLOW = BLOCKS.register("advanced_candle_yellow", () -> new AdvancedCandle("advanced_candle_yellow", DyeColor.YELLOW, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_CANDLE_LIME = BLOCKS.register("advanced_candle_lime", () -> new AdvancedCandle("advanced_candle_lime", DyeColor.LIME, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_CANDLE_PINK = BLOCKS.register("advanced_candle_pink", () -> new AdvancedCandle("advanced_candle_pink", DyeColor.PINK, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_CANDLE_GRAY = BLOCKS.register("advanced_candle_gray", () -> new AdvancedCandle("advanced_candle_gray", DyeColor.GRAY, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_CANDLE_LIGHT_GRAY = BLOCKS.register("advanced_candle_light_gray", () -> new AdvancedCandle("advanced_candle_light_gray", DyeColor.LIGHT_GRAY, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_CANDLE_CYAN = BLOCKS.register("advanced_candle_cyan", () -> new AdvancedCandle("advanced_candle_cyan", DyeColor.CYAN, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_CANDLE_PURPLE = BLOCKS.register("advanced_candle_purple", () -> new AdvancedCandle("advanced_candle_purple", DyeColor.PURPLE, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_CANDLE_BLUE = BLOCKS.register("advanced_candle_blue", () -> new AdvancedCandle("advanced_candle_blue", DyeColor.BLUE, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_CANDLE_BROWN = BLOCKS.register("advanced_candle_brown", () -> new AdvancedCandle("advanced_candle_brown", DyeColor.BROWN, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_CANDLE_GREEN = BLOCKS.register("advanced_candle_green", () -> new AdvancedCandle("advanced_candle_green", DyeColor.GREEN, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_CANDLE_RED = BLOCKS.register("advanced_candle_red", () -> new AdvancedCandle("advanced_candle_red", DyeColor.RED, HyperLighting.mainTab));
    public static final RegistryObject<Block> ADVANCED_CANDLE_BLACK = BLOCKS.register("advanced_candle_black", () -> new AdvancedCandle("advanced_candle_black", DyeColor.BLACK, HyperLighting.mainTab));

    /**
     * TIKI TORCHES
     */
    public static final RegistryObject<Block> TIKI_TORCH_WHITE = BLOCKS.register("tiki_torch_white", () -> new TikiTorch("tiki_torch_white", DyeColor.WHITE, HyperLighting.mainTab));
    public static final RegistryObject<Block> TIKI_TORCH_ORANGE = BLOCKS.register("tiki_torch_orange", () -> new TikiTorch("tiki_torch_orange", DyeColor.ORANGE, HyperLighting.mainTab));
    public static final RegistryObject<Block> TIKI_TORCH_MAGENTA = BLOCKS.register("tiki_torch_magenta", () -> new TikiTorch("tiki_torch_magenta", DyeColor.MAGENTA, HyperLighting.mainTab));
    public static final RegistryObject<Block> TIKI_TORCH_LIGHT_BLUE = BLOCKS.register("tiki_torch_light_blue", () -> new TikiTorch("tiki_torch_light_blue", DyeColor.LIGHT_BLUE, HyperLighting.mainTab));
    public static final RegistryObject<Block> TIKI_TORCH_YELLOW = BLOCKS.register("tiki_torch_yellow", () -> new TikiTorch("tiki_torch_yellow", DyeColor.YELLOW, HyperLighting.mainTab));
    public static final RegistryObject<Block> TIKI_TORCH_LIME = BLOCKS.register("tiki_torch_lime", () -> new TikiTorch("tiki_torch_lime", DyeColor.LIME, HyperLighting.mainTab));
    public static final RegistryObject<Block> TIKI_TORCH_PINK = BLOCKS.register("tiki_torch_pink", () -> new TikiTorch("tiki_torch_pink", DyeColor.PINK, HyperLighting.mainTab));
    public static final RegistryObject<Block> TIKI_TORCH_GRAY = BLOCKS.register("tiki_torch_gray", () -> new TikiTorch("tiki_torch_gray", DyeColor.GRAY, HyperLighting.mainTab));
    public static final RegistryObject<Block> TIKI_TORCH_LIGHT_GRAY = BLOCKS.register("tiki_torch_light_gray", () -> new TikiTorch("tiki_torch_light_gray", DyeColor.LIGHT_GRAY, HyperLighting.mainTab));
    public static final RegistryObject<Block> TIKI_TORCH_CYAN = BLOCKS.register("tiki_torch_cyan", () -> new TikiTorch("tiki_torch_cyan", DyeColor.CYAN, HyperLighting.mainTab));
    public static final RegistryObject<Block> TIKI_TORCH_PURPLE = BLOCKS.register("tiki_torch_purple", () -> new TikiTorch("tiki_torch_purple", DyeColor.PURPLE, HyperLighting.mainTab));
    public static final RegistryObject<Block> TIKI_TORCH_BLUE = BLOCKS.register("tiki_torch_blue", () -> new TikiTorch("tiki_torch_blue", DyeColor.BLUE, HyperLighting.mainTab));
    public static final RegistryObject<Block> TIKI_TORCH_BROWN = BLOCKS.register("tiki_torch_brown", () -> new TikiTorch("tiki_torch_brown", DyeColor.BROWN, HyperLighting.mainTab));
    public static final RegistryObject<Block> TIKI_TORCH_GREEN = BLOCKS.register("tiki_torch_green", () -> new TikiTorch("tiki_torch_green", DyeColor.GREEN, HyperLighting.mainTab));
    public static final RegistryObject<Block> TIKI_TORCH_RED = BLOCKS.register("tiki_torch_red", () -> new TikiTorch("tiki_torch_red", DyeColor.RED, HyperLighting.mainTab));
    public static final RegistryObject<Block> TIKI_TORCH_BLACK = BLOCKS.register("tiki_torch_black", () -> new TikiTorch("tiki_torch_black", DyeColor.BLACK, HyperLighting.mainTab));

    /**
     * Redstone Lamps
     */
    public static final RegistryObject<Block> COLORED_REDSTONE_WHITE = BLOCKS.register("colored_redstone_white", () -> new ColoredRedstone("colored_redstone_white", DyeColor.WHITE, HyperLighting.mainTab));
    public static final RegistryObject<Block> COLORED_REDSTONE_ORANGE = BLOCKS.register("colored_redstone_orange", () -> new ColoredRedstone("colored_redstone_orange", DyeColor.ORANGE, HyperLighting.mainTab));
    public static final RegistryObject<Block> COLORED_REDSTONE_MAGENTA = BLOCKS.register("colored_redstone_magenta", () -> new ColoredRedstone("colored_redstone_magenta", DyeColor.MAGENTA, HyperLighting.mainTab));
    public static final RegistryObject<Block> COLORED_REDSTONE_LIGHT_BLUE = BLOCKS.register("colored_redstone_light_blue", () -> new ColoredRedstone("colored_redstone_light_blue", DyeColor.LIGHT_BLUE, HyperLighting.mainTab));
    public static final RegistryObject<Block> COLORED_REDSTONE_YELLOW = BLOCKS.register("colored_redstone_yellow", () -> new ColoredRedstone("colored_redstone_yellow", DyeColor.YELLOW, HyperLighting.mainTab));
    public static final RegistryObject<Block> COLORED_REDSTONE_LIME = BLOCKS.register("colored_redstone_lime", () -> new ColoredRedstone("colored_redstone_lime", DyeColor.LIME, HyperLighting.mainTab));
    public static final RegistryObject<Block> COLORED_REDSTONE_PINK = BLOCKS.register("colored_redstone_pink", () -> new ColoredRedstone("colored_redstone_pink", DyeColor.PINK, HyperLighting.mainTab));
    public static final RegistryObject<Block> COLORED_REDSTONE_GRAY = BLOCKS.register("colored_redstone_gray", () -> new ColoredRedstone("colored_redstone_gray", DyeColor.GRAY, HyperLighting.mainTab));
    public static final RegistryObject<Block> COLORED_REDSTONE_LIGHT_GRAY = BLOCKS.register("colored_redstone_light_gray", () -> new ColoredRedstone("colored_redstone_light_gray", DyeColor.LIGHT_GRAY, HyperLighting.mainTab));
    public static final RegistryObject<Block> COLORED_REDSTONE_CYAN = BLOCKS.register("colored_redstone_cyan", () -> new ColoredRedstone("colored_redstone_cyan", DyeColor.CYAN, HyperLighting.mainTab));
    public static final RegistryObject<Block> COLORED_REDSTONE_PURPLE = BLOCKS.register("colored_redstone_purple", () -> new ColoredRedstone("colored_redstone_purple", DyeColor.PURPLE, HyperLighting.mainTab));
    public static final RegistryObject<Block> COLORED_REDSTONE_BLUE = BLOCKS.register("colored_redstone_blue", () -> new ColoredRedstone("colored_redstone_blue", DyeColor.BLUE, HyperLighting.mainTab));
    public static final RegistryObject<Block> COLORED_REDSTONE_BROWN = BLOCKS.register("colored_redstone_brown", () -> new ColoredRedstone("colored_redstone_brown", DyeColor.BROWN, HyperLighting.mainTab));
    public static final RegistryObject<Block> COLORED_REDSTONE_GREEN = BLOCKS.register("colored_redstone_green", () -> new ColoredRedstone("colored_redstone_green", DyeColor.GREEN, HyperLighting.mainTab));
    public static final RegistryObject<Block> COLORED_REDSTONE_RED = BLOCKS.register("colored_redstone_red", () -> new ColoredRedstone("colored_redstone_red", DyeColor.RED, HyperLighting.mainTab));
    public static final RegistryObject<Block> COLORED_REDSTONE_BLACK = BLOCKS.register("colored_redstone_black", () -> new ColoredRedstone("colored_redstone_black", DyeColor.BLACK, HyperLighting.mainTab));


    /**
     * Single Lights
     */
    public static final RegistryObject<Block> FENCE_SOLAR = BLOCKS.register("fence_solar", () -> new FenceSolar("fence_solar", DyeColor.WHITE, HyperLighting.mainTab));
    public static final RegistryObject<Block> BATTERY_NEON = BLOCKS.register("battery_neon", () -> new BatteryNeon("battery_neon"));
    public static final RegistryObject<Block> UNDERWATER_TORCH = BLOCKS.register("advanced_torch_underwater", () -> new UnderwaterTorch("advanced_torch_underwater", HyperLighting.mainTab));
    public static final RegistryObject<Block> UNDERWATER_LANTERN = BLOCKS.register("advanced_lantern_underwater", () -> new UnderwaterLantern("advanced_lantern_underwater", HyperLighting.mainTab));
    public static final RegistryObject<Block> SUSPICIOUS_LAMP = BLOCKS.register("suspicious_lamp", () -> new SuspiciousLamp("suspicious_lamp", HyperLighting.mainTab));
    public static final RegistryObject<Block> DANGER_LAMP = BLOCKS.register("danger_lamp", () -> new DangerLamp("danger_lamp", HyperLighting.mainTab));
    public static final RegistryObject<Block> WALL_LIGHT = BLOCKS.register("wall_light", () -> new WallLight("wall_light", HyperLighting.mainTab));
    public static final RegistryObject<Block> UNCLEAR_GLASS = BLOCKS.register("unclear_glass", () -> new UnclearGlass("unclear_glass", HyperLighting.mainTab));
    public static final RegistryObject<Block> LAVA_LAMP = BLOCKS.register("lava_lamp", () -> new LavaLamp("lava_lamp", HyperLighting.mainTab));
    public static final RegistryObject<Block> CAMPFIRE = BLOCKS.register("campfire", () -> new CampFireBlock("campfire", DyeColor.ORANGE, HyperLighting.mainTab));
    public static final RegistryObject<Block> CAMPFIRE_UNDERWATER = BLOCKS.register("campfire_underwater", () -> new CampFireUnderwater("campfire_underwater", DyeColor.BLUE, HyperLighting.mainTab));
    public static final RegistryObject<Block> PUMPKIN_TRIO = BLOCKS.register("pumpkin_trio", () -> new PumpkinTrio("pumpkin_trio"));
    public static final RegistryObject<Block> PUMPKIN_TRIO_INVERTED = BLOCKS.register("pumpkin_trio_inverted", () -> new PumpkinTrio("pumpkin_trio_inverted"));

    /**
     * MACHINES
     */
    public static final RegistryObject<Block> SOLAR_PANEL = BLOCKS.register("solar_panel", () -> new SolarPanel("solar_panel"));
    public static final RegistryObject<Block> SWITCHBOARD = BLOCKS.register("switch_board", () -> new SwitchBoard("switch_board"));

    /**
     * OTHER
     */
    public static final RegistryObject<Block> TIKI_BASE = BLOCKS.register("tiki_base", () -> new TikiTorchStick("tiki_base"));
}
