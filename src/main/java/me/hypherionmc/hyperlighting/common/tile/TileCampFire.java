package me.hypherionmc.hyperlighting.common.tile;

import me.hypherionmc.hyperlighting.common.blocks.CampFireBlock;
import me.hypherionmc.hyperlighting.common.init.HLTileEntities;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;

public class TileCampFire extends BlockEntity implements Clearable {

    private final NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);
    public final int[] cookingTimes = new int[4];
    public final int[] cookingTotalTimes = new int[4];

    public TileCampFire(BlockPos pos, BlockState state) {
        super(HLTileEntities.TILE_CAMPFIRE.get(), pos, state);
    }

    public void serverTick() {
        boolean flag = this.getBlockState().getValue(CampFireBlock.LIT);

        if (flag) {
            this.cookAndDrop();
        } else {
            for(int i = 0; i < this.inventory.size(); ++i) {
                if (this.cookingTimes[i] > 0) {
                    this.cookingTimes[i] = Mth.clamp(this.cookingTimes[i] - 2, 0, this.cookingTotalTimes[i]);
                }
            }
        }

    }

    public void clientTick() {
        this.addParticles();
    }

    @Override
    public BlockPos getBlockPos() {
        return this.worldPosition;
    }

    /**
     * Individually tracks the cooking of each item, then spawns the finished product in-world and clears the
     * corresponding held item.
     */
    private void cookAndDrop() {
        for(int i = 0; i < this.inventory.size(); ++i) {
            ItemStack itemstack = this.inventory.get(i);
            if (!itemstack.isEmpty()) {
                int j = this.cookingTimes[i]++;
                if (this.cookingTimes[i] >= this.cookingTotalTimes[i]) {
                    Container iinventory = new SimpleContainer(itemstack);
                    ItemStack itemstack1 = this.level.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, iinventory, this.level).map((campfireRecipe) -> {
                        return campfireRecipe.assemble(iinventory);
                    }).orElse(itemstack);
                    BlockPos blockpos = this.getBlockPos();
                    Containers.dropItemStack(this.level, (double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ(), itemstack1);
                    this.inventory.set(i, ItemStack.EMPTY);
                    this.inventoryChanged();
                }
            }
        }

    }

    private void addParticles() {
        Level world = this.getLevel();
        if (world != null) {
            BlockPos blockpos = this.getBlockPos();
            Random random = world.random;
            if (random.nextFloat() < 0.11F) {
                for(int i = 0; i < random.nextInt(2) + 2; ++i) {
                    CampFireBlock.spawnSmokeParticles(world, blockpos, this.getBlockState().getValue(CampFireBlock.SIGNAL_FIRE), false);
                }
            }

            int l = this.getBlockState().getValue(CampFireBlock.FACING).get2DDataValue();

            for(int j = 0; j < this.inventory.size(); ++j) {
                if (!this.inventory.get(j).isEmpty() && random.nextFloat() < 0.2F) {
                    Direction direction = Direction.from2DDataValue(Math.floorMod(j + l, 4));
                    float f = 0.3125F;
                    double d0 = (double)blockpos.getX() + 0.5D - (double)((float)direction.getStepX() * 0.3125F) + (double)((float)direction.getClockWise().getStepX() * 0.3125F);
                    double d1 = (double)blockpos.getY() + 0.5D;
                    double d2 = (double)blockpos.getZ() + 0.5D - (double)((float)direction.getStepZ() * 0.3125F) + (double)((float)direction.getClockWise().getStepZ() * 0.3125F);

                    for(int k = 0; k < 4; ++k) {
                        world.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 5.0E-4D, 0.0D);
                    }
                }
            }

        }
    }

    /**
     * Returns a NonNullList<ItemStack> of items currently held in the campfire.
     */
    public NonNullList<ItemStack> getInventory() {
        return this.inventory;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.inventory.clear();
        ContainerHelper.loadAllItems(nbt, this.inventory);
        if (nbt.contains("CookingTimes", 11)) {
            int[] aint = nbt.getIntArray("CookingTimes");
            System.arraycopy(aint, 0, this.cookingTimes, 0, Math.min(this.cookingTotalTimes.length, aint.length));
        }

        if (nbt.contains("CookingTotalTimes", 11)) {
            int[] aint1 = nbt.getIntArray("CookingTotalTimes");
            System.arraycopy(aint1, 0, this.cookingTotalTimes, 0, Math.min(this.cookingTotalTimes.length, aint1.length));
        }

    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        this.writeItems(compound);
        compound.putIntArray("CookingTimes", this.cookingTimes);
        compound.putIntArray("CookingTotalTimes", this.cookingTotalTimes);
        return compound;
    }

    private CompoundTag writeItems(CompoundTag compound) {
        super.save(compound);
        ContainerHelper.saveAllItems(compound, this.inventory, true);
        return compound;
    }

    /**
     * Retrieves packet to send to the client whenever this Tile Entity is resynced via World.notifyBlockUpdate. For
     * modded TE's, this packet comes back to you clientside in {@link #onDataPacket}
     */
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, (blockEntity -> this.getUpdateTag()));
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.writeItems(new CompoundTag());
    }

    public Optional<CampfireCookingRecipe> findMatchingRecipe(ItemStack itemStackIn) {
        return this.inventory.stream().noneMatch(ItemStack::isEmpty) ? Optional.empty() : this.level.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, new SimpleContainer(itemStackIn), this.level);
    }

    public boolean addItem(ItemStack itemStackIn, int cookTime) {
        for(int i = 0; i < this.inventory.size(); ++i) {
            ItemStack itemstack = this.inventory.get(i);
            if (itemstack.isEmpty()) {
                this.cookingTotalTimes[i] = cookTime;
                this.cookingTimes[i] = 0;
                this.inventory.set(i, itemStackIn.split(1));
                this.inventoryChanged();
                return true;
            }
        }

        return false;
    }

    private void inventoryChanged() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    public void clearContent() {
        this.inventory.clear();
    }

    public void dropAllItems() {
        if (this.level != null) {
            if (!this.level.isClientSide) {
                Containers.dropContents(this.level, this.getBlockPos(), this.getInventory());
            }

            this.inventoryChanged();
        }
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
    }

    @Override
    public CompoundTag serializeNBT() {
        return super.serializeNBT();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        this.handleUpdateTag(pkt.getTag());
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

}
