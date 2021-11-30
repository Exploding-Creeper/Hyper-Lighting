package me.hypherionmc.hyperlighting.common.tile;

import me.hypherionmc.hyperlighting.api.RemoteSwitchable;
import me.hypherionmc.hyperlighting.api.SolarLight;
import me.hypherionmc.hyperlighting.api.SwitchModule;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.init.HLTileEntities;
import me.hypherionmc.hyperlighting.common.network.PacketHandler;
import me.hypherionmc.hyperlighting.common.network.packets.PacketStateToggle;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileSwitchBoard extends BlockEntity {

    private final ItemStackHandler itemStackHandler = new SwitchItemStackHandler(6);
    private LazyOptional<IItemHandler> storage = LazyOptional.of(() -> itemStackHandler);

    public TileSwitchBoard(BlockPos pos, BlockState state) {
        super(HLTileEntities.TILE_SWITCHBOARD.get(), pos, state);
    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return this.storage.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        compound.put("inventory", this.itemStackHandler.serializeNBT());
        return compound;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.itemStackHandler.deserializeNBT(compound.getCompound("inventory"));
    }

    public int getPowerLevel(int SlotID) {
        ItemStack stack = itemStackHandler.getStackInSlot(SlotID);
        if (this.isLinked(SlotID)) {
            CompoundTag compound = stack.getTag();
            BlockPos pos = new BlockPos(compound.getInt("blockx"), compound.getInt("blocky"), compound.getInt("blockz"));
            SolarLight solar = (SolarLight) level.getBlockEntity(pos);
            return (int) (((double) solar.getPowerLevel() / solar.getMaxPowerLevel()) * 23);
        }
        return 0;
    }

    public int getPowerLevelPer(int SlotID) {
        ItemStack stack = itemStackHandler.getStackInSlot(SlotID);
        if (this.isLinked(SlotID)) {
            CompoundTag compound = stack.getTag();
            BlockPos pos = new BlockPos(compound.getInt("blockx"), compound.getInt("blocky"), compound.getInt("blockz"));
            SolarLight solar = (SolarLight) level.getBlockEntity(pos);
            return (int) (((double) solar.getPowerLevel() / solar.getMaxPowerLevel()) * 100);
        }
        return 0;
    }

    public boolean getState(int SlotID) {
        ItemStack stack = itemStackHandler.getStackInSlot(SlotID);
        if (this.isLinked(SlotID)) {
            CompoundTag compound = stack.getTag();
            BlockPos pos = new BlockPos(compound.getInt("blockx"), compound.getInt("blocky"), compound.getInt("blockz"));
            if (level.getBlockState(pos).getBlock() instanceof RemoteSwitchable) {
                return ((RemoteSwitchable)level.getBlockState(pos).getBlock()).getPoweredState(level.getBlockState(pos));
            }

        }
        return false;
    }

    public boolean getCharging(int SlotID) {
        ItemStack stack = itemStackHandler.getStackInSlot(SlotID);
        if (this.isLinked(SlotID)) {
            CompoundTag compound = stack.getTag();
            BlockPos pos = new BlockPos(compound.getInt("blockx"), compound.getInt("blocky"), compound.getInt("blockz"));
            SolarLight solar = (SolarLight) level.getBlockEntity(pos);
            return solar.isCharging();
        }
        return false;
    }

    public boolean isLinked(int SlotID) {
        ItemStack stack = itemStackHandler.getStackInSlot(SlotID);
        if (!stack.isEmpty() && stack.getItem() instanceof SwitchModule) {
            if (stack.getTag() != null) {
                CompoundTag compound = stack.getTag();
                if (compound.contains("blockx") && compound.contains("blocky") && compound.contains("blockz")) {
                    BlockPos pos = new BlockPos(compound.getInt("blockx"), compound.getInt("blocky"), compound.getInt("blockz"));
                    if (level.getBlockEntity(pos) != null && level.getBlockEntity(pos).hasLevel() && level.getBlockEntity(pos) instanceof SolarLight) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void toggleState(int SlotID) {
        ItemStack stack = itemStackHandler.getStackInSlot(SlotID);
        if (this.isLinked(SlotID)) {
            CompoundTag compound = stack.getTag();
            BlockPos pos = new BlockPos(compound.getInt("blockx"), compound.getInt("blocky"), compound.getInt("blockz"));
            PacketStateToggle msg = new PacketStateToggle(pos);
            if (this.getPowerLevel(SlotID) > 0) {
                PacketHandler.INSTANCE.sendToServer(msg);
            } else {
                if (this.level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 1, false) != null) {
                    this.level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 1, false).displayClientMessage(new TranslatableComponent("Out of power"), true);
                }
            }
        }
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 3, this.getUpdateTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getTag());
    }

    public void dropInventory() {
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            if (!itemStackHandler.getStackInSlot(i).isEmpty()) {
                Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), itemStackHandler.getStackInSlot(i));
            }
        }
    }

    public static class SwitchItemStackHandler extends ItemStackHandler {

        public SwitchItemStackHandler(int size) {
            super(size);
        }

        @Override
        public void setSize(int size) {
            super.setSize(size);
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return stack.getItem() instanceof SwitchModule;
        }
    }
}
