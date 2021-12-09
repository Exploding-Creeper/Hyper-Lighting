package me.hypherionmc.hyperlighting.common.blockentities;

import me.hypherionmc.hyperlighting.api.SolarLight;
import me.hypherionmc.hyperlighting.api.energy.SolarEnergyStorage;
import me.hypherionmc.hyperlighting.common.blocks.FenceSolar;
import me.hypherionmc.hyperlighting.common.init.HLBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;

public class FenceSolarBlockEntity extends BlockEntity implements SolarLight {

    private final SolarEnergyStorage energyStorage = new SolarEnergyStorage(500, 100, 0);
    private boolean isCharging = false;

    public FenceSolarBlockEntity(BlockPos pos, BlockState state) {
        super(HLBlockEntities.TILE_FENCE_SOLAR, pos, state);
    }

    public void serverTick() {
        if (this.world != null && this.world.getTime() % 20L == 0L && this.getCachedState().getBlock() instanceof FenceSolar) {
            BlockState iblockstate = this.world.getBlockState(this.pos);
            BlockState oldState = iblockstate;

            if (world.getDimension().hasSkyLight()) {
                int i = world.getLightLevel(LightType.SKY, this.pos) - world.getAmbientDarkness();
                float f = world.getSkyAngle(1.0F);

                if (i > 8 && this.energyStorage.getEnergyLevel() < this.energyStorage.getEnergyCapacity()) {
                    float f1 = f < (float) Math.PI ? 0.0F : ((float) Math.PI * 2F);
                    f = f + (f1 - f) * 0.2F;
                    i = Math.round((float) i * MathHelper.cos(f));
                    i = MathHelper.clamp(i, 0, 15);
                    this.isCharging = true;
                    energyStorage.receiveEnergy(i, false);
                } else {
                    this.isCharging = false;
                }

                if (iblockstate.get(FenceSolar.LIT)) {
                    if (this.energyStorage.getEnergyLevel() < 1) {
                        iblockstate = iblockstate.with(FenceSolar.LIT, false);
                        world.setBlockState(this.pos, iblockstate, 3);
                        world.updateListeners(this.pos, oldState, iblockstate, 4);
                    } else {
                        this.energyStorage.extractEnergyInternal(1, false);
                    }
                }
            }
            this.sendUpdates();
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.isCharging = nbt.getBoolean("isCharging");
        this.energyStorage.readNBT(nbt);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putBoolean("isCharging", this.isCharging);
        this.energyStorage.writeNBT(nbt);
    }

    @Override
    public boolean isCharging() {
        return this.isCharging;
    }

    @Override
    public int getMaxPowerLevel() {
        return this.energyStorage.getEnergyCapacity();
    }

    @Override
    public int getPowerLevel() {
        return this.energyStorage.getEnergyLevel();
    }


    private void sendUpdates() {
        world.markDirty(this.pos);
        world.updateListeners(this.pos, world.getBlockState(this.pos), world.getBlockState(this.pos), 3);
        markDirty();
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putBoolean("isCharging", this.isCharging);
        this.energyStorage.writeNBT(nbtCompound);
        return nbtCompound;
    }
}
