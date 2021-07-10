package me.hypherionmc.hyperlighting.common.tile;

import me.hypherionmc.hyperlighting.api.SolarLight;
import me.hypherionmc.hyperlighting.api.energy.SolarEnergyStorage;
import me.hypherionmc.hyperlighting.common.blocks.FenceSolar;
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

public class TileSolarLight extends TileEntity implements ITickableTileEntity, SolarLight {

    private int powerLevel = 0;
    private boolean isCharging = false;
    private final int maxPowerLevel = 500;
    private final SolarEnergyStorage energyStorage = new SolarEnergyStorage(500, 100, 0);

    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);

    public TileSolarLight() {
        super(HLTileEntities.TILE_SOLAR_LIGHT.get());
    }

    @Override
    public void tick() {

        if (this.world != null && !this.world.isRemote && this.world.getGameTime() % 20L == 0L)
        {
            Block blockType = this.getBlockState().getBlock();
            if (blockType instanceof FenceSolar)
            {
                BlockState iblockstate = world.getBlockState(pos);
                BlockState oldState = iblockstate;

                if (world.getDimensionType().hasSkyLight())
                {
                    int i = world.getLightFor(LightType.SKY, pos) - world.getSkylightSubtracted();
                    float f = world.getCelestialAngleRadians(1.0F);

                    if (i > 5 && this.powerLevel < this.maxPowerLevel) {
                        float f1 = f < (float)Math.PI ? 0.0F : ((float)Math.PI * 2F);
                        f = f + (f1 - f) * 0.2F;
                        i = Math.round((float)i * MathHelper.cos(f));
                        i = MathHelper.clamp(i, 0, 15);
                        this.isCharging = true;
                        this.powerLevel += i;
                    } else {
                        this.isCharging = false;
                    }

                    if (this.powerLevel > this.maxPowerLevel) {
                        this.powerLevel = this.maxPowerLevel;
                    }

                    if (this.powerLevel < 1) {
                        this.powerLevel = 0;
                    }

                    if (iblockstate.get(FenceSolar.LIT)) {
                        if (this.powerLevel < 1) {
                            iblockstate = iblockstate.with(FenceSolar.LIT, false);
                            world.setBlockState(pos, iblockstate, 3);
                            world.notifyBlockUpdate(pos, oldState, iblockstate, 4);
                        } else {
                            this.powerLevel -= 1;
                        }
                    }
                }
                this.sendUpdates();

            }
        }
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        this.powerLevel = compound.getInt("power");
        this.isCharging = compound.getBoolean("isCharging");
        this.energyStorage.readNBT(compound.getCompound("energyStorage"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("power", this.powerLevel);
        compound.putBoolean("isCharging", this.isCharging);
        compound.put("energyStorage", this.energyStorage.writeNBT(compound));
        return compound;
    }

    @Override
    public boolean isCharging() {
        return this.isCharging;
    }

    @Override
    public int getMaxPowerLevel() {
        return this.maxPowerLevel;
    }

    @Override
    public int getPowerLevel() {
        return this.powerLevel;
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
}
