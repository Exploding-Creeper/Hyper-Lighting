package me.hypherionmc.hyperlighting.common.integration.top.overrides;

import mcjty.theoneprobe.api.*;
import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.common.integration.top.TOPIntegration;
import me.hypherionmc.hyperlighting.common.tile.TileCampFire;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;

import static mcjty.theoneprobe.api.IProbeConfig.ConfigMode.EXTENDED;
import static mcjty.theoneprobe.api.IProbeConfig.ConfigMode.NORMAL;
import static mcjty.theoneprobe.api.TextStyleClass.MODNAME;

public class TOPCampfireInfoProvider implements IBlockDisplayOverride {


    @Override
    public boolean overrideStandardInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, PlayerEntity playerEntity, World world, BlockState blockState, IProbeHitData iProbeHitData) {
       IProbeConfig config = TOPIntegration.getProbeConfig();

        if (probeMode != ProbeMode.DEBUG) {

            if (world.getTileEntity(iProbeHitData.getPos()) instanceof TileCampFire) {
                TileCampFire tileCampFire = (TileCampFire) world.getTileEntity(iProbeHitData.getPos());

                String modName = ModList.get().getModContainerById(ModConstants.MODID).get().getModInfo().getDisplayName();
                iProbeInfo.horizontal().item(iProbeHitData.getPickBlock()).vertical().text(CompoundText.create().name(blockState.getBlock().getTranslatedName())).vertical().text(CompoundText.create().info(MODNAME + modName));

                for (int i = 0; i < tileCampFire.getInventory().size(); i++) {
                    ItemStack stack = tileCampFire.getInventory().get(i);

                    if (!stack.isEmpty()) {
                        iProbeInfo.horizontal().item(stack).horizontal().progress((int) ((float)tileCampFire.cookingTimes[i] / tileCampFire.cookingTotalTimes[i] * 100), 100, iProbeInfo.defaultProgressStyle().suffix(" %").alignment(ElementAlignment.ALIGN_TOPLEFT));
                    }
                }

                return true;
            }

        }
        return false;
    }

    private boolean show(ProbeMode mode, IProbeConfig.ConfigMode cfg) {
        return cfg == NORMAL || (cfg == EXTENDED && mode == ProbeMode.EXTENDED);
    }
}
