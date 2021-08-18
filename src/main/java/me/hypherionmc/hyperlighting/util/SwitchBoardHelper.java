package me.hypherionmc.hyperlighting.util;


import net.minecraft.world.phys.Vec2;

public class SwitchBoardHelper {

    private final Vec2 barPos;
    private final Vec2 statepos;
    private final Vec2 chargePos;
    private final Vec2 buttonPos;

    public SwitchBoardHelper(Vec2 barPos, Vec2 statepos, Vec2 chargePos, Vec2 buttonPos) {
        this.barPos = barPos;
        this.statepos = statepos;
        this.chargePos = chargePos;
        this.buttonPos = buttonPos;
    }

    public Vec2 getBarPos() {
        return barPos;
    }

    public Vec2 getButtonPos() {
        return buttonPos;
    }

    public Vec2 getChargePos() {
        return chargePos;
    }

    public Vec2 getStatepos() {
        return statepos;
    }
}
