package me.hypherionmc.hyperlighting.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.common.containers.ContainerBatteryNeon;
import me.hypherionmc.hyperlighting.common.tile.TileBatteryNeon;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public class GuiBatteryNeon extends AbstractContainerScreen<ContainerBatteryNeon> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModConstants.MODID + ":textures/gui/neon_light.png");
    private final Inventory player;
    private final TileBatteryNeon te;

    public GuiBatteryNeon(ContainerBatteryNeon containerSolarPanel, Inventory player, Component titleIn) {
        super(containerSolarPanel, player, titleIn);
        this.player = player;
        this.te = containerSolarPanel.getTe();

    }

    @Override
    public void init() {
        super.init();
        int id = 0;

    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        GuiUtils.drawTexturedModalRect(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 0);
        GuiUtils.drawTexturedModalRect(matrixStack, this.leftPos + 47, this.topPos + 20, 0, 198, (int) (((float) this.te.getPowerLevel() / this.te.getMaxPowerLevel()) * 110), 16, 1);

        if (this.te.isCharging()) {
            GuiUtils.drawTexturedModalRect(matrixStack, this.leftPos + 26, this.topPos + 38, 185, 17, 4, 4, 2);
        }
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        String tileName = "Fluorescent Light";
        this.font.draw(matrixStack, tileName, 7, 6, 4210752);
        this.font.draw(matrixStack, this.player.getDisplayName().getString(), 7, this.imageHeight - 96 + 2, 4210752);

        this.drawPowerToolTip(matrixStack, mouseX, mouseY, leftPos + this.menu.slots.get(0).x, topPos + this.menu.slots.get(0).y, 16, 16, ChatFormatting.YELLOW + "Power Slot", "Place a wireless battery", "module in this slot", "linked to a solar panel", "to charge the light");
        this.drawPowerToolTip(matrixStack, mouseX, mouseY, leftPos + this.menu.slots.get(1).x, topPos + this.menu.slots.get(1).y, 16, 16, ChatFormatting.YELLOW + "Dye Slot", "Place dye here to", "change the color of the", "light");
        this.drawPowerToolTip(matrixStack, mouseX, mouseY, this.leftPos + 47, this.topPos + 20, 110, 16, ChatFormatting.YELLOW + "Power Level", ChatFormatting.BLUE + "" + (int) (((float) this.te.getPowerLevel() / this.te.getMaxPowerLevel()) * 100) + "%", (te.isCharging() ? ChatFormatting.GREEN + "Charging" : ChatFormatting.RED + "Not Charging"));

    }

    private void drawPowerToolTip(PoseStack stack, int mouseX, int mouseY, int startX, int startY, int sizeX, int sizeY, String title, String... description) {
        int k = (this.width - this.imageWidth) / 2; //X asis on GUI
        int l = (this.height - this.imageHeight) / 2; //Y asis on GUI
        if (mouseX > startX && mouseX < startX + sizeX) //Basically checking if mouse is in the correct area
        {
            if (mouseY > startY && mouseY < startY + sizeY) {
                List<TranslatableComponent> list = new ArrayList<>();
                list.add(new TranslatableComponent(title));
                for (String desc : description) {
                    list.add(new TranslatableComponent(desc));
                }
                renderComponentTooltip(stack, list, mouseX - k, mouseY - l, font);
                //GuiUtils.drawHoveringText(stack, list, (int)mouseX - k, (int)mouseY - l, this.width, this.height, 200, font);
            }
        }
    }
}
