package me.hypherionmc.hyperlighting.common.blockentities;

import me.hypherionmc.hyperlighting.api.ISidedTickable;
import me.hypherionmc.hyperlighting.api.fluid.FluidStorageTank;
import me.hypherionmc.hyperlighting.common.blocks.FogMachineBlock;
import me.hypherionmc.hyperlighting.common.fluids.ColoredWater;
import me.hypherionmc.hyperlighting.common.handlers.ParticleRegistryHandler;
import me.hypherionmc.hyperlighting.common.handlers.screen.FogMachineScreenHandler;
import me.hypherionmc.hyperlighting.common.init.HLBlockEntities;
import me.hypherionmc.hyperlighting.common.inventory.ImplementedInventory;
import me.hypherionmc.hyperlighting.mixin.coloredwater.BucketItemAccessorMixin;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.impl.transfer.fluid.FluidVariantImpl;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.BucketItem;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Clearable;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class FogMachineBlockEntity extends BlockEntity implements Clearable, ISidedTickable, ImplementedInventory, ExtendedScreenHandlerFactory {

    public final int cooldownTime = 1200;
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private final FluidStorageTank tank = new FluidStorageTank(FluidConstants.BUCKET * 5, (fluidVariant -> {
        return fluidVariant.getFluid() == Fluids.WATER || fluidVariant.getFluid() instanceof ColoredWater;
    }));
    public boolean autoFireEnabled = false;
    public boolean isCooldown = false;
    public boolean fireMachine = false;
    public int timer = 0;
    public int fireLength = 120;
    public int autoFireTimer = 0;
    public int autoFireTime = 1000;
    public int cooldownTimer = 0;
    public int cooldownTimeRemaining = 600;

    public FogMachineBlockEntity(BlockPos pos, BlockState state) {
        super(HLBlockEntities.TILE_FOG_MACHINE, pos, state);
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
            this.tank.extract(this.tank.getResource(), 1, null);
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
    public void readNbt(NbtCompound pTag) {
        super.readNbt(pTag);
        this.fireMachine = pTag.getBoolean("isfiring");
        this.timer = pTag.getInt("firetimer");
        this.autoFireTimer = pTag.getInt("autofiretimer");
        isCooldown = pTag.getBoolean("iscooldown");
        cooldownTimer = pTag.getInt("cooldowntimer");
        cooldownTimeRemaining = pTag.getInt("cooldowntimeremain");
        autoFireEnabled = pTag.getBoolean("autofireenabled");
        this.autoFireTime = pTag.getInt("autofiretime");
        this.tank.readNbt(pTag);
        this.inventory.clear();
        Inventories.readNbt(pTag, this.inventory);
    }

    @Override
    protected void writeNbt(NbtCompound pTag) {
        super.writeNbt(pTag);
        pTag.putBoolean("isfiring", this.fireMachine);
        pTag.putInt("firetimer", this.timer);
        pTag.putInt("autofiretimer", this.autoFireTimer);
        pTag.putBoolean("iscooldown", isCooldown);
        pTag.putInt("cooldowntimer", cooldownTimer);
        pTag.putInt("cooldowntimeremain", cooldownTimeRemaining);
        pTag.putBoolean("autofireenabled", autoFireEnabled);
        pTag.putInt("autofiretime", autoFireTime);
        this.tank.writeNbt(pTag);
        Inventories.writeNbt(pTag, this.inventory, true);
    }

    private void sendUpdates() {
        this.markDirty();
        this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
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

    public boolean isFiring() {
        return fireMachine && canFire();
    }

    public void setFireMachine(boolean fireMachine) {
        this.fireMachine = fireMachine;
    }

    public boolean canFire() {
        return tank.getAmount() > 0 && !isCooldown;
    }

    public int getFluidLevelGui() {
        return (int) (((float) this.tank.getAmount() / this.tank.getCapacity()) * 24);
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

    public boolean hasFluid() {
        return this.tank.getAmount() > 0;
    }

    private void addParticles() {
        BlockState state = world.getBlockState(pos);
        Direction facing = state.get(FogMachineBlock.FACING);

        DefaultParticleType PARTICLE;

        if (tank.getResource().getFluid() instanceof ColoredWater coloredWater) {
            PARTICLE = ParticleRegistryHandler.FOG_MACHINE_PARTICLES.get(coloredWater.getColor());
        } else if (inventory.get(0).isEmpty()) {
            PARTICLE = ParticleRegistryHandler.FOG_MACHINE_PARTICLES.get(DyeColor.WHITE);
        } else {
            PARTICLE = ParticleRegistryHandler.FOG_MACHINE_PARTICLES.get(((DyeItem) inventory.get(0).getItem()).getColor());
        }

        for (int i = 0; i <= 4; ++i) {

            world.addParticle(PARTICLE, (double) getPos().getX() + 2 + world.random.nextGaussian(), (double) getPos().getY() + 0.4D, (double) getPos().getZ() + world.random.nextGaussian(), (double) (world.random.nextFloat() - 0.5F) * 0.03D, (double) (world.random.nextFloat() - 0.5F) * 0.03D, (double) (world.random.nextFloat() - 0.5F) * 0.03D);
            world.addParticle(PARTICLE, (double) getPos().getX() + 2 + world.random.nextGaussian(), (double) getPos().getY() + 0.4D, (double) getPos().getZ() + world.random.nextGaussian(), (double) (world.random.nextFloat() - 0.5F) * 0.03D, (double) (world.random.nextFloat() - 0.5F) * 0.03D, (double) (world.random.nextFloat() - 0.5F) * 0.03D);

            if (facing == Direction.NORTH) {
                world.addParticle(PARTICLE, (double) getPos().getX() + world.random.nextGaussian(), (double) getPos().getY() + 0.4D, (double) getPos().getZ() - 1.5 + world.random.nextGaussian(), (double) (world.random.nextFloat() - 0.5F) * 0.03D, (double) (world.random.nextFloat()) * 0.03D, (double) (world.random.nextFloat() - 0.5F) * 0.03D);
                world.addParticle(PARTICLE, (double) getPos().getX() + world.random.nextGaussian(), (double) getPos().getY() + 0.7D, (double) getPos().getZ() - 1.5 + world.random.nextGaussian(), (double) (world.random.nextFloat() - 0.5F) * 0.03D, (double) (world.random.nextFloat()) * 0.03D, (double) (world.random.nextFloat() - 0.5F) * 0.03D);
            } else if (facing == Direction.SOUTH) {
                world.addParticle(PARTICLE, (double) getPos().getX() + world.random.nextGaussian(), (double) getPos().getY() + 0.4D, (double) getPos().getZ() + 2 + world.random.nextGaussian(), (double) (world.random.nextFloat() - 0.5F) * 0.03D, (double) (world.random.nextFloat()) * 0.03D, (double) (world.random.nextFloat() - 0.5F) * 0.03D);
                world.addParticle(PARTICLE, (double) getPos().getX() + world.random.nextGaussian(), (double) getPos().getY() + 0.7D, (double) getPos().getZ() + 2 + world.random.nextGaussian(), (double) (world.random.nextFloat() - 0.5F) * 0.03D, (double) (world.random.nextFloat()) * 0.03D, (double) (world.random.nextFloat() - 0.5F) * 0.03D);
            } else if (facing == Direction.WEST) {
                world.addParticle(PARTICLE, (double) getPos().getX() - 1.5 + world.random.nextGaussian(), (double) getPos().getY() + 0.4D, (double) getPos().getZ() + world.random.nextGaussian(), (double) (world.random.nextFloat() - 0.5F) * 0.03D, (double) (world.random.nextFloat()) * 0.03D, (double) (world.random.nextFloat() - 0.5F) * 0.03D);
                world.addParticle(PARTICLE, (double) getPos().getX() - 1.5 + world.random.nextGaussian(), (double) getPos().getY() + 0.7D, (double) getPos().getZ() + world.random.nextGaussian(), (double) (world.random.nextFloat() - 0.5F) * 0.03D, (double) (world.random.nextFloat()) * 0.03D, (double) (world.random.nextFloat() - 0.5F) * 0.03D);
            } else if (facing == Direction.EAST) {
                world.addParticle(PARTICLE, (double) getPos().getX() + 2 + world.random.nextGaussian(), (double) getPos().getY() + 0.4D, (double) getPos().getZ() + world.random.nextGaussian(), (double) (world.random.nextFloat() - 0.5F) * 0.03D, (double) (world.random.nextFloat()) * 0.03D, (double) (world.random.nextFloat() - 0.5F) * 0.03D);
                world.addParticle(PARTICLE, (double) getPos().getX() + 2 + world.random.nextGaussian(), (double) getPos().getY() + 0.7D, (double) getPos().getZ() + world.random.nextGaussian(), (double) (world.random.nextFloat() - 0.5F) * 0.03D, (double) (world.random.nextFloat()) * 0.03D, (double) (world.random.nextFloat() - 0.5F) * 0.03D);
            }
        }

        addFireParticles();
    }

    private void addFireParticles() {
        BlockState state = world.getBlockState(pos);
        Direction facing = state.get(FogMachineBlock.FACING);
        if (facing == Direction.NORTH) {
            world.addParticle(ParticleTypes.SMOKE, getPos().getX() + 0.5, getPos().getY(), getPos().getZ() + 0.1, 0, 0, -0.1);
        } else if (facing == Direction.SOUTH) {
            world.addParticle(ParticleTypes.SMOKE, getPos().getX() + 0.5, getPos().getY(), getPos().getZ() + 0.9, 0, 0, 0.1);
        } else if (facing == Direction.WEST) {
            world.addParticle(ParticleTypes.SMOKE, getPos().getX() + 0.1, getPos().getY(), getPos().getZ() + 0.5, -0.1, 0, 0);
        } else if (facing == Direction.EAST) {
            world.addParticle(ParticleTypes.SMOKE, getPos().getX() + 0.9, getPos().getY(), getPos().getZ() + 0.5, 0.1, 0, 0);
        }
    }

    public FluidStorageTank getTank() {
        return tank;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public BlockEntity getBlockEntity() {
        return this;
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.getPos());
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new FogMachineScreenHandler(syncId, inv, this);
    }

    public boolean onPlayerUse(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.getItem() instanceof BucketItem bucketItem) {
            FluidVariant fluidVariant = new FluidVariantImpl(((BucketItemAccessorMixin) bucketItem).getFluid(), stack.getNbt());
            if (fluidVariant.isBlank() || !(fluidVariant.getFluid() instanceof ColoredWater || fluidVariant.getFluid() == Fluids.WATER))
                return false;
            if (this.tank.insert(fluidVariant, FluidConstants.BUCKET, null) > 0) {
                player.world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
                return true;
            }
        }
        return false;
    }

    public DyeColor getColor() {
        if (tank.getResource().getFluid() instanceof ColoredWater coloredWater) {
            return coloredWater.getColor();
        } else if (inventory.get(0).isEmpty()) {
            return DyeColor.WHITE;
        } else {
            return ((DyeItem) inventory.get(0).getItem()).getColor();
        }
    }
}
