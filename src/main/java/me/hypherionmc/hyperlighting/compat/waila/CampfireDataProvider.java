package me.hypherionmc.hyperlighting.compat.waila;

import mcp.mobius.waila.api.IServerDataProvider;
import me.hypherionmc.hyperlighting.common.blockentities.CampFireBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class CampfireDataProvider implements IServerDataProvider<CampFireBlockEntity> {

    @Override
    public void appendServerData(NbtCompound data, ServerPlayerEntity player, World world, CampFireBlockEntity campFireBlockEntity) {
        NbtCompound compound = new NbtCompound();
        campFireBlockEntity.writeNbt(compound);
        data.put("hl_campfire", compound);
    }
}
