package me.hypherionmc.hyperlighting.common.network.packets;

import me.hypherionmc.hyperlighting.common.containers.ContainerBatteryNeon;
import me.hypherionmc.hyperlighting.common.tile.TileBatteryNeon;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.function.Supplier;

public class OpenGUIPacket {

    private BlockPos posToSet;
    private int guiid;

    public OpenGUIPacket(PacketBuffer buffer) {
        posToSet = buffer.readBlockPos();
        guiid = buffer.readInt();
    }

    public OpenGUIPacket(int guiid, BlockPos pos) {
        this.guiid = guiid;
        this.posToSet = pos;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(this.posToSet);
        buf.writeInt(this.guiid);

    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            TileEntity te = ctx.get().getSender().getServerWorld().getTileEntity(this.posToSet);

            INamedContainerProvider containerProvider = new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    if (te instanceof TileBatteryNeon) {
                        return new TranslationTextComponent("container.batteryneon");
                    }
                    /*if (te instanceof TileNeonSign) {
                        return new TranslationTextComponent("container.neonsign");
                    }*/
                    return null;
                }

                @Override
                public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                    if (te instanceof TileBatteryNeon) {
                        return new ContainerBatteryNeon(i, te.getWorld(), posToSet, playerInventory, playerEntity);
                    }
                    /*if (te instanceof TileNeonSign) {
                        return new ContainerNeonSign(i, te.getWorld(), posToSet, playerInventory, playerEntity);
                    }*/
                    return null;
                }
            };

            if (containerProvider.getDisplayName() != null) {
                NetworkHooks.openGui(ctx.get().getSender(), containerProvider, te.getPos());
            }
        });
        return true;
    }

}