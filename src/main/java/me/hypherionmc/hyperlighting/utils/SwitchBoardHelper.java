package me.hypherionmc.hyperlighting.utils;

import net.minecraft.client.util.math.Vector2f;

public class SwitchBoardHelper {

    private final Vector2f barPos;
    private final Vector2f statepos;
    private final Vector2f chargePos;
    private final Vector2f buttonPos;

    public SwitchBoardHelper(Vector2f barPos, Vector2f statepos, Vector2f chargePos, Vector2f buttonPos) {
        this.barPos = barPos;
        this.statepos = statepos;
        this.chargePos = chargePos;
        this.buttonPos = buttonPos;
    }

    public Vector2f getBarPos() {
        return barPos;
    }

    public Vector2f getButtonPos() {
        return buttonPos;
    }

    public Vector2f getChargePos() {
        return chargePos;
    }

    public Vector2f getStatepos() {
        return statepos;
    }
}
