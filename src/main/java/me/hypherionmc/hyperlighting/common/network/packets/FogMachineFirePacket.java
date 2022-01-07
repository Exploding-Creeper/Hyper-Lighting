package me.hypherionmc.hyperlighting.common.network.packets;

import me.hypherionmc.hyperlighting.common.tile.TileFogMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FogMachineFirePacket {

    private BlockPos tePos;

    public FogMachineFirePacket(FriendlyByteBuf buf) {
        tePos = buf.readBlockPos();
    }

    public FogMachineFirePacket(BlockPos pos) {
        this.tePos = pos;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(tePos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
                if (ctx.get().getSender().getLevel().getBlockEntity(tePos) instanceof TileFogMachine tileFogMachine) {
                    tileFogMachine.setFireMachine(!tileFogMachine.fireMachine);
                }
            }
        });
        return true;
    }

}
