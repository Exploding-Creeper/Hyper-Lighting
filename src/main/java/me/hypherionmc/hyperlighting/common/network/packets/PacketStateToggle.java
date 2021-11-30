package me.hypherionmc.hyperlighting.common.network.packets;

import me.hypherionmc.hyperlighting.api.RemoteSwitchable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketStateToggle {

    private BlockPos posToSet;

    public PacketStateToggle(FriendlyByteBuf buffer) {
        posToSet = buffer.readBlockPos();
    }

    public PacketStateToggle(BlockPos pos) {
        this.posToSet = pos;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(posToSet);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
                BlockState te = ctx.get().getSender().getLevel().getBlockState(posToSet);
                BlockState oldstate = te;
                if (!(te.getBlock() instanceof RemoteSwitchable))
                    return;
                BlockState newState = ((RemoteSwitchable)te.getBlock()).remoteSwitched(te, posToSet, ctx.get().getSender().level);
                ctx.get().getSender().getLevel().setBlock(posToSet, newState, 3);
                ctx.get().getSender().getLevel().sendBlockUpdated(posToSet, oldstate, newState, 3);
            }
        });
        return true;
    }
}
