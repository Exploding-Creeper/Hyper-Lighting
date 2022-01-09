package me.hypherionmc.hyperlighting.common.tile;

import me.hypherionmc.hyperlighting.api.SolarLight;
import me.hypherionmc.hyperlighting.api.energy.SolarEnergyStorage;
import me.hypherionmc.hyperlighting.common.blocks.FenceSolar;
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

public class TileSolarLight extends BlockEntity implements SolarLight {

    private final int maxPowerLevel = 500;
    private final SolarEnergyStorage energyStorage = new SolarEnergyStorage(500, 100, 0);
    private final LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);
    private int powerLevel = 0;
    private boolean isCharging = false;

    public TileSolarLight(BlockPos pos, BlockState state) {
        super(HLTileEntities.TILE_SOLAR_LIGHT.get(), pos, state);
    }

    // TODO - Clean up this mess
    public void serverTick() {

        if (this.level != null && this.level.getGameTime() % 20L == 0L) {
            Block blockType = this.getBlockState().getBlock();
            if (blockType instanceof FenceSolar) {
                BlockState iblockstate = level.getBlockState(worldPosition);
                BlockState oldState = iblockstate;

                if (level.dimensionType().hasSkyLight()) {
                    int i = level.getBrightness(LightLayer.SKY, worldPosition) - level.getSkyDarken();
                    float f = level.getSunAngle(1.0F);

                    if (i > 5 && this.powerLevel < this.maxPowerLevel) {
                        float f1 = f < (float) Math.PI ? 0.0F : ((float) Math.PI * 2F);
                        f = f + (f1 - f) * 0.2F;
                        i = Math.round((float) i * Mth.cos(f));
                        i = Mth.clamp(i, 0, 15);
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

                    if (iblockstate.getValue(FenceSolar.LIT)) {
                        if (this.powerLevel < 1) {
                            iblockstate = iblockstate.setValue(FenceSolar.LIT, false);
                            level.setBlock(worldPosition, iblockstate, 3);
                            level.sendBlockUpdated(worldPosition, oldState, iblockstate, 4);
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
    public void load(CompoundTag compound) {
        super.load(compound);
        this.powerLevel = compound.getInt("power");
        this.isCharging = compound.getBoolean("isCharging");
        this.energyStorage.readNBT(compound);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("power", this.powerLevel);
        compound.putBoolean("isCharging", this.isCharging);
        this.energyStorage.writeNBT(compound);
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
        level.setBlocksDirty(worldPosition, this.getBlockState(), this.getBlockState());
        level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 3);
        setChanged();
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, (blockEntity -> this.getUpdateTag()));
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
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
}
