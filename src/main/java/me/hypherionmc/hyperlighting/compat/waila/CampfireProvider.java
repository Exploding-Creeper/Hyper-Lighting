package me.hypherionmc.hyperlighting.compat.waila;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;

public class CampfireProvider implements IBlockComponentProvider {

    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        NbtCompound compound = accessor.getServerData().getCompound("hl_campfire");
        int[] cookingTimes = new int[4];
        int[] cookingTotalTimes = new int[4];
        DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);

        inventory.clear();
        Inventories.readNbt(compound, inventory);
        if (compound.contains("CookingTimes", 11)) {
            int[] aint = compound.getIntArray("CookingTimes");
            System.arraycopy(aint, 0, cookingTimes, 0, Math.min(cookingTotalTimes.length, aint.length));
        }

        if (compound.contains("CookingTotalTimes", 11)) {
            int[] aint1 = compound.getIntArray("CookingTotalTimes");
            System.arraycopy(aint1, 0, cookingTotalTimes, 0, Math.min(cookingTotalTimes.length, aint1.length));
        }

        if (inventory.isEmpty()) {
            tooltip.add(new LiteralText(Formatting.RED + "Empty"));
        } else {
            for (int i = 0; i < inventory.size(); i++) {
                if (!inventory.get(i).isEmpty()) {
                    int progress = (int) ((float) cookingTimes[i] / cookingTotalTimes[i] * 100);
                    tooltip.add(new LiteralText(inventory.get(i).getName().getString() + " : " + Formatting.YELLOW + progress + "%"));
                }
            }
        }
    }
}
