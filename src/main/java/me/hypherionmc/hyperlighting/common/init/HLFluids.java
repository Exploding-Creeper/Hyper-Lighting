package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.hyperlighting.HyperLighting;
import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.common.fluids.ColoredWater;
import me.hypherionmc.hyperlighting.common.items.ColoredWaterBottle;
import me.hypherionmc.hyperlighting.common.items.ColoredWaterBucketItem;
import me.hypherionmc.hyperlighting.util.ModUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;

public class HLFluids {

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, ModConstants.MODID);
    public static final HashMap<DyeColor, ColoredWaterEntry> COLORED_WATER = new HashMap<>();
    public static final HashMap<DyeColor, ColoredWaterEntry> GLOWING_COLORED_WATER = new HashMap<>();

    public static final ResourceLocation WATER_STILL_RL = new ResourceLocation("minecraft:block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = new ResourceLocation("minecraft:block/water_flow");
    public static final ResourceLocation WATER_OVERLAY_RL = new ResourceLocation("minecraft:block/water_overlay");

    public static void registerWaterColors(IEventBus bus) {
        for (DyeColor color : DyeColor.values()) {
            COLORED_WATER.put(color, new ColoredWaterEntry(color, false));
            GLOWING_COLORED_WATER.put(color, new ColoredWaterEntry(color, true));
        }
        FLUIDS.register(bus);
    }

    public static FlowingFluid getFluidFlowing(DyeColor color, boolean isGlowing) {
        return isGlowing ? GLOWING_COLORED_WATER.get(color).FLOWING.get() : COLORED_WATER.get(color).FLOWING.get();
    }

    public static FlowingFluid getFluidStill(DyeColor color, boolean isGlowing) {
        return isGlowing ? GLOWING_COLORED_WATER.get(color).STILL.get() : COLORED_WATER.get(color).STILL.get();
    }

    public static Block getFluidBlock(DyeColor color, boolean isGlowing) {
        return isGlowing ? GLOWING_COLORED_WATER.get(color).BLOCK.get() : COLORED_WATER.get(color).BLOCK.get();
    }

    public static Item getFluidBucket(DyeColor color, boolean isGlowing) {
        return isGlowing ? GLOWING_COLORED_WATER.get(color).BUCKET.get() : COLORED_WATER.get(color).BUCKET.get();
    }

    public static Item getWaterBottle(DyeColor color, boolean isGlowing) {
        return isGlowing ? GLOWING_COLORED_WATER.get(color).WATER_BOTTLE.get() : COLORED_WATER.get(color).WATER_BOTTLE.get();
    }

    public static HashMap<DyeColor, ColoredWaterEntry> getFluidMap() {
        return COLORED_WATER;
    }

    public static HashMap<DyeColor, ColoredWaterEntry> getGlowingFluidMap() {
        return GLOWING_COLORED_WATER;
    }

    public static class ColoredWaterEntry {
        private final RegistryObject<FlowingFluid> STILL;
        private final RegistryObject<FlowingFluid> FLOWING;
        private final RegistryObject<Block> BLOCK;
        private final RegistryObject<Item> BUCKET;
        private final RegistryObject<Item> WATER_BOTTLE;

        public ColoredWaterEntry(DyeColor color, boolean isGlowing) {
            STILL = FLUIDS.register(color.getName().toLowerCase() + (isGlowing ? "_colored_glowing_water_still" : "_colored_water_still"), () -> new ColoredWater.Source(color, isGlowing, makeProperties(color)));
            FLOWING = FLUIDS.register(color.getName().toLowerCase() + (isGlowing ? "_colored_glowing_water_flowing" : "_colored_water_flowing"), () -> new ColoredWater.Flowing(color, isGlowing, makeProperties(color)));
            BUCKET = HLItems.ITEMS.register(color.getName().toLowerCase() + (isGlowing ? "_colored_glowing_water_bucket" : "_colored_water_bucket"), () -> new ColoredWaterBucketItem(STILL, new Item.Properties().craftRemainder(Items.BUCKET).tab(HyperLighting.fluidsTab).stacksTo(1), color, isGlowing));
            WATER_BOTTLE = HLItems.ITEMS.register(color.getName().toLowerCase() + (isGlowing ? "_glowing_water_bottle" : "_water_bottle"), () -> new ColoredWaterBottle(color, isGlowing));
            BLOCK = HLBlocks.BLOCKS.register(color.getName().toLowerCase() + (isGlowing ? "_colored_glowing_water" : "_colored_water"), () -> new LiquidBlock(STILL, BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops().lightLevel((state) -> isGlowing ? 15 : 0)));
        }

        private ForgeFlowingFluid.Properties makeProperties(DyeColor color) {
            return new ForgeFlowingFluid.Properties(STILL, FLOWING,
                    FluidAttributes.builder(WATER_STILL_RL, WATER_FLOWING_RL).overlay(WATER_OVERLAY_RL)
                            .color(ModUtils.fluidColorFromDye(color))
                            .sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY))
                    .bucket(BUCKET).block(() -> (LiquidBlock) BLOCK.get());
        }

        public Fluid getFLOWING() {
            return FLOWING.get();
        }

        public Fluid getSTILL() {
            return STILL.get();
        }

        public RegistryObject<Block> getBLOCK() {
            return BLOCK;
        }

        public Item getBUCKET() {
            return BUCKET.get();
        }

        public Item getWaterBottle() {
            return WATER_BOTTLE.get();
        }
    }

}
