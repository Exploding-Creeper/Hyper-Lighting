package me.hypherionmc.hyperlighting.common.blockentities;

import me.hypherionmc.hyperlighting.common.blocks.CampFireBlock;
import me.hypherionmc.hyperlighting.common.init.HLBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Clearable;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Random;

public class CampFireBlockEntity extends BlockEntity implements Clearable {

    public final int[] cookingTimes = new int[4];
    public final int[] cookingTotalTimes = new int[4];
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);

    public CampFireBlockEntity(BlockPos pos, BlockState state) {
        super(HLBlockEntities.TILE_CAMPFIRE, pos, state);
    }

    public void serverTick() {
        boolean flag = this.getCachedState().get(CampFireBlock.LIT);
        if (flag) {
            this.cookAndDrop();
        } else {
            for (int i = 0; i < this.inventory.size(); ++i) {
                if (this.cookingTimes[i] > 0) {
                    this.cookingTimes[i] = MathHelper.clamp(this.cookingTimes[i] - 2, 0, this.cookingTotalTimes[i]);
                }
            }
        }
    }

    public void clientTick() {
        if (this.getCachedState().get(CampFireBlock.LIT)) {
            this.addParticles();
        }
    }

    @Override
    public BlockPos getPos() {
        return this.pos;
    }

    private void cookAndDrop() {
        for (int i = 0; i < this.inventory.size(); ++i) {
            ItemStack itemstack = this.inventory.get(i);
            if (!itemstack.isEmpty()) {
                int j = this.cookingTimes[i]++;
                if (this.cookingTimes[i] >= this.cookingTotalTimes[i]) {
                    Inventory iinventory = new SimpleInventory(itemstack);
                    ItemStack itemstack1 = this.world.getRecipeManager().getFirstMatch(RecipeType.CAMPFIRE_COOKING, iinventory, this.world).map((campfireRecipe) -> {
                        return campfireRecipe.craft(iinventory);
                    }).orElse(itemstack);
                    BlockPos blockpos = this.pos;
                    ItemScatterer.spawn(this.world, blockpos.getX(), blockpos.getY(), blockpos.getZ(), itemstack1);
                    this.inventory.set(i, ItemStack.EMPTY);
                    this.inventoryChanged();
                }
            }
        }

    }

    private void addParticles() {
        World world = this.getWorld();
        if (world != null) {
            BlockPos blockpos = this.pos;
            Random random = world.random;
            if (random.nextFloat() < 0.11F) {
                for (int i = 0; i < random.nextInt(2) + 2; ++i) {
                    CampFireBlock.spawnSmokeParticles(world, blockpos, this.getCachedState().get(CampFireBlock.SIGNAL_FIRE), false);
                }
            }

            int l = this.getCachedState().get(CampFireBlock.FACING).getHorizontal();

            for (int j = 0; j < this.inventory.size(); ++j) {
                if (!this.inventory.get(j).isEmpty() && random.nextFloat() < 0.2F) {
                    Direction direction = Direction.fromHorizontal(Math.floorMod(j + l, 4));
                    float f = 0.3125F;
                    double d0 = (double) blockpos.getX() + 0.5D - (double) ((float) direction.getOffsetX() * 0.3125F) + (double) ((float) direction.rotateYClockwise().getOffsetX() * 0.3125F);
                    double d1 = (double) blockpos.getY() + 0.5D;
                    double d2 = (double) blockpos.getZ() + 0.5D - (double) ((float) direction.getOffsetZ() * 0.3125F) + (double) ((float) direction.rotateYClockwise().getOffsetZ() * 0.3125F);

                    for (int k = 0; k < 4; ++k) {
                        world.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 5.0E-4D, 0.0D);
                    }
                }
            }

        }
    }

    /**
     * Returns a NonNullList<ItemStack> of items currently held in the campfire.
     */
    public DefaultedList<ItemStack> getInventory() {
        return this.inventory;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory.clear();
        Inventories.readNbt(nbt, this.inventory);
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
    public void writeNbt(NbtCompound compound) {
        this.writeItems(compound);
        compound.putIntArray("CookingTimes", this.cookingTimes);
        compound.putIntArray("CookingTotalTimes", this.cookingTotalTimes);
    }

    private void writeItems(NbtCompound compound) {
        super.writeNbt(compound);
        Inventories.writeNbt(compound, this.inventory, true);
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

    public Optional<CampfireCookingRecipe> findMatchingRecipe(ItemStack itemStackIn) {
        return this.inventory.stream().noneMatch(ItemStack::isEmpty) ? Optional.empty() : this.world.getRecipeManager().getFirstMatch(RecipeType.CAMPFIRE_COOKING, new SimpleInventory(itemStackIn), this.world);
    }

    public boolean addItem(ItemStack itemStackIn, int cookTime) {
        for (int i = 0; i < this.inventory.size(); ++i) {
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
        this.markDirty();
        this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
    }

    public void dropAllItems() {
        if (this.world != null) {
            if (!this.world.isClient) {
                ItemScatterer.spawn(this.world, this.pos, this.getInventory());
            }

            this.inventoryChanged();
        }
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

}
