package me.hypherionmc.hyperlighting.common.network.packets;

import me.hypherionmc.hyperlighting.common.containers.ContainerBatteryNeon;
import me.hypherionmc.hyperlighting.common.tile.TileBatteryNeon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

import java.util.function.Supplier;

public class OpenGUIPacket {

    private BlockPos posToSet;
    private int guiid;

    public OpenGUIPacket(FriendlyByteBuf buffer) {
        posToSet = buffer.readBlockPos();
        guiid = buffer.readInt();
    }

    public OpenGUIPacket(int guiid, BlockPos pos) {
        this.guiid = guiid;
        this.posToSet = pos;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.posToSet);
        buf.writeInt(this.guiid);

    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            BlockEntity te = ctx.get().getSender().getLevel().getBlockEntity(this.posToSet);

            MenuProvider containerProvider = new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    if (te instanceof TileBatteryNeon) {
                        return new TranslatableComponent("container.batteryneon");
                    }
                    /*if (te instanceof TileNeonSign) {
                        return new TranslationTextComponent("container.neonsign");
                    }*/
                    return null;
                }

                @Override
                public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
                    if (te instanceof TileBatteryNeon) {
                        return new ContainerBatteryNeon(i, te.getLevel(), posToSet, playerInventory, playerEntity);
                    }
                    /*if (te instanceof TileNeonSign) {
                        return new ContainerNeonSign(i, te.getWorld(), posToSet, playerInventory, playerEntity);
                    }*/
                    return null;
                }
            };

            if (containerProvider.getDisplayName() != null) {
                NetworkHooks.openGui(ctx.get().getSender(), containerProvider, te.getBlockPos());
            }
        });
        return true;
    }

}
