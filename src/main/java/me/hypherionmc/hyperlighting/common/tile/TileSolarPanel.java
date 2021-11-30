package me.hypherionmc.hyperlighting.common.tile;

import me.hypherionmc.hyperlighting.api.energy.SolarEnergyStorage;
import me.hypherionmc.hyperlighting.common.blocks.SolarPanel;
import me.hypherionmc.hyperlighting.common.init.HLTileEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class TileSolarPanel extends BlockEntity {

    private final SolarEnergyStorage energyStorage = new SolarEnergyStorage(2000, 0, 1000);
    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);

    public TileSolarPanel(BlockPos pos, BlockState state) {
        super(HLTileEntities.TILE_SOLAR_PANEL.get(), pos, state);
    }

    public void serverTick() {

        Block blockType = this.getBlockState().getBlock();
        if (blockType instanceof SolarPanel)
        {
            if (level.dimensionType().hasSkyLight())
            {
                int i = level.getBrightness(LightLayer.SKY, worldPosition) - level.getSkyDarken();
                float f = level.getSunAngle(1.0F);

                if (i > 5 && this.energyStorage.getEnergyStored() < this.energyStorage.getMaxEnergyStored()) {
                    float f1 = f < (float)Math.PI ? 0.0F : ((float)Math.PI * 2F);
                    f = f + (f1 - f) * 0.2F;
                    i = Math.round((float)i * Mth.cos(f));
                    i = Mth.clamp(i, 0, 15);
                    this.energyStorage.receiveEnergyInternal(i, false);
                }

            }

            for (Direction direction : Direction.values()) {
                direction = direction.getOpposite();
                if (level.getBlockEntity(getBlockPos().relative(direction)) != null && level.getBlockEntity(getBlockPos().relative(direction)).getCapability(CapabilityEnergy.ENERGY).isPresent()) {
                    IEnergyStorage storage = level.getBlockEntity(getBlockPos().relative(direction)).getCapability(CapabilityEnergy.ENERGY).resolve().get();
                    if (storage.canReceive() && storage.receiveEnergy(200, true) > 0) {
                        if (this.energyStorage.extractEnergy(200, true) > 0) {
                            storage.receiveEnergy(this.energyStorage.extractEnergy(200, false), false);
                        }
                    }
                }
            }

            this.sendUpdates();
        }
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.energyStorage.readNBT(compound);
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        this.energyStorage.writeNBT(compound);
        return compound;
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, (blockEntity -> this.getUpdateTag()));
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

    private void sendUpdates() {
        level.setBlocksDirty(worldPosition, this.getBlockState(), this.getBlockState());
        level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 3);
        setChanged();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return this.energy.cast();
        }
        return super.getCapability(cap, side);
    }

    void transferEnergyToAllAround() {
        if (!canTransferEnergyToAllAround()) {
            return;
        }

        getConnectedSides().forEach(side -> {
            transferEnergyTo(side, (int) ((float) this.energyStorage.getEnergyStored() / (float) getConnectedSides().size()), false);
        });

    }

    boolean canTransferEnergyToAllAround() {
        if (getLevel().isClientSide) {
            return false;
        }

        if (!this.energyStorage.canExtract()) {
            return false;
        }

        if (this.energyStorage.getEnergyStored() <= 0) {
            return false;
        }
        return true;
    }

    ArrayList<Direction> getConnectedSides() {
        final ArrayList<Direction> connectedSides = new ArrayList<>();

        for (final Direction side : Direction.values()) {
            if (isConnectedTo(side)) {
                connectedSides.add(side);
            }
        }

        return connectedSides;
    }

    int transferEnergyTo(final Direction side, final int energyToTransfer, final boolean simulate) {
        if (!canTransferEnergyTo(side, energyToTransfer)) {
            return 0;
        }
        final IEnergyStorage storage = getLevel().getBlockEntity(getBlockPos().relative(side)).getCapability(CapabilityEnergy.ENERGY, side.getOpposite()).resolve().get();
        return this.energyStorage.extractEnergy(storage.receiveEnergy(energyToTransfer, simulate), simulate);
    }

    boolean canTransferEnergyTo(final Direction side, final int energyToTransfer) {
        if (!this.energyStorage.canExtract()) {
            return false;
        }

        if (getLevel() == null) {
            return false;
        }

        if (getLevel().isClientSide) {
            return false;
        }

        if (getLevel().getBlockEntity(getBlockPos().relative(side)) == null) {
            return false;
        }

        final IEnergyStorage storage = getLevel().getBlockEntity(getBlockPos().relative(side)).getCapability(CapabilityEnergy.ENERGY, side.getOpposite()).resolve().get();

        if (storage == null) {
            return false;
        }

        if (!storage.canReceive()) {
            return false;
        }

        return true;
    }

    boolean isConnectedTo(final Direction side) {
        if (getLevel() == null) {
            return false;
        }

        final BlockEntity tile = this.getLevel().getBlockEntity(getBlockPos().relative(side));

        if (tile == null) {
            return false;
        }

        return tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite()) != null;
    }

}
