package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.common.blocks.BatteryNeon;
import me.hypherionmc.hyperlighting.common.tile.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class HLTileEntities {

    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ModConstants.MODID);

    public static final RegistryObject<TileEntityType<TileSolarLight>> TILE_SOLAR_LIGHT = TILES.register("tilesolarlight", () -> TileEntityType.Builder.create(TileSolarLight::new, HLBlocks.FENCE_SOLAR.get()).build(null));
    public static final RegistryObject<TileEntityType<TileSolarPanel>> TILE_SOLAR_PANEL = TILES.register("tilesolarpanel", () -> TileEntityType.Builder.create(TileSolarPanel::new, HLBlocks.SOLAR_PANEL.get()).build(null));
    public static final RegistryObject<TileEntityType<TileBatteryNeon>> TILE_BATTERY_NEON = TILES.register("tilebatteryneon", () -> TileEntityType.Builder.create(TileBatteryNeon::new, HLBlocks.BATTERY_NEON.get()).build(null));
    public static final RegistryObject<TileEntityType<TileSwitchBoard>> TILE_SWITCHBOARD = TILES.register("tileswitchboard", () -> TileEntityType.Builder.create(TileSwitchBoard::new, HLBlocks.SWITCHBOARD.get()).build(null));

    public static final RegistryObject<TileEntityType<TileCampFire>> TILE_CAMPFIRE = TILES.register("tilecampfire", () -> TileEntityType.Builder.create(TileCampFire::new, HLBlocks.CAMPFIRE.get(), HLBlocks.CAMPFIRE_UNDERWATER.get()).build(null));

}
