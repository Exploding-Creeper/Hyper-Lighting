package me.hypherionmc.hyperlighting.common.tile;

import me.hypherionmc.hyperlighting.api.ISidedTickable;
import me.hypherionmc.hyperlighting.common.blocks.FogMachineBlock;
import me.hypherionmc.hyperlighting.common.fluids.ColoredWater;
import me.hypherionmc.hyperlighting.common.handlers.ParticleRegistryHandler;
import me.hypherionmc.hyperlighting.common.init.HLTileEntities;
import me.hypherionmc.hyperlighting.common.items.ColoredWaterBottle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileFogMachine extends BlockEntity implements ISidedTickable, IFluidHandler {

    private final ItemStackHandler itemStackHandler = new StackHandler(1);
    private final LazyOptional<IItemHandler> storage = LazyOptional.of(() -> itemStackHandler);

    public boolean autoFireEnabled = false;
    public boolean isCooldown = false;
    public boolean fireMachine = false;
    public int timer = 0;
    public int fireLength = 120;

    public int autoFireTimer = 0;
    public int autoFireTime = 1000;

    public int cooldownTimer = 0;
    public final int cooldownTime = 1200;
    public int cooldownTimeRemaining = 600;

    private final FluidTank tank = new FluidTank(5000) {
        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid() instanceof ColoredWater || stack.getFluid() == Fluids.WATER;
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return stack.getFluid() instanceof ColoredWater || stack.getFluid() == Fluids.WATER;
        }
    };
    private LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> tank);

    public TileFogMachine(BlockPos pos, BlockState state) {
        super(HLTileEntities.TILE_FOG_MACHINE.get(), pos, state);
    }

    @Override
    public void tickServer() {
        if (isFiring()) this.timer++;
        autoFireTimer = autoFireEnabled ? autoFireTimer + 1 : 0;
        if (!isCooldown) this.cooldownTimer++;

        if (this.timer > this.fireLength) {
            this.fireMachine = false;
            this.timer = 0;
        }

        if (this.autoFireTimer > autoFireTime && autoFireEnabled) {
            this.fireMachine = true;
            this.autoFireTimer = 0;
        }

        if (isFiring()) {
            this.tank.drain(1, FluidAction.EXECUTE);
        }

        if (this.cooldownTimer > this.cooldownTime) {
            this.isCooldown = true;
            this.cooldownTimer = 0;
        }

        if (this.isCooldown && cooldownTimeRemaining > 0) {
            this.cooldownTimeRemaining--;
        } else {
            this.cooldownTimeRemaining = this.cooldownTime;
            this.isCooldown = false;
        }

        this.sendUpdates();
    }

    @Override
    public void tickClient() {
        if (isFiring()) {
            addParticles();
        }
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.fireMachine = pTag.getBoolean("isfiring");
        this.timer = pTag.getInt("firetimer");
        this.autoFireTimer = pTag.getInt("autofiretimer");
        isCooldown = pTag.getBoolean("iscooldown");
        cooldownTimer = pTag.getInt("cooldowntimer");
        cooldownTimeRemaining = pTag.getInt("cooldowntimeremain");
        autoFireEnabled = pTag.getBoolean("autofireenabled");
        this.autoFireTime = pTag.getInt("autofiretime");
        this.tank.readFromNBT(pTag);
        this.itemStackHandler.deserializeNBT(pTag.getCompound("dye"));
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putBoolean("isfiring", this.fireMachine);
        pTag.putInt("firetimer", this.timer);
        pTag.putInt("autofiretimer", this.autoFireTimer);
        pTag.putBoolean("iscooldown", isCooldown);
        pTag.putInt("cooldowntimer", cooldownTimer);
        pTag.putInt("cooldowntimeremain", cooldownTimeRemaining);
        pTag.putBoolean("autofireenabled", autoFireEnabled);
        pTag.putInt("autofiretime", autoFireTime);
        this.tank.writeToNBT(pTag);
        pTag.put("dye", this.itemStackHandler.serializeNBT());
    }

    private void sendUpdates() {
        level.setBlocksDirty(worldPosition, this.getBlockState(), this.getBlockState());
        level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 3);
        setChanged();
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, (blockEntity) -> this.getUpdateTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        handleUpdateTag(pkt.getTag());
    }

    public boolean isFiring() {
        return fireMachine && canFire();
    }

    public void setFireMachine(boolean fireMachine) {
        this.fireMachine = fireMachine;
    }

    public boolean canFire() {
        return tank.getFluidAmount() > 0 && !isCooldown;
    }

    public int getFluidLevelGui() {
        return (int) (((float)this.tank.getFluidAmount() / this.tank.getCapacity()) * 24);
    }

    public boolean isCooldown() {
        return isCooldown;
    }

    public boolean isAutoFireEnabled() {
        return autoFireEnabled;
    }

    public void setAutoFireEnabled(Boolean enabled) {
        autoFireEnabled = enabled;
        this.sendUpdates();
    }

    public int getAutoFireTime() {
        return autoFireTime;
    }

    public void setAutoFireTime(int autoFireTime) {
        this.autoFireTime = autoFireTime;
        this.sendUpdates();
    }

    @Override
    public int getTanks() {
        return tank.getTanks();
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return this.tank.getFluidInTank(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        return this.tank.getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return stack.getFluid() instanceof ColoredWater || stack.getFluid() == Fluids.WATER;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return this.tank.fill(resource, action);
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return this.tank.drain(resource, action);
    }

    @NotNull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return this.tank.drain(maxDrain, action);
    }

    public boolean hasFluid() {
        return this.tank.getFluidAmount() > 0;
    }

    static class StackHandler extends ItemStackHandler {

        public StackHandler(int size) {
            super(size);
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return stack.getItem() instanceof DyeItem || stack.getItem() instanceof ColoredWaterBottle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void addParticles() {
        BlockState state = level.getBlockState(getBlockPos());
        Direction facing = state.getValue(FogMachineBlock.FACING);

        RegistryObject<SimpleParticleType> PARTICLE;

        if (tank.getFluid().getFluid() instanceof ColoredWater coloredWater) {
            PARTICLE = ParticleRegistryHandler.FOG_MACHINE_PARTICLES.get(coloredWater.getColor());
        } else if (itemStackHandler.getStackInSlot(0).isEmpty()) {
            PARTICLE = ParticleRegistryHandler.FOG_MACHINE_PARTICLES.get(DyeColor.WHITE);
        } else {
            PARTICLE = ParticleRegistryHandler.FOG_MACHINE_PARTICLES.get(((DyeItem)itemStackHandler.getStackInSlot(0).getItem()).getDyeColor());
        }

        for(int i = 0; i <= 4; ++i) {

            level.addParticle(PARTICLE.get(), (double)getBlockPos().getX() + 2 + level.random.nextGaussian(), (double)getBlockPos().getY() + 0.4D, (double)getBlockPos().getZ() + level.random.nextGaussian(), (double)(level.random.nextFloat() - 0.5F) * 0.03D, (double)(level.random.nextFloat() - 0.5F) * 0.03D, (double)(level.random.nextFloat() - 0.5F) * 0.03D);
            level.addParticle(PARTICLE.get(), (double)getBlockPos().getX() + 2 + level.random.nextGaussian(), (double)getBlockPos().getY() + 0.4D, (double)getBlockPos().getZ() + level.random.nextGaussian(), (double)(level.random.nextFloat() - 0.5F) * 0.03D, (double)(level.random.nextFloat() - 0.5F) * 0.03D, (double)(level.random.nextFloat() - 0.5F) * 0.03D);

            if (facing == Direction.NORTH) {
                level.addParticle(PARTICLE.get(), (double)getBlockPos().getX() + level.random.nextGaussian(), (double)getBlockPos().getY() + 0.4D, (double)getBlockPos().getZ() - 1.5 + level.random.nextGaussian(), (double)(level.random.nextFloat() - 0.5F) * 0.03D, (double)(level.random.nextFloat()) * 0.03D, (double)(level.random.nextFloat() - 0.5F) * 0.03D);
                level.addParticle(PARTICLE.get(), (double)getBlockPos().getX() + level.random.nextGaussian(), (double)getBlockPos().getY() + 0.7D, (double)getBlockPos().getZ() - 1.5 + level.random.nextGaussian(), (double)(level.random.nextFloat() - 0.5F) * 0.03D, (double)(level.random.nextFloat()) * 0.03D, (double)(level.random.nextFloat() - 0.5F) * 0.03D);
            } else if (facing == Direction.SOUTH) {
                level.addParticle(PARTICLE.get(), (double)getBlockPos().getX() + level.random.nextGaussian(), (double)getBlockPos().getY() + 0.4D, (double)getBlockPos().getZ() + 2 + level.random.nextGaussian(), (double)(level.random.nextFloat() - 0.5F) * 0.03D, (double)(level.random.nextFloat()) * 0.03D, (double)(level.random.nextFloat() - 0.5F) * 0.03D);
                level.addParticle(PARTICLE.get(), (double)getBlockPos().getX() + level.random.nextGaussian(), (double)getBlockPos().getY() + 0.7D, (double)getBlockPos().getZ() + 2 + level.random.nextGaussian(), (double)(level.random.nextFloat() - 0.5F) * 0.03D, (double)(level.random.nextFloat()) * 0.03D, (double)(level.random.nextFloat() - 0.5F) * 0.03D);
            } else if (facing == Direction.WEST) {
                level.addParticle(PARTICLE.get(), (double)getBlockPos().getX() - 1.5 + level.random.nextGaussian(), (double)getBlockPos().getY() + 0.4D, (double)getBlockPos().getZ() + level.random.nextGaussian(), (double)(level.random.nextFloat() - 0.5F) * 0.03D, (double)(level.random.nextFloat()) * 0.03D, (double)(level.random.nextFloat() - 0.5F) * 0.03D);
                level.addParticle(PARTICLE.get(), (double)getBlockPos().getX() - 1.5 + level.random.nextGaussian(), (double)getBlockPos().getY() + 0.7D, (double)getBlockPos().getZ() + level.random.nextGaussian(), (double)(level.random.nextFloat() - 0.5F) * 0.03D, (double)(level.random.nextFloat()) * 0.03D, (double)(level.random.nextFloat() - 0.5F) * 0.03D);
            } else if (facing == Direction.EAST) {
                level.addParticle(PARTICLE.get(), (double)getBlockPos().getX() + 2 + level.random.nextGaussian(), (double)getBlockPos().getY() + 0.4D, (double)getBlockPos().getZ() + level.random.nextGaussian(), (double)(level.random.nextFloat() - 0.5F) * 0.03D, (double)(level.random.nextFloat()) * 0.03D, (double)(level.random.nextFloat() - 0.5F) * 0.03D);
                level.addParticle(PARTICLE.get(), (double)getBlockPos().getX() + 2 + level.random.nextGaussian(), (double)getBlockPos().getY() + 0.7D, (double)getBlockPos().getZ() + level.random.nextGaussian(), (double)(level.random.nextFloat() - 0.5F) * 0.03D, (double)(level.random.nextFloat()) * 0.03D, (double)(level.random.nextFloat() - 0.5F) * 0.03D);
            }
        }

        addFireParticles();
    }

    @OnlyIn(Dist.CLIENT)
    private void addFireParticles() {
        BlockState state = level.getBlockState(getBlockPos());
        Direction facing = state.getValue(FogMachineBlock.FACING);
        if (facing == Direction.NORTH) {
            level.addParticle(ParticleTypes.SMOKE, getBlockPos().getX() + 0.5, getBlockPos().getY(), getBlockPos().getZ() + 0.1, 0, 0, -0.1);
        } else if (facing == Direction.SOUTH) {
            level.addParticle(ParticleTypes.SMOKE, getBlockPos().getX() + 0.5, getBlockPos().getY(), getBlockPos().getZ() + 0.9, 0, 0, 0.1);
        } else if (facing == Direction.WEST) {
            level.addParticle(ParticleTypes.SMOKE, getBlockPos().getX() + 0.1, getBlockPos().getY(), getBlockPos().getZ() + 0.5, -0.1, 0, 0);
        } else if (facing == Direction.EAST) {
            level.addParticle(ParticleTypes.SMOKE, getBlockPos().getX() + 0.9, getBlockPos().getY(), getBlockPos().getZ() + 0.5, 0.1, 0, 0);
        }
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return this.storage.cast();
        }
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return this.fluidHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    public FluidTank getTank() {
        return tank;
    }
}
