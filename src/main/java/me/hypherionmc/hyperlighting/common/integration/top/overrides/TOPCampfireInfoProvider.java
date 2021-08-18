package me.hypherionmc.hyperlighting.common.integration.top.overrides;

/*public class TOPCampfireInfoProvider implements IBlockDisplayOverride {


    @Override
    public boolean overrideStandardInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, Player playerEntity, Level world, BlockState blockState, IProbeHitData iProbeHitData) {
       IProbeConfig config = TOPIntegration.getProbeConfig();

        if (probeMode != ProbeMode.DEBUG) {

            if (world.getBlockEntity(iProbeHitData.getPos()) instanceof TileCampFire) {
                TileCampFire tileCampFire = (TileCampFire) world.getBlockEntity(iProbeHitData.getPos());

                String modName = ModList.get().getModContainerById(ModConstants.MODID).get().getModInfo().getDisplayName();
                iProbeInfo.horizontal().item(iProbeHitData.getPickBlock()).vertical().text(CompoundText.create().name(blockState.getBlock().getName())).vertical().text(CompoundText.create().info(MODNAME + modName));

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
}*/
