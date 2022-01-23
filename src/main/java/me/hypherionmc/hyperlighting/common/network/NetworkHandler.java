package me.hypherionmc.hyperlighting.common.network;

import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.api.RemoteSwitchable;
import me.hypherionmc.hyperlighting.common.blockentities.FogMachineBlockEntity;
import me.hypherionmc.hyperlighting.common.init.HLSounds;
import me.hypherionmc.hyperlighting.utils.ModUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NetworkHandler {

    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(new Identifier(ModConstants.MOD_ID, "statepacket"), (server, player, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            server.execute(() -> {
                World world = player.getWorld();
                BlockState state = world.getBlockState(pos);
                if (!(state.getBlock() instanceof RemoteSwitchable rs))
                    return;

                BlockState newState = rs.remoteSwitched(state, pos, world);
                world.setBlockState(pos, newState, 3);
                world.updateListeners(pos, state, newState, 3);
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(ModConstants.MOD_ID, "opengui"), (server, player, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            server.execute(() -> {
                BlockEntity blockEntity = player.world.getBlockEntity(pos);
                NamedScreenHandlerFactory screenHandlerFactory = blockEntity instanceof NamedScreenHandlerFactory ? (NamedScreenHandlerFactory) blockEntity : null;
                if (screenHandlerFactory != null) {
                    player.openHandledScreen(screenHandlerFactory);
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(ModConstants.MOD_ID, "saber"), (server, player, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            server.execute(() -> {
                if (!player.world.isClient) {
                    player.world.playSound(null, pos, HLSounds.SABER_USE, SoundCategory.VOICE, 0.4f, ModUtils.floatInRange(0.8f, 1.0f));
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(ModConstants.MOD_ID, "fogpacket"), (server, player, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            FogMachinePacketType type = FogMachinePacketType.values()[buf.readInt()];
            int timer = buf.readInt();
            server.execute(() -> {
                if (!player.world.isClient && player.world.getBlockEntity(pos) instanceof FogMachineBlockEntity fogMachine) {
                    switch (type) {
                        case FIRE -> fogMachine.setFireMachine(true);
                        case AUTO_FIRE -> fogMachine.setAutoFireEnabled(!fogMachine.autoFireEnabled);
                        case AUTO_FIRE_TIMER -> fogMachine.setAutoFireTime(timer);
                    }
                }
            });
        });
    }

    public static void sendStateTogglePacket(BlockPos pos) {
        PacketByteBuf byteBuf = PacketByteBufs.create();
        ClientPlayNetworking.send(new Identifier(ModConstants.MOD_ID, "statepacket"), byteBuf.writeBlockPos(pos));
    }

    public static void sendOpenGuiPacket(BlockPos pos) {
        PacketByteBuf byteBuf = PacketByteBufs.create();
        ClientPlayNetworking.send(new Identifier(ModConstants.MOD_ID, "opengui"), byteBuf.writeBlockPos(pos));
    }

    public static void sendFogMachinePacket(BlockPos pos, int time, FogMachinePacketType type) {
        PacketByteBuf byteBuf = PacketByteBufs.create();
        byteBuf.writeBlockPos(pos);
        byteBuf.writeInt(type.ordinal());
        byteBuf.writeInt(time);
        ClientPlayNetworking.send(new Identifier(ModConstants.MOD_ID, "fogpacket"), byteBuf);
    }

    // SERIOUSLY? There has to be a better way of doing this
    public static void sendLightSaberPacket(BlockPos pos) {
        PacketByteBuf byteBuf = PacketByteBufs.create();
        ClientPlayNetworking.send(new Identifier(ModConstants.MOD_ID, "saber"), byteBuf.writeBlockPos(pos));
    }

}
