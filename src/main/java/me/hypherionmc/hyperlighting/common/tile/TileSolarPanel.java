package me.hypherionmc.hyperlighting.common.tile;

import me.hypherionmc.hyperlighting.api.energy.SolarEnergyStorage;
import me.hypherionmc.hyperlighting.api.energy.SolarMachine;
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

public class TileSolarPanel extends TileEntity implements ITickableTileEntity, SolarMachine {

    private final SolarEnergyStorage energyStorage = new SolarEnergyStorage(2000, 2000, 1000);
    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);

    public TileSolarPanel() {
        super(HLTileEntities.TILE_SOLAR_PANEL.get());
    }

    @Override
    public SolarEnergyStorage getStorage() {
        return this.energyStorage;
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

                if (i > 5 && this.energyStorage.getSolarEnergyStored() < this.energyStorage.getMaxSolarEnergyStored()) {
                    float f1 = f < (float)Math.PI ? 0.0F : ((float)Math.PI * 2F);
                    f = f + (f1 - f) * 0.2F;
                    i = Math.round((float)i * MathHelper.cos(f));
                    i = MathHelper.clamp(i, 0, 15);
                    this.energyStorage.receiveSolar(i, false);
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

}
