package me.hypherionmc.hyperlighting.common.tile;

import me.hypherionmc.hyperlighting.api.SolarLight;
import me.hypherionmc.hyperlighting.api.energy.IEnergyContainerItem;
import me.hypherionmc.hyperlighting.api.energy.SolarEnergyStorage;
import me.hypherionmc.hyperlighting.common.blocks.BatteryNeon;
import me.hypherionmc.hyperlighting.common.config.HyperLightingConfig;
import me.hypherionmc.hyperlighting.common.init.HLBlocks;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.init.HLTileEntities;
import me.hypherionmc.hyperlighting.util.ModUtils;
import me.hypherionmc.rgblib.api.ColoredLightManager;
import me.hypherionmc.rgblib.api.RGBLight;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileBatteryNeon extends TileEntity implements ITickableTileEntity, SolarLight {

    private boolean isCharging = false;
    private final SolarEnergyStorage energyStorage = new SolarEnergyStorage(500, 20, 1);
    private final ItemStackHandler itemStackHandler = new ItemHandler(1);
    private final ItemStackHandler dyeHandler = new DyeHandler(1);

    private final LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);

    public TileBatteryNeon() {
        super(HLTileEntities.TILE_BATTERY_NEON.get());

        if (ModUtils.isRGBLibPresent()) {
            ColoredLightManager.registerProvider(this, this::produceColoredLight);
        }
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            if (this.world.getGameTime() % 20L == 0L) {
                if (!itemStackHandler.getStackInSlot(0).isEmpty() && itemStackHandler.getStackInSlot(0).getItem() == HLItems.WIRELESS_POWERCARD.get()) {
                    ItemStack stack = itemStackHandler.getStackInSlot(0);
                    if (stack.hasTag() && stack.getTag() != null) {
                        CompoundNBT tagCompound = stack.getTag();

                        if (tagCompound.contains("blockx") && tagCompound.contains("blocky") && tagCompound.contains("blockz")) {
                            BlockPos pos = new BlockPos(tagCompound.getInt("blockx"), tagCompound.getInt("blocky"), tagCompound.getInt("blockz"));

                            if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileSolarPanel) {
                                TileSolarPanel storage1 = (TileSolarPanel) world.getTileEntity(pos);

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

            if (this.world.getGameTime() % 40L == 0L) {
                if (world.getBlockState(pos) != null && world.getBlockState(pos).getBlock() == HLBlocks.BATTERY_NEON.get()) {
                    if (world.getBlockState(pos).get(BatteryNeon.LIT)) {
                        this.energyStorage.extractEnergy(1, false);
                    }
                }

            }
            this.sendUpdates();

        }
        world.getLight(pos);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.isCharging = nbt.getBoolean("isCharging");
        this.energyStorage.readNBT(nbt);
        this.itemStackHandler.deserializeNBT(nbt.getCompound("inventory"));
        this.dyeHandler.deserializeNBT(nbt.getCompound("dye"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putBoolean("isCharging", this.isCharging);
        this.energyStorage.writeNBT(compound);
        compound.put("inventory", this.itemStackHandler.serializeNBT());
        compound.put("dye", this.dyeHandler.serializeNBT());
        return compound;
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
        world.markBlockRangeForRenderUpdate(pos, this.getBlockState(), this.getBlockState());
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        markDirty();
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

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return this.energy.cast();
        }
        return super.getCapability(cap, side);
    }

    public RGBLight produceColoredLight(BlockPos pos) {

        if (!HyperLightingConfig.batteryColor.get()) {
            if (world.getBlockState(pos).getBlock() instanceof BatteryNeon && world.getBlockState(pos).get(BatteryNeon.LIT)) {
                if (!dyeHandler.getStackInSlot(0).isEmpty()) {
                    ItemStack stack = dyeHandler.getStackInSlot(0);
                    float[] color = ((DyeItem)stack.getItem()).getDyeColor().getColorComponentValues();
                    return new RGBLight.Builder().pos(pos).color(color[0], color[1], color[2]).radius(14).build();
                } else {
                    float[] color = DyeColor.WHITE.getColorComponentValues();
                    return new RGBLight.Builder().pos(pos).color(color[0], color[1], color[2]).radius(14).build();
                }
            }
        }
        return null;
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
            return stack.getItem() == HLItems.WIRELESS_POWERCARD.get() || stack.getItem() == HLItems.BATTERY.get();
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
            InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), dyeHandler.getStackInSlot(0));
        }

        if (!itemStackHandler.getStackInSlot(0).isEmpty()) {
            InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemStackHandler.getStackInSlot(0));
        }
    }
}