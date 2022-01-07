package me.hypherionmc.hyperlighting.common.network.packets;

import me.hypherionmc.hyperlighting.common.tile.TileFogMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FogMachineAutoFireTimerPacket {

    private BlockPos tePos;
    private int fireTime;

    public FogMachineAutoFireTimerPacket(FriendlyByteBuf buf) {
        tePos = buf.readBlockPos();
        fireTime = buf.readInt();
    }

    public FogMachineAutoFireTimerPacket(BlockPos pos, int fireTime) {
        this.tePos = pos;
        this.fireTime = fireTime;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(tePos);
        buf.writeInt(fireTime);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
                if (ctx.get().getSender().getLevel().getBlockEntity(tePos) instanceof TileFogMachine tileFogMachine) {
                    tileFogMachine.setAutoFireTime(fireTime);
                }
            }
        });
        return true;
    }

}
