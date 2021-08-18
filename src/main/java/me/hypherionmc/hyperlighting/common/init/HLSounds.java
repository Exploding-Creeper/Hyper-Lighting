package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.hyperlighting.ModConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class HLSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ModConstants.MODID);

    public static final RegistryObject<SoundEvent> SABER_EQUIP = SOUNDS.register("item.saber_equip", () -> new SoundEvent(new ResourceLocation(ModConstants.MODID, "item.saber_equip")));
    public static final RegistryObject<SoundEvent> SABER_USE = SOUNDS.register("item.saber_use", () -> new SoundEvent(new ResourceLocation(ModConstants.MODID, "item.saber_use")));
    public static final RegistryObject<SoundEvent> SABER_HIT = SOUNDS.register("item.saber_hit", () -> new SoundEvent(new ResourceLocation(ModConstants.MODID, "item.saber_hit")));
}
