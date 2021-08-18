package me.hypherionmc.hyperlighting.common.integration.top.overrides;

/*public class TOPBlockInfoProvider implements IBlockDisplayOverride {

    @Override
    public boolean overrideStandardInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, Player playerEntity, Level world, BlockState blockState, IProbeHitData iProbeHitData) {

        if (probeMode != ProbeMode.DEBUG) {

            if (world.getBlockState(iProbeHitData.getPos()).getBlock() instanceof CustomProbeInfo) {

                String modName = ModList.get().getModContainerById(ModConstants.MODID).get().getModInfo().getDisplayName();
                iProbeInfo.horizontal().item(iProbeHitData.getPickBlock()).vertical().text(CompoundText.create().name(blockState.getBlock().getName())).vertical().text(CompoundText.create().info(MODNAME + modName));

                iProbeInfo = ((CustomProbeInfo) world.getBlockState(iProbeHitData.getPos()).getBlock()).getCustomProbeInfo(probeMode, playerEntity, world, blockState, iProbeHitData);
                return true;
            }
        }

        return false;
    }

}*/
