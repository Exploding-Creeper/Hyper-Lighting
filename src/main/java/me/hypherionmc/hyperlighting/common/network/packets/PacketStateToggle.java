package me.hypherionmc.hyperlighting.common.network.packets;

import me.hypherionmc.hyperlighting.api.RemoteSwitchable;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketStateToggle {

    private BlockPos posToSet;

    public PacketStateToggle(PacketBuffer buffer) {
        posToSet = buffer.readBlockPos();
    }

    public PacketStateToggle(BlockPos pos) {
        this.posToSet = pos;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(posToSet);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
                BlockState te = ctx.get().getSender().getServerWorld().getBlockState(posToSet);
                BlockState oldstate = te;
                if (!(te.getBlock() instanceof RemoteSwitchable))
                    return;
                BlockState newState = ((RemoteSwitchable)te.getBlock()).remoteSwitched(te, posToSet, ctx.get().getSender().world);
                ctx.get().getSender().getServerWorld().setBlockState(posToSet, newState, 3);
                ctx.get().getSender().getServerWorld().notifyBlockUpdate(posToSet, oldstate, newState, 3);
            }
        });
        return true;
    }
}