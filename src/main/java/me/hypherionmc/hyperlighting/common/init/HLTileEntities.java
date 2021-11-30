package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.common.tile.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class HLTileEntities {

    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ModConstants.MODID);

    public static final RegistryObject<BlockEntityType<TileSolarLight>> TILE_SOLAR_LIGHT = TILES.register("tilesolarlight", () -> BlockEntityType.Builder.of(TileSolarLight::new, HLBlocks.FENCE_SOLAR.get()).build(null));
    public static final RegistryObject<BlockEntityType<TileSolarPanel>> TILE_SOLAR_PANEL = TILES.register("tilesolarpanel", () -> BlockEntityType.Builder.of(TileSolarPanel::new, HLBlocks.SOLAR_PANEL.get()).build(null));
    public static final RegistryObject<BlockEntityType<TileBatteryNeon>> TILE_BATTERY_NEON = TILES.register("tilebatteryneon", () -> BlockEntityType.Builder.of(TileBatteryNeon::new, HLBlocks.BATTERY_NEON.get()).build(null));
    public static final RegistryObject<BlockEntityType<TileSwitchBoard>> TILE_SWITCHBOARD = TILES.register("tileswitchboard", () -> BlockEntityType.Builder.of(TileSwitchBoard::new, HLBlocks.SWITCHBOARD.get()).build(null));

    public static final RegistryObject<BlockEntityType<TileCampFire>> TILE_CAMPFIRE = TILES.register("tilecampfire", () -> BlockEntityType.Builder.of(TileCampFire::new, HLBlocks.CAMPFIRE.get(), HLBlocks.CAMPFIRE_UNDERWATER.get()).build(null));

}
