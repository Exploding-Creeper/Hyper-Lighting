package me.hypherionmc.hyperlighting.common.network;

import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.common.network.packets.OpenGUIPacket;
import me.hypherionmc.hyperlighting.common.network.packets.PacketStateToggle;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ModConstants.MODID, "main"),
            () -> "1",
            "1"::equals,
            "1"::equals
    );
    private static int ID = 0;

    /**
     * Get the next id
     *
     * @return The next id
     */
    private static int nextID() {
        return ID++;
    }

    public static void registerMessages() {

        // Server Packets
        INSTANCE.messageBuilder(OpenGUIPacket.class, nextID()).encoder(OpenGUIPacket::toBytes).decoder(OpenGUIPacket::new).consumer(OpenGUIPacket::handle).add();
        INSTANCE.messageBuilder(PacketStateToggle.class, nextID()).encoder(PacketStateToggle::toBytes).decoder(PacketStateToggle::new).consumer(PacketStateToggle::handle).add();

    }

    public static void sendToClient(Object packet, ServerPlayer playerEntity) {
        INSTANCE.sendTo(packet, playerEntity.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }

    public static void sendToAll(Object message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }

}
