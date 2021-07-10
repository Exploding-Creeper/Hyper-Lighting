package me.hypherionmc.hyperlighting.common.tile;

import me.hypherionmc.hyperlighting.api.energy.SolarEnergyStorage;
import me.hypherionmc.hyperlighting.common.blocks.SolarPanel;
import me.hypherionmc.hyperlighting.common.init.HLTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class TileSolarPanel extends TileEntity implements ITickableTileEntity {

    private final SolarEnergyStorage energyStorage = new SolarEnergyStorage(2000, 0, 1000);
    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);

    public TileSolarPanel() {
        super(HLTileEntities.TILE_SOLAR_PANEL.get());
    }

    @Override
    public void tick() {

        Block blockType = this.getBlockState().getBlock();
        if (blockType instanceof SolarPanel)
        {
            if (world.getDimensionType().hasSkyLight())
            {
                int i = world.getLightFor(LightType.SKY, pos) - world.getSkylightSubtracted();
                float f = world.getCelestialAngleRadians(1.0F);

                if (i > 5 && this.energyStorage.getEnergyStored() < this.energyStorage.getMaxEnergyStored()) {
                    float f1 = f < (float)Math.PI ? 0.0F : ((float)Math.PI * 2F);
                    f = f + (f1 - f) * 0.2F;
                    i = Math.round((float)i * MathHelper.cos(f));
                    i = MathHelper.clamp(i, 0, 15);
                    this.energyStorage.receiveEnergyInternal(i, false);
                }

            }

            if (!world.isRemote) {
                for (Direction direction : Direction.values()) {
                    direction = direction.getOpposite();
                    if (world.getTileEntity(getPos().offset(direction)) != null && world.getTileEntity(getPos().offset(direction)).getCapability(CapabilityEnergy.ENERGY).isPresent()) {
                        IEnergyStorage storage = world.getTileEntity(getPos().offset(direction)).getCapability(CapabilityEnergy.ENERGY).resolve().get();
                        if (storage.canReceive() && storage.receiveEnergy(200, true) > 0) {
                            if (this.energyStorage.extractEnergy(200, true) > 0) {
                                storage.receiveEnergy(this.energyStorage.extractEnergy(200, false), false);
                            }
                        }
                    }
                }
            }

            this.sendUpdates();
        }
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        this.energyStorage.readNBT(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        this.energyStorage.writeNBT(compound);
        return compound;
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

    private void sendUpdates() {
        world.markBlockRangeForRenderUpdate(pos, this.getBlockState(), this.getBlockState());
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        markDirty();
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
        if (getWorld().isRemote) {
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
        final IEnergyStorage storage = getWorld().getTileEntity(getPos().offset(side)).getCapability(CapabilityEnergy.ENERGY, side.getOpposite()).resolve().get();
        return this.energyStorage.extractEnergy(storage.receiveEnergy(energyToTransfer, simulate), simulate);
    }

    boolean canTransferEnergyTo(final Direction side, final int energyToTransfer) {
        if (!this.energyStorage.canExtract()) {
            return false;
        }

        if (getWorld() == null) {
            return false;
        }

        if (getWorld().isRemote) {
            return false;
        }

        if (getWorld().getTileEntity(getPos().offset(side)) == null) {
            return false;
        }

        final IEnergyStorage storage = getWorld().getTileEntity(getPos().offset(side)).getCapability(CapabilityEnergy.ENERGY, side.getOpposite()).resolve().get();

        if (storage == null) {
            return false;
        }

        if (!storage.canReceive()) {
            return false;
        }

        return true;
    }

    boolean isConnectedTo(final Direction side) {
        if (getWorld() == null) {
            return false;
        }

        final TileEntity tile = this.getWorld().getTileEntity(getPos().offset(side));

        if (tile == null) {
            return false;
        }

        return tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite()) != null;
    }

}
