package me.hypherionmc.hyperlighting.common.blockentities;

import me.hypherionmc.hyperlighting.api.RemoteSwitchable;
import me.hypherionmc.hyperlighting.api.SolarLight;
import me.hypherionmc.hyperlighting.api.SwitchModule;
import me.hypherionmc.hyperlighting.common.handlers.screen.SwitchBoardScreenHandler;
import me.hypherionmc.hyperlighting.common.init.HLBlockEntities;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.inventory.ImplementedInventory;
import me.hypherionmc.hyperlighting.common.network.NetworkHandler;
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
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class SwitchBoardBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {

    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(6, ItemStack.EMPTY);

    public SwitchBoardBlockEntity(BlockPos pos, BlockState state) {
        super(HLBlockEntities.TILE_SWITCH_BOARD, pos, state);
    }

    public int getPowerLevel(int SlotID) {
        ItemStack stack = inventory.get(SlotID);
        if (this.isLinked(SlotID)) {
            NbtCompound compound = stack.getNbt();
            BlockPos pos = new BlockPos(compound.getInt("blockx"), compound.getInt("blocky"), compound.getInt("blockz"));
            SolarLight solar = (SolarLight) world.getBlockEntity(pos);
            return (int) (((double) solar.getPowerLevel() / solar.getMaxPowerLevel()) * 23);
        }
        return 0;
    }

    public int getPowerLevelPer(int SlotID) {
        ItemStack stack = inventory.get(SlotID);
        if (this.isLinked(SlotID)) {
            NbtCompound compound = stack.getNbt();
            BlockPos pos = new BlockPos(compound.getInt("blockx"), compound.getInt("blocky"), compound.getInt("blockz"));
            SolarLight solar = (SolarLight) world.getBlockEntity(pos);
            return (int) (((double) solar.getPowerLevel() / solar.getMaxPowerLevel()) * 100);
        }
        return 0;
    }

    public boolean getState(int SlotID) {
        ItemStack stack = inventory.get(SlotID);
        if (this.isLinked(SlotID)) {
            NbtCompound compound = stack.getNbt();
            BlockPos pos = new BlockPos(compound.getInt("blockx"), compound.getInt("blocky"), compound.getInt("blockz"));
            if (world.getBlockState(pos).getBlock() instanceof RemoteSwitchable) {
                return ((RemoteSwitchable) world.getBlockState(pos).getBlock()).getPoweredState(world.getBlockState(pos));
            }

        }
        return false;
    }

    public boolean getCharging(int SlotID) {
        ItemStack stack = inventory.get(SlotID);
        if (this.isLinked(SlotID)) {
            NbtCompound compound = stack.getNbt();
            BlockPos pos = new BlockPos(compound.getInt("blockx"), compound.getInt("blocky"), compound.getInt("blockz"));
            SolarLight solar = (SolarLight) world.getBlockEntity(pos);
            return solar.isCharging();
        }
        return false;
    }

    public boolean isLinked(int SlotID) {
        ItemStack stack = inventory.get(SlotID);
        if (!stack.isEmpty() && stack.getItem() instanceof SwitchModule) {
            if (stack.getNbt() != null) {
                NbtCompound compound = stack.getNbt();
                if (compound.contains("blockx") && compound.contains("blocky") && compound.contains("blockz")) {
                    BlockPos pos = new BlockPos(compound.getInt("blockx"), compound.getInt("blocky"), compound.getInt("blockz"));
                    return world.getBlockEntity(pos) != null && world.getBlockEntity(pos).hasWorld() && world.getBlockEntity(pos) instanceof SolarLight;
                }
            }
        }
        return false;
    }

    public void toggleState(int SlotID) {
        ItemStack stack = inventory.get(SlotID);
        if (this.isLinked(SlotID)) {
            NbtCompound compound = stack.getNbt();
            BlockPos pos = new BlockPos(compound.getInt("blockx"), compound.getInt("blocky"), compound.getInt("blockz"));
            if (this.getPowerLevel(SlotID) > 0) {
                NetworkHandler.sendStateTogglePacket(pos);
            } else {
                if (this.world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 1, false) != null) {
                    this.world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 1, false).sendMessage(new LiteralText("Out of power"), true);
                }
            }
        }
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
        return stack.getItem() instanceof SwitchModule;
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new SwitchBoardScreenHandler(syncId, inv, this);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, inventory);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        this.writeNbt(nbtCompound);
        return nbtCompound;
    }

    @Override
    public void inventoryChanged() {
        this.sendUpdates();
    }

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

    private void sendUpdates() {
        world.markDirty(this.pos);
        world.updateListeners(this.pos, world.getBlockState(this.pos), world.getBlockState(this.pos), 3);
        markDirty();
    }
}
