package me.hypherionmc.hyperlighting.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.common.containers.ContainerSwitchBoard;
import me.hypherionmc.hyperlighting.common.tile.TileSwitchBoard;
import me.hypherionmc.hyperlighting.util.SwitchBoardHelper;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public class GuiSwitchBoard extends ContainerScreen<ContainerSwitchBoard> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModConstants.MODID + ":textures/gui/switchboard_gui.png");
    private final PlayerInventory player;
    private final TileSwitchBoard te;
    private ImageButton buttonImage;
    private List<SwitchBoardHelper> slots = new ArrayList<>();

    public GuiSwitchBoard(ContainerSwitchBoard containerSwitchBoard, PlayerInventory inventory, ITextComponent title) {
        super(containerSwitchBoard, inventory, title);
        this.player = inventory;
        this.te = containerSwitchBoard.getTe();

        slots.add(new SwitchBoardHelper(new Vector2f(17, 14 + 22), new Vector2f(24, 32), new Vector2f(29, 32), new Vector2f(43, 14)));
        slots.add(new SwitchBoardHelper(new Vector2f(66, 14 + 22), new Vector2f(73, 32), new Vector2f(78, 32), new Vector2f(92, 14)));
        slots.add(new SwitchBoardHelper(new Vector2f(115, 14 + 22), new Vector2f(122, 32), new Vector2f(127, 32), new Vector2f(141, 14)));
        slots.add(new SwitchBoardHelper(new Vector2f(17, 44 + 22), new Vector2f(24, 62), new Vector2f(29, 62), new Vector2f(43, 44)));
        slots.add(new SwitchBoardHelper(new Vector2f(66, 44 + 22), new Vector2f(73, 62), new Vector2f(78, 62), new Vector2f(92, 44)));
        slots.add(new SwitchBoardHelper(new Vector2f(115, 44 + 22), new Vector2f(122, 62), new Vector2f(127, 62), new Vector2f(141, 44)));

    }

    @Override
    public void init() {
        super.init();
        int id = 0;
        for (SwitchBoardHelper itm : this.slots) {
            int finalId = id;
            this.buttonImage = new ImageButton(this.guiLeft + (int)itm.getButtonPos().x, this.guiTop + (int)itm.getButtonPos().y, 16, 16, 176, 0,0, TEXTURE, (button) -> this.actionPerformed(button, finalId));
            addButton(this.buttonImage);
            id++;
        }

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        GuiUtils.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize, 0);

        for (int j = 0; j < 6; j++) {
            if (this.getContainer().inventorySlots.get(j).getHasStack() && this.te.isLinked(j)) {

                int i = this.te.getPowerLevel(j);
                SwitchBoardHelper helper = slots.get(j);
                GuiUtils.drawTexturedModalRect(this.guiLeft + (int)helper.getBarPos().x, this.guiTop + (int)helper.getBarPos().y - i + 1, 176, 39 - i, 4, i + 1, 1);

                if (this.te.getCharging(j)) {
                    GuiUtils.drawTexturedModalRect(this.guiLeft + (int)helper.getChargePos().x, this.guiTop + (int)helper.getChargePos().y, 189, 17, 4, 4, 2);
                }

                if (this.te.getState(j)) {
                    GuiUtils.drawTexturedModalRect(this.guiLeft + (int)helper.getStatepos().x, this.guiTop + (int)helper.getStatepos().y, 181, 17, 4, 4, 3);
                } else {
                    GuiUtils.drawTexturedModalRect(this.guiLeft + (int)helper.getStatepos().x, this.guiTop + (int)helper.getStatepos().y, 185, 17, 4, 4, 3);

                }
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        //String tileName = Objects.requireNonNull(this.te.getDisplayName()).getString();
        //this.font.drawString(matrixStack, tileName, 7, 4, 4210752);
        this.font.drawString(matrixStack, this.player.getDisplayName().getString(), 7, this.ySize - 96 + 2, 4210752);

        for (int i = 0; i < slots.size(); ++i) {
            if (this.container.inventorySlots.get(i).getHasStack() && this.te.isLinked(i)) {
                SwitchBoardHelper helper = slots.get(i);
                this.drawPowerToolTip(matrixStack, mouseX, mouseY, this.guiLeft + (int)helper.getBarPos().x, this.guiTop + (int)helper.getBarPos().y - 22, 4, 23, "Battery Level", TextFormatting.YELLOW + "" + Math.round(this.te.getPowerLevelPer(i)) + "%");
                this.drawPowerToolTip(matrixStack, mouseX, mouseY, this.guiLeft + (int)helper.getChargePos().x,this.guiTop + (int)helper.getChargePos().y, 4, 4, "Charging State", (this.te.getCharging(i) ? TextFormatting.GREEN + "Charging" : TextFormatting.RED + "Not Charging"));
                this.drawPowerToolTip(matrixStack, mouseX, mouseY, this.guiLeft + (int)helper.getStatepos().x,this.guiTop + (int)helper.getStatepos().y, 4, 4, "Power State", (this.te.getState(i) ? TextFormatting.GREEN + "On" : TextFormatting.RED + "Off"));

            }
        }
    }

    private void drawPowerToolTip (MatrixStack stack, int mouseX, int mouseY, int startX, int startY, int sizeX, int sizeY, String title, String description) {
        int k = (this.width - this.xSize) / 2; //X asis on GUI
        int l = (this.height - this.ySize) / 2; //Y asis on GUI
        if (mouseX > startX && mouseX < startX + sizeX) //Basically checking if mouse is in the correct area
        {
            if (mouseY > startY && mouseY < startY + sizeY)
            {
                List<TranslationTextComponent> list = new ArrayList<>();
                list.add(new TranslationTextComponent(title));
                list.add(new TranslationTextComponent(description));
                GuiUtils.drawHoveringText(stack, list,(int)mouseX - k, (int)mouseY - l, this.width, this.height, 200, font);
            }
        }
    }

    protected void actionPerformed(Button button, int bid) {
        switch (bid) {
            case 0:
                if (this.container.inventorySlots.get(0).getHasStack()) { this.te.toggleState(0); }
                break;
            case 1:
                if (this.container.inventorySlots.get(1).getHasStack()) { this.te.toggleState(1); }
                break;
            case 2:
                if (this.container.inventorySlots.get(2).getHasStack()) { this.te.toggleState(2); }
                break;
            case 3:
                if (this.container.inventorySlots.get(3).getHasStack()) { this.te.toggleState(3); }
                break;
            case 4:
                if (this.container.inventorySlots.get(4).getHasStack()) { this.te.toggleState(4); }
                break;
            case 5:
                if (this.container.inventorySlots.get(5).getHasStack()) { this.te.toggleState(5); }
                break;

        }
    }
}
