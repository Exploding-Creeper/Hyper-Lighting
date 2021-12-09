package me.hypherionmc.hyperlighting.common.blockentities;

import me.hypherionmc.hyperlighting.api.energy.SolarEnergyStorage;
import me.hypherionmc.hyperlighting.common.blocks.SolarPanel;
import me.hypherionmc.hyperlighting.common.init.HLBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;

public class SolarPanelBlockEntity extends BlockEntity {

    private final SolarEnergyStorage energyStorage = new SolarEnergyStorage(2000, 50, 1000);

    public SolarPanelBlockEntity(BlockPos pos, BlockState state) {
        super(HLBlockEntities.TILE_SOLAR_PANEL, pos, state);
    }

    public void serverTick() {

        if (this.getCachedState().getBlock() instanceof SolarPanel) {
            if (world.getDimension().hasSkyLight()) {
                int i = world.getLightLevel(LightType.SKY, this.pos) - world.getAmbientDarkness();
                float f = world.getSkyAngle(1.0F);

                if (i > 8 && this.energyStorage.getEnergyLevel() < this.energyStorage.getEnergyCapacity()) {
                    float f1 = f < (float) Math.PI ? 0.0F : ((float) Math.PI * 2F);
                    f = f + (f1 - f) * 0.2F;
                    i = Math.round((float) i * MathHelper.cos(f));
                    i = MathHelper.clamp(i, 0, 15);
                    this.energyStorage.receiveEnergyInternal(i, false);
                }
            }
            this.sendUpdates();
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.energyStorage.readNBT(nbt);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        this.energyStorage.writeNBT(nbt);
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
        this.energyStorage.writeNBT(nbtCompound);
        return nbtCompound;
    }

    public SolarEnergyStorage getEnergyStorage() {
        return energyStorage;
    }
}
