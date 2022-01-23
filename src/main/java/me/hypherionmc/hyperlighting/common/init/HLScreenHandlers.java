package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.client.gui.BatteryNeonScreen;
import me.hypherionmc.hyperlighting.client.gui.FogMachineScreen;
import me.hypherionmc.hyperlighting.client.gui.SwitchBoardScreen;
import me.hypherionmc.hyperlighting.common.handlers.screen.BatteryNeonScreenHandler;
import me.hypherionmc.hyperlighting.common.handlers.screen.FogMachineScreenHandler;
import me.hypherionmc.hyperlighting.common.handlers.screen.SwitchBoardScreenHandler;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class HLScreenHandlers {

    public static ScreenHandlerType<SwitchBoardScreenHandler> SWITCH_BOARD_HANDLER;
    public static ScreenHandlerType<BatteryNeonScreenHandler> BATTERY_NEON_HANDLER;
    public static ScreenHandlerType<FogMachineScreenHandler> FOG_MACHINE_HANDLER;

    public static void registerServer() {
        SWITCH_BOARD_HANDLER = ScreenHandlerRegistry.registerExtended(new Identifier(ModConstants.MOD_ID, "switch_board"), SwitchBoardScreenHandler::new);
        BATTERY_NEON_HANDLER = ScreenHandlerRegistry.registerExtended(new Identifier(ModConstants.MOD_ID, "battery_neon"), BatteryNeonScreenHandler::new);
        FOG_MACHINE_HANDLER = ScreenHandlerRegistry.registerExtended(new Identifier(ModConstants.MOD_ID, "fog_machine"), FogMachineScreenHandler::new);
    }

    public static void registerClient() {
        ScreenRegistry.register(SWITCH_BOARD_HANDLER, SwitchBoardScreen::new);
        ScreenRegistry.register(BATTERY_NEON_HANDLER, BatteryNeonScreen::new);
        ScreenRegistry.register(FOG_MACHINE_HANDLER, FogMachineScreen::new);
    }

}
