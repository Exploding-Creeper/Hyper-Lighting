package me.hypherionmc.hyperlighting.common.tile;

import me.hypherionmc.hyperlighting.api.SolarLight;
import me.hypherionmc.hyperlighting.api.energy.IEnergyContainerItem;
import me.hypherionmc.hyperlighting.api.energy.SolarEnergyStorage;
import me.hypherionmc.hyperlighting.common.blocks.BatteryNeon;
import me.hypherionmc.hyperlighting.common.init.HLBlocks;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.init.HLTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileBatteryNeon extends BlockEntity implements SolarLight {

    private boolean isCharging = false;
    private final SolarEnergyStorage energyStorage = new SolarEnergyStorage(500, 20, 1);
    private final ItemStackHandler itemStackHandler = new ItemHandler(1);
    private final ItemStackHandler dyeHandler = new DyeHandler(1);

    private final LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);

    public TileBatteryNeon(BlockPos pos, BlockState state) {
        super(HLTileEntities.TILE_BATTERY_NEON.get(), pos, state);
        /*if (ModUtils.isRGBLibPresent()) {
            ColoredLightManager.registerProvider(this, this::produceColoredLight);
        }*/
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.isCharging = nbt.getBoolean("isCharging");
        this.energyStorage.readNBT(nbt);
        this.itemStackHandler.deserializeNBT(nbt.getCompound("inventory"));
        this.dyeHandler.deserializeNBT(nbt.getCompound("dye"));
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putBoolean("isCharging", this.isCharging);
        this.energyStorage.writeNBT(compound);
        compound.put("inventory", this.itemStackHandler.serializeNBT());
        compound.put("dye", this.dyeHandler.serializeNBT());
        //return compound;
    }

    public boolean isCharging() {
        return this.isCharging;
    }

    public int getMaxPowerLevel() {
        return this.energyStorage.getMaxEnergyStored();
    }

    public int getPowerLevel() {
        return this.energyStorage.getEnergyStored();
    }


    private void sendUpdates() {
        level.setBlocksDirty(worldPosition, this.getBlockState(), this.getBlockState());
        level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 3);
        setChanged();
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, (blockEntity) -> this.getUpdateTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        //super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getTag());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return this.energy.cast();
        }
        return super.getCapability(cap, side);
    }

    /*public RGBLight produceColoredLight(BlockPos pos) {

        if (!HyperLightingConfig.batteryColor.get()) {
            if (level.getBlockState(pos).getBlock() instanceof BatteryNeon && level.getBlockState(pos).getValue(BatteryNeon.LIT)) {
                if (!dyeHandler.getStackInSlot(0).isEmpty()) {
                    ItemStack stack = dyeHandler.getStackInSlot(0);
                    float[] color = ((DyeItem)stack.getItem()).getDyeColor().getTextureDiffuseColors();
                    //return new RGBLight.Builder().pos(pos).color(color[0], color[1], color[2]).radius(14).build();
                } else {
                    float[] color = DyeColor.WHITE.getTextureDiffuseColors();
                    //return new RGBLight.Builder().pos(pos).color(color[0], color[1], color[2]).radius(14).build();
                }
            }
        }
        return null;
    }*/

    // TODO - Cleanup this mess
    public void serverTick() {
        if (this.level.getGameTime() % 20L == 0L) {
            if (!itemStackHandler.getStackInSlot(0).isEmpty() && itemStackHandler.getStackInSlot(0).getItem() == HLItems.WIRELESS_POWERCARD.get()) {
                ItemStack stack = itemStackHandler.getStackInSlot(0);
                if (stack.hasTag() && stack.getTag() != null) {
                    CompoundTag tagCompound = stack.getTag();

                    if (tagCompound.contains("blockx") && tagCompound.contains("blocky") && tagCompound.contains("blockz")) {
                        BlockPos pos = new BlockPos(tagCompound.getInt("blockx"), tagCompound.getInt("blocky"), tagCompound.getInt("blockz"));

                        if (level.getBlockEntity(pos) != null && level.getBlockEntity(pos) instanceof TileSolarPanel) {
                            TileSolarPanel storage1 = (TileSolarPanel) level.getBlockEntity(pos);

                            if (storage1 != null && storage1.getCapability(CapabilityEnergy.ENERGY).isPresent()) {
                                IEnergyStorage storage = storage1.getCapability(CapabilityEnergy.ENERGY).resolve().get();

                                if (storage.canExtract() && storage.extractEnergy(10, true) > 0) {

                                    if (this.energyStorage.receiveEnergy(20, true) > 0) {
                                        this.isCharging = true;
                                        storage.extractEnergy(this.energyStorage.receiveEnergy(20, false), false);
                                    } else {
                                        this.isCharging = false;
                                    }
                                } else {
                                    this.isCharging = false;
                                }
                            }

                        }
                    }
                }
            } else if (!itemStackHandler.getStackInSlot(0).isEmpty() && itemStackHandler.getStackInSlot(0).getItem() instanceof IEnergyContainerItem) {
                IEnergyStorage storage = itemStackHandler.getStackInSlot(0).getCapability(CapabilityEnergy.ENERGY).resolve().get();

                if (storage.canExtract() && storage.extractEnergy(10, true) > 0) {

                    if (this.energyStorage.receiveEnergy(20, true) > 0) {
                        this.isCharging = true;
                        storage.extractEnergy(this.energyStorage.receiveEnergy(20, false), false);
                    } else {
                        this.isCharging = false;
                    }
                } else {
                    this.isCharging = false;
                }
            } else {
                this.isCharging = false;
            }

        }

        if (this.level.getGameTime() % 40L == 0L) {
            if (level.getBlockState(worldPosition) != null && level.getBlockState(worldPosition).getBlock() == HLBlocks.BATTERY_NEON.get()) {
                if (level.getBlockState(worldPosition).getValue(BatteryNeon.LIT)) {
                    this.energyStorage.extractEnergy(1, false);
                }
            }

        }
        this.sendUpdates();

        level.getMaxLocalRawBrightness(worldPosition);
    }

    class ItemHandler extends ItemStackHandler {

        public ItemHandler(int size) {
            super(size);
        }

        @Override
        public void setSize(int size) {
            super.setSize(size);
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return stack.getItem() == HLItems.WIRELESS_POWERCARD.get()/* || stack.getItem() == HLItems.BATTERY.get()*/;
        }
    }

    public class DyeHandler extends ItemStackHandler {

        public DyeHandler(int size) {
            super(size);
        }

        @Override
        public void setSize(int size) {
            super.setSize(size);
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return stack.getItem() instanceof DyeItem;
        }
    }

    public ItemStackHandler getItemStackHandler() {
        return itemStackHandler;
    }

    public ItemStackHandler getDyeHandler() {
        return dyeHandler;
    }

    public void dropInventory() {
        if (!dyeHandler.getStackInSlot(0).isEmpty()) {
            Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), dyeHandler.getStackInSlot(0));
        }

        if (!itemStackHandler.getStackInSlot(0).isEmpty()) {
            Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), itemStackHandler.getStackInSlot(0));
        }
    }

    @Override
    public void setRemoved() {
        dropInventory();
        super.setRemoved();
    }
}
