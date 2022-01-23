package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.common.blockentities.*;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class HLBlockEntities {

    public static BlockEntityType<CampFireBlockEntity> TILE_CAMPFIRE;
    public static BlockEntityType<FenceSolarBlockEntity> TILE_FENCE_SOLAR;
    public static BlockEntityType<SolarPanelBlockEntity> TILE_SOLAR_PANEL;
    public static BlockEntityType<SwitchBoardBlockEntity> TILE_SWITCH_BOARD;
    public static BlockEntityType<BatteryNeonBlockEntity> TILE_BATTERY_NEON;
    public static BlockEntityType<FogMachineBlockEntity> TILE_FOG_MACHINE;

    public static void register() {
        TILE_CAMPFIRE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(ModConstants.MOD_ID, "campfire"), FabricBlockEntityTypeBuilder.create(CampFireBlockEntity::new, HLBlocks.CAMPFIRE, HLBlocks.CAMPFIRE_UNDERWATER).build());
        TILE_FENCE_SOLAR = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(ModConstants.MOD_ID, "fence_solar"), FabricBlockEntityTypeBuilder.create(FenceSolarBlockEntity::new, HLBlocks.FENCE_SOLAR).build());
        TILE_SOLAR_PANEL = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(ModConstants.MOD_ID, "solar_panel"), FabricBlockEntityTypeBuilder.create(SolarPanelBlockEntity::new, HLBlocks.SOLAR_PANEL).build());
        TILE_SWITCH_BOARD = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(ModConstants.MOD_ID, "switch_board"), FabricBlockEntityTypeBuilder.create(SwitchBoardBlockEntity::new, HLBlocks.SWITCHBOARD).build());
        TILE_BATTERY_NEON = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(ModConstants.MOD_ID, "battery_neon"), FabricBlockEntityTypeBuilder.create(BatteryNeonBlockEntity::new, HLBlocks.BATTERY_NEON).build());
        TILE_FOG_MACHINE = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(ModConstants.MOD_ID, "fog_machine"), FabricBlockEntityTypeBuilder.create(FogMachineBlockEntity::new, HLBlocks.FOG_MACHINE).build());
    }

}
