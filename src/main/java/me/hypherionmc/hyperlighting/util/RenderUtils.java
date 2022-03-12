package me.hypherionmc.hyperlighting.util;

import com.mojang.math.Vector4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
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

    public static TextureAtlasSprite[] getFluidTextures(Fluid fluid) {
        TextureAtlasSprite[] sprites = new TextureAtlasSprite[3];
        FluidAttributes attributes = fluid.getAttributes();
        sprites[0] = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(attributes.getStillTexture());
        sprites[1] = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(attributes.getFlowingTexture());
        sprites[2] = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(attributes.getOverlayTexture());
        return sprites;
    }

}
