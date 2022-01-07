package me.hypherionmc.hyperlighting.common.network.packets;

import me.hypherionmc.hyperlighting.common.tile.TileFogMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FogMachineAutoFirePacket {

    private BlockPos tePos;
    private boolean doAutoFire;

    public FogMachineAutoFirePacket(FriendlyByteBuf buf) {
        tePos = buf.readBlockPos();
        doAutoFire = buf.readBoolean();
    }

    public FogMachineAutoFirePacket(BlockPos pos, boolean doAutoFire) {
        this.tePos = pos;
        this.doAutoFire = doAutoFire;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(tePos);
        buf.writeBoolean(doAutoFire);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
                if (ctx.get().getSender().getLevel().getBlockEntity(tePos) instanceof TileFogMachine tileFogMachine) {
                    tileFogMachine.setAutoFireEnabled(doAutoFire);
                }
            }
        });
        return true;
    }

}
