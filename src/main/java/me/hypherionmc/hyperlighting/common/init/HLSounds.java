package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.hyperlighting.ModConstants;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class HLSounds {

    public static final SoundEvent SABER_EQUIP = register("item.saber_equip", new SoundEvent(new Identifier(ModConstants.MOD_ID, "item.saber_equip")));
    public static final SoundEvent SABER_USE = register("item.saber_use", new SoundEvent(new Identifier(ModConstants.MOD_ID, "item.saber_use")));
    public static final SoundEvent SABER_HIT = register("item.saber_hit", new SoundEvent(new Identifier(ModConstants.MOD_ID, "item.saber_hit")));

    public static SoundEvent register(String name, SoundEvent event) {
        return Registry.register(Registry.SOUND_EVENT, new Identifier(ModConstants.MOD_ID, name), event);
    }

}
