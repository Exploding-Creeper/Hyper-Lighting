package me.hypherionmc.hyperlighting.common.integration.top;

import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.ITheOneProbe;
import me.hypherionmc.hyperlighting.HyperLighting;
import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.common.integration.top.overrides.TOPBlockInfoProvider;
import me.hypherionmc.hyperlighting.common.integration.top.overrides.TOPCampfireInfoProvider;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Function;

public class TOPIntegration implements Function<ITheOneProbe, Void> {

    private static ITheOneProbe theOneProbe;

    public void setup() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::sendIMC);
    }

    public void sendIMC(InterModEnqueueEvent event) {
        HyperLighting.logger.info("Registering TOP integration");
        InterModComms.sendTo(ModConstants.THE_ONE_PROBE, "getTheOneProbe", TOPIntegration::new);
    }

    @Override
    public Void apply(ITheOneProbe theOneProbe) {
        TOPIntegration.theOneProbe = theOneProbe;

        TOPCampfireInfoProvider topCampfireInfoProvider = new TOPCampfireInfoProvider();
        theOneProbe.registerBlockDisplayOverride(topCampfireInfoProvider);

        TOPBlockInfoProvider blockInfoProvider = new TOPBlockInfoProvider();
        theOneProbe.registerBlockDisplayOverride(blockInfoProvider);
        return null;
    }

    public static IProbeConfig getProbeConfig() {
        return theOneProbe.createProbeConfig();
    }

}
