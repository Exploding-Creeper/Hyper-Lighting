package me.hypherionmc.hyperlighting.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.common.containers.ContainerBatteryNeon;
import me.hypherionmc.hyperlighting.common.tile.TileBatteryNeon;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public class GuiBatteryNeon extends ContainerScreen<ContainerBatteryNeon> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModConstants.MODID + ":textures/gui/neon_light.png");
    private final PlayerInventory player;
    private final TileBatteryNeon te;

    public GuiBatteryNeon(ContainerBatteryNeon containerSolarPanel, PlayerInventory player, ITextComponent titleIn) {
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
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        GuiUtils.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize, 0);
        GuiUtils.drawTexturedModalRect(this.guiLeft + 47, this.guiTop + 20, 0, 198, (int)(((float)this.te.getPowerLevel() / this.te.getMaxPowerLevel()) * 110), 16, 1);

        if (this.te.isCharging()) {
            GuiUtils.drawTexturedModalRect(this.guiLeft + 26, this.guiTop + 38, 185, 17, 4, 4, 2);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        String tileName = "Fluorescent Light";
        this.font.drawString(matrixStack, tileName, 7, 6, 4210752);
        this.font.drawString(matrixStack, this.player.getDisplayName().getString(), 7, this.ySize - 96 + 2, 4210752);

        this.drawPowerToolTip(matrixStack, mouseX, mouseY, guiLeft + this.container.inventorySlots.get(0).xPos, guiTop + this.container.inventorySlots.get(0).yPos, 16, 16, TextFormatting.YELLOW + "Power Slot", "Place a wireless battery", "module in this slot", "linked to a solar panel", "to charge the light");
        this.drawPowerToolTip(matrixStack, mouseX, mouseY, guiLeft + this.container.inventorySlots.get(1).xPos, guiTop + this.container.inventorySlots.get(1).yPos, 16, 16, TextFormatting.YELLOW + "Dye Slot", "Place dye here to", "change the color of the", "light");
        this.drawPowerToolTip(matrixStack, mouseX, mouseY, this.guiLeft + 47, this.guiTop + 20, 110, 16, TextFormatting.YELLOW + "Power Level", TextFormatting.BLUE + "" + (int)(((float)this.te.getPowerLevel() / this.te.getMaxPowerLevel()) * 100) + "%", (te.isCharging() ? TextFormatting.GREEN + "Charging" : TextFormatting.RED + "Not Charging"));

    }

    private void drawPowerToolTip (MatrixStack stack, int mouseX, int mouseY, int startX, int startY, int sizeX, int sizeY, String title, String... description) {
        int k = (this.width - this.xSize) / 2; //X asis on GUI
        int l = (this.height - this.ySize) / 2; //Y asis on GUI
        if (mouseX > startX && mouseX < startX + sizeX) //Basically checking if mouse is in the correct area
        {
            if (mouseY > startY && mouseY < startY + sizeY)
            {
                List<TranslationTextComponent> list = new ArrayList<>();
                list.add(new TranslationTextComponent(title));
                for (String desc : description) {
                    list.add(new TranslationTextComponent(desc));
                }
                GuiUtils.drawHoveringText(stack, list, (int)mouseX - k, (int)mouseY - l, this.width, this.height, 200, font);
            }
        }
    }
}
