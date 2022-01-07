package me.hypherionmc.hyperlighting.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.gui.GuiUtils;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

public class RenderUtils {

    public static TextureAtlasSprite getFluidTexture(FluidStack fluidStack, boolean still) {
        Fluid fluid = fluidStack.getFluid();
        FluidAttributes attributes = fluid.getAttributes();
        ResourceLocation fluidStill = still ? attributes.getStillTexture(fluidStack) : attributes.getFlowingTexture(fluidStack);
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidStill);
    }

    public static Vector4f colorIntToRGBA(int color) {
        float a = 1.0F;
        float r = (color >> 16 & 0xFF) / 255.0F;
        float g = (color >> 8 & 0xFF) / 255.0F;
        float b = (color & 0xFF) / 255.0F;

        return new Vector4f(r, g, b, a);
    }

    public static ResourceLocation getFluidTextureGui(FluidStack fluidStack, boolean still) {
       TextureAtlasSprite textureAtlasSprite = getFluidTexture(fluidStack, still);
       ResourceLocation resourceLocation = textureAtlasSprite.getName();
       return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath() + ".png");
    }

    public static ResourceLocation getFluidTextureGui(TextureAtlasSprite textureAtlasSprite) {
        ResourceLocation resourceLocation = textureAtlasSprite.getName();
        return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath() + ".png");
    }

}
