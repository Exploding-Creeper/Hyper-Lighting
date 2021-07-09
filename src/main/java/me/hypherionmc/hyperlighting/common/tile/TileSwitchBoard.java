package me.hypherionmc.hyperlighting.common.tile;

import me.hypherionmc.hyperlighting.api.RemoteSwitchable;
import me.hypherionmc.hyperlighting.api.SolarLight;
import me.hypherionmc.hyperlighting.api.SwitchModule;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.init.HLTileEntities;
import me.hypherionmc.hyperlighting.common.network.PacketHandler;
import me.hypherionmc.hyperlighting.common.network.packets.PacketStateToggle;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileSwitchBoard extends TileEntity {

    private final ItemStackHandler itemStackHandler = new SwitchItemStackHandler(6);
    private LazyOptional<IItemHandler> storage = LazyOptional.of(() -> itemStackHandler);

    public TileSwitchBoard() {
        super(HLTileEntities.TILE_SWITCHBOARD.get());
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
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put("inventory", this.itemStackHandler.serializeNBT());
        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        this.itemStackHandler.deserializeNBT(compound.getCompound("inventory"));
    }

    public int getPowerLevel(int SlotID) {
        ItemStack stack = itemStackHandler.getStackInSlot(SlotID);
        if (this.isLinked(SlotID)) {
            CompoundNBT compound = stack.getTag();
            BlockPos pos = new BlockPos(compound.getInt("blockx"), compound.getInt("blocky"), compound.getInt("blockz"));
            SolarLight solar = (SolarLight) world.getTileEntity(pos);
            return (int) (((double) solar.getPowerLevel() / solar.getMaxPowerLevel()) * 23);
        }
        return 0;
    }

    public int getPowerLevelPer(int SlotID) {
        ItemStack stack = itemStackHandler.getStackInSlot(SlotID);
        if (this.isLinked(SlotID)) {
            CompoundNBT compound = stack.getTag();
            BlockPos pos = new BlockPos(compound.getInt("blockx"), compound.getInt("blocky"), compound.getInt("blockz"));
            SolarLight solar = (SolarLight) world.getTileEntity(pos);
            return (int) (((double) solar.getPowerLevel() / solar.getMaxPowerLevel()) * 100);
        }
        return 0;
    }

    public boolean getState(int SlotID) {
        ItemStack stack = itemStackHandler.getStackInSlot(SlotID);
        if (this.isLinked(SlotID)) {
            CompoundNBT compound = stack.getTag();
            BlockPos pos = new BlockPos(compound.getInt("blockx"), compound.getInt("blocky"), compound.getInt("blockz"));
            if (world.getBlockState(pos).getBlock() instanceof RemoteSwitchable) {
                return ((RemoteSwitchable)world.getBlockState(pos).getBlock()).getPoweredState(world.getBlockState(pos));
            }

        }
        return false;
    }

    public boolean getCharging(int SlotID) {
        ItemStack stack = itemStackHandler.getStackInSlot(SlotID);
        if (this.isLinked(SlotID)) {
            CompoundNBT compound = stack.getTag();
            BlockPos pos = new BlockPos(compound.getInt("blockx"), compound.getInt("blocky"), compound.getInt("blockz"));
            SolarLight solar = (SolarLight) world.getTileEntity(pos);
            return solar.isCharging();
        }
        return false;
    }

    public boolean isLinked(int SlotID) {
        ItemStack stack = itemStackHandler.getStackInSlot(SlotID);
        if (!stack.isEmpty() && stack.getItem() instanceof SwitchModule) {
            if (stack.getTag() != null) {
                CompoundNBT compound = stack.getTag();
                if (compound.contains("blockx") && compound.contains("blocky") && compound.contains("blockz")) {
                    BlockPos pos = new BlockPos(compound.getInt("blockx"), compound.getInt("blocky"), compound.getInt("blockz"));
                    if (world.getTileEntity(pos) != null && world.getTileEntity(pos).hasWorld() && world.getTileEntity(pos) instanceof SolarLight) {
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
            CompoundNBT compound = stack.getTag();
            BlockPos pos = new BlockPos(compound.getInt("blockx"), compound.getInt("blocky"), compound.getInt("blockz"));
            PacketStateToggle msg = new PacketStateToggle(pos);
            if (this.getPowerLevel(SlotID) > 0) {
                PacketHandler.INSTANCE.sendToServer(msg);
            } else {
                if (this.world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 1, false) != null) {
                    this.world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 1, false).sendStatusMessage(new TranslationTextComponent("Out of power"), true);
                }
            }
        }
    }

    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(this.getBlockState(), pkt.getNbtCompound());
    }

    public void dropInventory() {
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            if (!itemStackHandler.getStackInSlot(i).isEmpty()) {
                InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemStackHandler.getStackInSlot(i));
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