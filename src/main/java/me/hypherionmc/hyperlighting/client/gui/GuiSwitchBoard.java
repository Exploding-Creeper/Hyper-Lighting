package me.hypherionmc.hyperlighting.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.common.containers.ContainerSwitchBoard;
import me.hypherionmc.hyperlighting.common.tile.TileSwitchBoard;
import me.hypherionmc.hyperlighting.util.SwitchBoardHelper;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.fmlclient.gui.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public class GuiSwitchBoard extends AbstractContainerScreen<ContainerSwitchBoard> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModConstants.MODID + ":textures/gui/switchboard_gui.png");
    private final Inventory player;
    private final TileSwitchBoard te;
    private ImageButton buttonImage;
    private List<SwitchBoardHelper> slots = new ArrayList<>();

    public GuiSwitchBoard(ContainerSwitchBoard containerSwitchBoard, Inventory inventory, Component title) {
        super(containerSwitchBoard, inventory, title);
        this.player = inventory;
        this.te = containerSwitchBoard.getTe();

        slots.add(new SwitchBoardHelper(new Vec2(17, 14 + 22), new Vec2(24, 32), new Vec2(29, 32), new Vec2(43, 14)));
        slots.add(new SwitchBoardHelper(new Vec2(66, 14 + 22), new Vec2(73, 32), new Vec2(78, 32), new Vec2(92, 14)));
        slots.add(new SwitchBoardHelper(new Vec2(115, 14 + 22), new Vec2(122, 32), new Vec2(127, 32), new Vec2(141, 14)));
        slots.add(new SwitchBoardHelper(new Vec2(17, 44 + 22), new Vec2(24, 62), new Vec2(29, 62), new Vec2(43, 44)));
        slots.add(new SwitchBoardHelper(new Vec2(66, 44 + 22), new Vec2(73, 62), new Vec2(78, 62), new Vec2(92, 44)));
        slots.add(new SwitchBoardHelper(new Vec2(115, 44 + 22), new Vec2(122, 62), new Vec2(127, 62), new Vec2(141, 44)));

    }

    @Override
    public void init() {
        super.init();
        int id = 0;
        for (SwitchBoardHelper itm : this.slots) {
            int finalId = id;
            this.buttonImage = new ImageButton(this.leftPos + (int)itm.getButtonPos().x, this.topPos + (int)itm.getButtonPos().y, 16, 16, 176, 0,0, TEXTURE, (button) -> this.actionPerformed(button, finalId));
            addWidget(this.buttonImage);
            id++;
        }

    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindForSetup(TEXTURE);
        GuiUtils.drawTexturedModalRect(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 0);

        for (int j = 0; j < 6; j++) {
            if (this.getMenu().slots.get(j).hasItem() && this.te.isLinked(j)) {

                int i = this.te.getPowerLevel(j);
                SwitchBoardHelper helper = slots.get(j);
                GuiUtils.drawTexturedModalRect(matrixStack, this.leftPos + (int)helper.getBarPos().x, this.topPos + (int)helper.getBarPos().y - i + 1, 176, 39 - i, 4, i + 1, 1);

                if (this.te.getCharging(j)) {
                    GuiUtils.drawTexturedModalRect(matrixStack, this.leftPos + (int)helper.getChargePos().x, this.topPos + (int)helper.getChargePos().y, 189, 17, 4, 4, 2);
                }

                if (this.te.getState(j)) {
                    GuiUtils.drawTexturedModalRect(matrixStack, this.leftPos + (int)helper.getStatepos().x, this.topPos + (int)helper.getStatepos().y, 181, 17, 4, 4, 3);
                } else {
                    GuiUtils.drawTexturedModalRect(matrixStack, this.leftPos + (int)helper.getStatepos().x, this.topPos + (int)helper.getStatepos().y, 185, 17, 4, 4, 3);

                }
            }
        }
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        //String tileName = Objects.requireNonNull(this.te.getDisplayName()).getString();
        //this.font.drawString(matrixStack, tileName, 7, 4, 4210752);
        this.font.draw(matrixStack, this.player.getDisplayName().getString(), 7, this.imageHeight - 96 + 2, 4210752);

        for (int i = 0; i < slots.size(); ++i) {
            if (this.menu.slots.get(i).hasItem() && this.te.isLinked(i)) {
                SwitchBoardHelper helper = slots.get(i);
                this.drawPowerToolTip(matrixStack, mouseX, mouseY, this.leftPos + (int)helper.getBarPos().x, this.topPos + (int)helper.getBarPos().y - 22, 4, 23, "Battery Level", ChatFormatting.YELLOW + "" + Math.round(this.te.getPowerLevelPer(i)) + "%");
                this.drawPowerToolTip(matrixStack, mouseX, mouseY, this.leftPos + (int)helper.getChargePos().x,this.topPos + (int)helper.getChargePos().y, 4, 4, "Charging State", (this.te.getCharging(i) ? ChatFormatting.GREEN + "Charging" : ChatFormatting.RED + "Not Charging"));
                this.drawPowerToolTip(matrixStack, mouseX, mouseY, this.leftPos + (int)helper.getStatepos().x,this.topPos + (int)helper.getStatepos().y, 4, 4, "Power State", (this.te.getState(i) ? ChatFormatting.GREEN + "On" : ChatFormatting.RED + "Off"));

            }
        }
    }

    private void drawPowerToolTip (PoseStack stack, int mouseX, int mouseY, int startX, int startY, int sizeX, int sizeY, String title, String description) {
        int k = (this.width - this.imageWidth) / 2; //X asis on GUI
        int l = (this.height - this.imageHeight) / 2; //Y asis on GUI
        if (mouseX > startX && mouseX < startX + sizeX) //Basically checking if mouse is in the correct area
        {
            if (mouseY > startY && mouseY < startY + sizeY)
            {
                List<TranslatableComponent> list = new ArrayList<>();
                list.add(new TranslatableComponent(title));
                list.add(new TranslatableComponent(description));
                GuiUtils.drawHoveringText(stack, list,(int)mouseX - k, (int)mouseY - l, this.width, this.height, 200, font);
            }
        }
    }

    protected void actionPerformed(Button button, int bid) {
        switch (bid) {
            case 0:
                if (this.menu.slots.get(0).hasItem()) { this.te.toggleState(0); }
                break;
            case 1:
                if (this.menu.slots.get(1).hasItem()) { this.te.toggleState(1); }
                break;
            case 2:
                if (this.menu.slots.get(2).hasItem()) { this.te.toggleState(2); }
                break;
            case 3:
                if (this.menu.slots.get(3).hasItem()) { this.te.toggleState(3); }
                break;
            case 4:
                if (this.menu.slots.get(4).hasItem()) { this.te.toggleState(4); }
                break;
            case 5:
                if (this.menu.slots.get(5).hasItem()) { this.te.toggleState(5); }
                break;

        }
    }
}
