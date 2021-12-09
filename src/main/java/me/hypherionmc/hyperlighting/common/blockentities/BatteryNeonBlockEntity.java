package me.hypherionmc.hyperlighting.common.blockentities;

import me.hypherionmc.hyperlighting.api.SolarLight;
import me.hypherionmc.hyperlighting.api.energy.SolarEnergyStorage;
import me.hypherionmc.hyperlighting.common.blocks.BatteryNeon;
import me.hypherionmc.hyperlighting.common.handlers.screen.BatteryNeonScreenHandler;
import me.hypherionmc.hyperlighting.common.init.HLBlockEntities;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.inventory.ImplementedInventory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class BatteryNeonBlockEntity extends BlockEntity implements ImplementedInventory, ExtendedScreenHandlerFactory, SolarLight {

    private final SolarEnergyStorage energyStorage = new SolarEnergyStorage(500, 20, 1);
    private boolean isCharging = false;
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);

    public BatteryNeonBlockEntity(BlockPos pos, BlockState state) {
        super(HLBlockEntities.TILE_BATTERY_NEON, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.isCharging = nbt.getBoolean("isCharging");
        this.energyStorage.readNBT(nbt);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, inventory);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putBoolean("isCharging", this.isCharging);
        this.energyStorage.writeNBT(nbt);
        Inventories.writeNbt(nbt, inventory);
    }

    public boolean isCharging() {
        return this.isCharging;
    }

    public int getMaxPowerLevel() {
        return this.energyStorage.getEnergyCapacity();
    }

    public int getPowerLevel() {
        return this.energyStorage.getEnergyLevel();
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putBoolean("isCharging", this.isCharging);
        this.energyStorage.writeNBT(nbtCompound);
        Inventories.writeNbt(nbtCompound, inventory);
        return nbtCompound;
    }

    private void sendUpdates() {
        BlockState state = world.getBlockState(this.pos);
        if (inventory.get(0).getItem() instanceof DyeItem dyeItem) {
            if (state.get(BatteryNeon.COLOR) != dyeItem.getColor()) {
                state = state.with(BatteryNeon.COLOR, dyeItem.getColor());
            }
        } else {
            state = state.with(BatteryNeon.COLOR, DyeColor.WHITE);
        }
        world.markDirty(this.pos);
        world.setBlockState(this.pos, state, 2);
        world.updateListeners(this.pos, world.getBlockState(this.pos), state, 3);
        markDirty();
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public void serverTick() {
        if (this.world.getTime() % 20L == 0L) {
            if (!inventory.get(1).isEmpty() && inventory.get(1).getItem() == HLItems.WIRELESS_POWERCARD) {
                ItemStack stack = inventory.get(1);
                if (stack.hasNbt() && stack.getNbt() != null) {
                    NbtCompound tagCompound = stack.getNbt();

                    if (tagCompound.contains("blockx") && tagCompound.contains("blocky") && tagCompound.contains("blockz")) {
                        BlockPos pos = new BlockPos(tagCompound.getInt("blockx"), tagCompound.getInt("blocky"), tagCompound.getInt("blockz"));

                        if (world.getBlockEntity(pos) != null && world.getBlockEntity(pos) instanceof SolarPanelBlockEntity solarPanel) {

                            if (solarPanel.getEnergyStorage().extractEnergy(10, true) > 0) {

                                if (this.energyStorage.receiveEnergy(20, true) > 0) {
                                    this.isCharging = true;
                                    solarPanel.getEnergyStorage().extractEnergy(this.energyStorage.receiveEnergy(20, false), false);
                                } else {
                                    this.isCharging = false;
                                }
                            } else {
                                this.isCharging = false;
                            }

                        }
                    }
                }
            } else {
                this.isCharging = false;
            }
        }

        if (this.world.getTime() % 40L == 0L) {
            if (world.getBlockState(pos).get(BatteryNeon.LIT)) {
                this.energyStorage.extractEnergy(1, false);
            }
        }
        this.sendUpdates();
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public BlockEntity getBlockEntity() {
        return this;
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        if (slot == 0 && stack.getItem() instanceof DyeItem) {
            return true;
        }
        return slot == 1 && stack.getItem() == HLItems.WIRELESS_POWERCARD;
    }

    @Override
    public ItemStack removeStack(int slot, int count) {
        ItemStack stack = ImplementedInventory.super.removeStack(slot);
        this.sendUpdates();
        return stack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack stack = ImplementedInventory.super.removeStack(slot);
        this.sendUpdates();
        return stack;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        ImplementedInventory.super.setStack(slot, stack);
        this.sendUpdates();
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new BatteryNeonScreenHandler(syncId, inv, this);
    }

    public DefaultedList<ItemStack> getInventory() {
        return inventory;
    }
}
