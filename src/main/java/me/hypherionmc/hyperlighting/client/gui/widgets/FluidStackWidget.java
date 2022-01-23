package me.hypherionmc.hyperlighting.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import me.hypherionmc.hyperlighting.api.fluid.FluidStorageTank;
import me.hypherionmc.hyperlighting.utils.LangUtils;
import me.hypherionmc.hyperlighting.utils.RenderUtils;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * Modified from https://github.com/SleepyTrousers/EnderIO-Rewrite/blob/dev/1.18.x/enderio-machines/src/main/java/com/enderio/machines/client/FluidStackWidget.java
 */
public class FluidStackWidget extends ClickableWidget {

    private final Screen displayOn;
    private final Supplier<FluidStorageTank> getFluid;

    public FluidStackWidget(Screen displayOn, Supplier<FluidStorageTank> getFluid, int pX, int pY, int pWidth, int pHeight) {
        super(pX, pY, pWidth, pHeight, LiteralText.EMPTY);
        this.displayOn = displayOn;
        this.getFluid = getFluid;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder pNarrationElementOutput) {
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        FluidStorageTank fluidTank = getFluid.get();
        if (!fluidTank.getResource().isBlank()) {
            FluidVariant fluidStack = fluidTank.getResource();
            Sprite still = FluidVariantRendering.getSprite(fluidStack);
            if (still != null) {
                RenderSystem.setShaderTexture(0, SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);

                int color = FluidVariantRendering.getColor(fluidStack);
                RenderSystem.setShaderColor(
                        RenderUtils.ARGB32.red(color) / 255.0F,
                        RenderUtils.ARGB32.green(color) / 255.0F,
                        RenderUtils.ARGB32.blue(color) / 255.0F,
                        RenderUtils.ARGB32.alpha(color) / 255.0F);
                RenderSystem.enableBlend();

                long stored = fluidTank.getAmount();
                float capacity = fluidTank.getCapacity();
                float filledVolume = stored / capacity;
                int renderableHeight = (int) (filledVolume * height);

                int atlasWidth = (int) (still.getWidth() / (still.getMaxU() - still.getMinU()));
                int atlasHeight = (int) (still.getHeight() / (still.getMaxV() - still.getMinV()));

                matrices.push();
                matrices.translate(0, height - 16, 0);
                for (int i = 0; i < Math.ceil(renderableHeight / 16f); i++) {
                    int drawingHeight = Math.min(16, renderableHeight - 16 * i);
                    int notDrawingHeight = 16 - drawingHeight;
                    drawTexture(matrices, x, y + notDrawingHeight, displayOn.getZOffset(), still.getMinU() * atlasWidth, still.getMinV() * atlasHeight + notDrawingHeight, this.width, drawingHeight, atlasWidth, atlasHeight);
                    matrices.translate(0, -16, 0);
                }

                RenderSystem.setShaderColor(1, 1, 1, 1);
                matrices.pop();
            }
            renderTooltip(matrices, mouseX, mouseY);
        }
    }

    @Override
    public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
        if (this.visible && this.isFocused() && isHovered()) {
            displayOn.renderTooltip(matrices, Arrays.asList(LangUtils.getTooltipTitle("gui.fogger.fluidtooltipname"), new LiteralText((int) (((float) this.getFluid.get().getAmount() / this.getFluid.get().getCapacity()) * 100) + "%")), mouseX, mouseY);
        }
    }
}
