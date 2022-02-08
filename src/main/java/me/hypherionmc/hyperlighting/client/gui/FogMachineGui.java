package me.hypherionmc.hyperlighting.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.client.gui.widgets.FluidStackWidget;
import me.hypherionmc.hyperlighting.common.containers.ContainerFogMachine;
import me.hypherionmc.hyperlighting.common.network.PacketHandler;
import me.hypherionmc.hyperlighting.common.network.packets.FogMachineAutoFirePacket;
import me.hypherionmc.hyperlighting.common.network.packets.FogMachineAutoFireTimerPacket;
import me.hypherionmc.hyperlighting.common.network.packets.FogMachineFirePacket;
import me.hypherionmc.hyperlighting.common.tile.TileFogMachine;
import me.hypherionmc.hyperlighting.util.LangUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.GuiUtils;
import net.minecraftforge.client.gui.widget.Slider;

import java.util.ArrayList;
import java.util.List;

public class FogMachineGui extends AbstractContainerScreen<ContainerFogMachine> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModConstants.MODID + ":textures/gui/fogger_gui.png");
    private final Inventory player;
    private final TileFogMachine te;

    private Button autoFireToggle;
    private Button fireButton;
    private Slider timerSlider;

    public FogMachineGui(ContainerFogMachine containerFogMachine, Inventory player, Component titleIn) {
        super(containerFogMachine, player, titleIn);
        this.te = containerFogMachine.getTe();
        this.player = player;
    }

    @Override
    protected void init() {
        super.init();
        autoFireToggle = new Button(this.leftPos + 42, this.topPos + 15, 74, 20, new TextComponent("Timed: " + ChatFormatting.RED + "Off"), this::onButtonPress);
        this.addRenderableWidget(autoFireToggle);

        fireButton = new Button(this.leftPos + 120, this.topPos + 15, 50, 20, new TextComponent("FIRE!"), this::onButtonPress);
        this.addRenderableWidget(fireButton);

        timerSlider = new Slider(leftPos + 42, topPos + 38, 128, 20, new TextComponent("Auto Fire Time: "), new TextComponent(" Seconds"), 0d, 6000d, te.getAutoFireTime(), true, true, this::onButtonPress, this::onSliderChange);
        timerSlider.setMessage(getDisplayString());
        this.addRenderableWidget(timerSlider);

        this.addRenderableWidget(new FluidStackWidget(this, te::getTank, this.leftPos + 7, this.topPos + 15, 11, 42));
    }

    private void onSliderChange(Slider slider) {
        if (slider == timerSlider) {
            timerSlider.setMessage(getDisplayString());
            FogMachineAutoFireTimerPacket autoFireTimerPacket = new FogMachineAutoFireTimerPacket(te.getBlockPos(), (int) Math.round(slider.sliderValue * 6000d));
            PacketHandler.sendToServer(autoFireTimerPacket);
        }
    }

    private void onButtonPress(Button button) {
        if (button == autoFireToggle) {
            FogMachineAutoFirePacket fogMachineAutoFirePacket = new FogMachineAutoFirePacket(te.getBlockPos(), !te.autoFireEnabled);
            PacketHandler.sendToServer(fogMachineAutoFirePacket);
        }

        if (button == fireButton) {
            FogMachineFirePacket fogMachineFirePacket = new FogMachineFirePacket(te.getBlockPos());
            PacketHandler.sendToServer(fogMachineFirePacket);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.clearColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        GuiUtils.drawTexturedModalRect(pPoseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 0);

        if (this.te.canFire()) {
            GuiUtils.drawTexturedModalRect(pPoseStack, this.leftPos + 28, this.topPos + 53, 176, 32, 5, 5, 1);
        } else {
            GuiUtils.drawTexturedModalRect(pPoseStack,this.leftPos + 28, this.topPos + 53, 181, 32, 5, 5, 1);
        }

        if (this.te.isCooldown()) {
            GuiUtils.drawTexturedModalRect(pPoseStack,this.leftPos + 22, this.topPos + 53, 186, 32, 5, 5, 1);
        }

        if (this.te.isFiring()) {
            GuiUtils.drawTexturedModalRect(pPoseStack,this.leftPos + 34, this.topPos + 53, 176, 32, 5, 5, 1);
        }

        if (!this.te.hasFluid()) {
            GuiUtils.drawTexturedModalRect(pPoseStack,this.leftPos + 34, this.topPos + 53, 191, 32, 5, 5, 1);
        }

        this.autoFireToggle.setMessage(new TextComponent("Timed: " + (this.te.autoFireEnabled ? ChatFormatting.GREEN + "On" : ChatFormatting.RED + "Off")));
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int mouseX, int mouseY) {
        this.font.draw(pPoseStack, this.title.getString(), 7, this.imageHeight - 163 + 2, 4210752);
        this.font.draw(pPoseStack, this.player.getDisplayName().getString(), 7, this.imageHeight - 108 + 2, 4210752);

        //this.drawPowerToolTip(pPoseStack, mouseX, mouseY, this.leftPos + 7, this.topPos + 14, 11, 41, LangUtils.getTooltipTitle("gui.fogger.fluidtooltipname"), new TextComponent(Math.round(((float)this.te.getFluidLevelGui() / 24) * 100) + "%"));
        this.drawPowerToolTip(pPoseStack, mouseX, mouseY, this.leftPos + 22,this.topPos + 53, 4, 4, LangUtils.getTooltipTitle("gui.fogger.cooldownstatetipname"), LangUtils.makeComponent((this.te.isCooldown ? ChatFormatting.RED + LangUtils.resolveTranslation("gui.fogger.cooldownstatecooldown") : ChatFormatting.BLUE + LangUtils.resolveTranslation("gui.fogger.cooldownstatereheating"))));
        this.drawPowerToolTip(pPoseStack, mouseX, mouseY, this.leftPos + 28,this.topPos + 53, 4, 4, LangUtils.getTooltipTitle("gui.fogger.statetooltipname"), LangUtils.makeComponent((this.te.canFire() ? ChatFormatting.GREEN + new TranslatableComponent("gui.fogger.stateready").getString() : ChatFormatting.RED + LangUtils.resolveTranslation("gui.fogger.statenotready"))));
        this.drawPowerToolTip(pPoseStack, mouseX, mouseY, this.leftPos + 34,this.topPos + 53, 4, 4, LangUtils.getTooltipTitle("gui.fogger.statustooltipname"), LangUtils.makeComponent((this.te.isFiring() ? ChatFormatting.GREEN + new TranslatableComponent("gui.fogger.firing").getString() : ChatFormatting.RESET + "") + (this.te.hasFluid() ? ChatFormatting.RESET + "" : ChatFormatting.RED + LangUtils.resolveTranslation("gui.foger.outoffluid"))));
        this.drawPowerToolTip(pPoseStack, mouseX, mouseY, this.autoFireToggle.x, this.autoFireToggle.y, this.autoFireToggle.getWidth(), this.autoFireToggle.getHeight(), LangUtils.getTooltipTitle("gui.fogger.autofiretooltipname"), LangUtils.getTranslation("gui.fogger.autofiretooltip1"), LangUtils.getTranslation("gui.fogger.autofiretooltip2"));
        this.drawPowerToolTip(pPoseStack, mouseX, mouseY, this.timerSlider.x, this.timerSlider.y, this.timerSlider.getWidth(), this.timerSlider.getHeight(), LangUtils.getTooltipTitle("gui.fogger.autofiretimetooltipname"), LangUtils.getTranslation("gui.fogger.autofiretimetooltipline1"), LangUtils.getTranslation("gui.fogger.autofiretimetooltipline2"));
        this.drawPowerToolTip(pPoseStack, mouseX, mouseY, this.leftPos + this.menu.slots.get(0).x, this.topPos + this.menu.slots.get(0).y, 16, 16, LangUtils.getTooltipTitle("gui.fogger.dyetooltipname"), LangUtils.getTranslation("gui.fogger.dyetooltipline1"), LangUtils.getTranslation("gui.fogger.dyetooltipline2"));
    }

    private void drawPowerToolTip(PoseStack stack, int mouseX, int mouseY, int startX, int startY, int sizeX, int sizeY, Component title, Component... description) {
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        if (mouseX > startX && mouseX < startX + sizeX) {
            if (mouseY > startY && mouseY < startY + sizeY) {
                List<Component> list = new ArrayList<>();
                list.add(title);
                list.addAll(List.of(description));
                renderComponentTooltip(stack, list, mouseX - k, mouseY - l, font);
            }
        }
    }

    private Component getDisplayString() {
        long seconds = Math.round(this.timerSlider.sliderValue * this.timerSlider.maxValue / 20);
        long minutes = Math.round(seconds / 60);
        if (this.timerSlider.sliderValue * this.timerSlider.maxValue >= 1200) {
            String appendString = (minutes == 1) ? "Minute" : "Minutes";
            String doSeconds = ((seconds - (minutes * 60)) > 0) ? ", " + (seconds - (minutes * 60)) + " Seconds" : "";
            return new TextComponent(minutes + " " + appendString + doSeconds);
        } else {
            return new TextComponent(seconds + " Seconds");
        }
    }

}
