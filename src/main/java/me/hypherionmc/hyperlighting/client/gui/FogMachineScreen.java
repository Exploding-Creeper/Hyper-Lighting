package me.hypherionmc.hyperlighting.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.client.gui.widgets.FluidStackWidget;
import me.hypherionmc.hyperlighting.client.gui.widgets.Slider;
import me.hypherionmc.hyperlighting.common.blockentities.FogMachineBlockEntity;
import me.hypherionmc.hyperlighting.common.handlers.screen.FogMachineScreenHandler;
import me.hypherionmc.hyperlighting.common.network.FogMachinePacketType;
import me.hypherionmc.hyperlighting.common.network.NetworkHandler;
import me.hypherionmc.hyperlighting.utils.LangUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class FogMachineScreen extends HandledScreen<FogMachineScreenHandler> {

    private static final Identifier TEXTURE = new Identifier(ModConstants.MOD_ID + ":textures/gui/fogger_gui.png");
    private final PlayerInventory player;
    private final FogMachineBlockEntity te;

    private ButtonWidget autoFireToggle;
    private ButtonWidget fireButton;
    private Slider timerSlider;

    public FogMachineScreen(FogMachineScreenHandler containerFogMachine, PlayerInventory player, Text titleIn) {
        super(containerFogMachine, player, titleIn);
        // TODO FIX THIS... IT'S SO FUCKING HACKY!
        this.te = (FogMachineBlockEntity) MinecraftClient.getInstance().world.getBlockEntity(handler.getPos());
        this.player = player;
    }

    @Override
    protected void init() {
        super.init();
        autoFireToggle = new ButtonWidget(this.x + 42, this.y + 15, 74, 20, new LiteralText("Timed: " + Formatting.RED + "Off"), this::onButtonPress);
        this.addDrawableChild(autoFireToggle);

        fireButton = new ButtonWidget(this.x + 120, this.y + 15, 50, 20, new LiteralText("FIRE!"), this::onButtonPress);
        this.addDrawableChild(fireButton);

        timerSlider = new Slider(x + 42, y + 38, 128, 20, new LiteralText("Auto Fire Time: "), te.getAutoFireTime(), 6000d, this::onSliderChange);
        this.addDrawableChild(timerSlider);

        this.addDrawableChild(new FluidStackWidget(this, te::getTank, this.x + 7, this.y + 15, 11, 42));
    }

    private void onSliderChange(Slider slider) {
        if (slider == timerSlider) {
            NetworkHandler.sendFogMachinePacket(te.getPos(), (int) Math.round(slider.getValue() * 6000d), FogMachinePacketType.AUTO_FIRE_TIMER);
        }
    }

    private void onButtonPress(ButtonWidget button) {
        if (button == autoFireToggle) {
            NetworkHandler.sendFogMachinePacket(te.getPos(), 0, FogMachinePacketType.AUTO_FIRE);
        }

        if (button == fireButton) {
            NetworkHandler.sendFogMachinePacket(te.getPos(), 0, FogMachinePacketType.FIRE);
        }
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        this.textRenderer.draw(matrices, this.title.getString(), 7, this.backgroundHeight - 163 + 2, 4210752);
        this.textRenderer.draw(matrices, this.player.getDisplayName().getString(), 7, this.backgroundHeight - 108 + 2, 4210752);

        //this.drawPowerToolTip(pPoseStack, mouseX, mouseY, this.x + 7, this.y + 14, 11, 41, LangUtils.getTooltipTitle("gui.fogger.fluidtooltipname"), new LiteralText(Math.round(((float)this.te.getFluidLevelGui() / 24) * 100) + "%"));
        this.drawPowerToolTip(matrices, mouseX, mouseY, this.x + 22, this.y + 53, 4, 4, LangUtils.getTooltipTitle("gui.fogger.cooldownstatetipname"), LangUtils.makeComponent((this.te.isCooldown ? Formatting.RED + LangUtils.resolveTranslation("gui.fogger.cooldownstatecooldown") : Formatting.BLUE + LangUtils.resolveTranslation("gui.fogger.cooldownstatereheating"))));
        this.drawPowerToolTip(matrices, mouseX, mouseY, this.x + 28, this.y + 53, 4, 4, LangUtils.getTooltipTitle("gui.fogger.statetooltipname"), LangUtils.makeComponent((this.te.canFire() ? Formatting.GREEN + new TranslatableText("gui.fogger.stateready").getString() : Formatting.RED + LangUtils.resolveTranslation("gui.fogger.statenotready"))));
        this.drawPowerToolTip(matrices, mouseX, mouseY, this.x + 34, this.y + 53, 4, 4, LangUtils.getTooltipTitle("gui.fogger.statustooltipname"), LangUtils.makeComponent((this.te.isFiring() ? Formatting.GREEN + new TranslatableText("gui.fogger.firing").getString() : Formatting.RESET + "") + (this.te.hasFluid() ? Formatting.RESET + "" : Formatting.RED + LangUtils.resolveTranslation("gui.foger.outoffluid"))));
        this.drawPowerToolTip(matrices, mouseX, mouseY, this.autoFireToggle.x, this.autoFireToggle.y, this.autoFireToggle.getWidth(), this.autoFireToggle.getHeight(), LangUtils.getTooltipTitle("gui.fogger.autofiretooltipname"), LangUtils.getTranslation("gui.fogger.autofiretooltip1"), LangUtils.getTranslation("gui.fogger.autofiretooltip2"));
        this.drawPowerToolTip(matrices, mouseX, mouseY, this.timerSlider.x, this.timerSlider.y, this.timerSlider.getWidth(), this.timerSlider.getHeight(), LangUtils.getTooltipTitle("gui.fogger.autofiretimetooltipname"), LangUtils.getTranslation("gui.fogger.autofiretimetooltipline1"), LangUtils.getTranslation("gui.fogger.autofiretimetooltipline2"));
        this.drawPowerToolTip(matrices, mouseX, mouseY, this.x + this.handler.getSlot(0).x, this.y + this.handler.getSlot(0).y, 16, 16, LangUtils.getTooltipTitle("gui.fogger.dyetooltipname"), LangUtils.getTranslation("gui.fogger.dyetooltipline1"), LangUtils.getTranslation("gui.fogger.dyetooltipline2"));
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.clearColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        drawTexture(matrices, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        if (this.te.canFire()) {
            drawTexture(matrices, this.x + 28, this.y + 53, 176, 32, 5, 5);
        } else {
            drawTexture(matrices, this.x + 28, this.y + 53, 181, 32, 5, 5);
        }

        if (this.te.isCooldown()) {
            drawTexture(matrices, this.x + 22, this.y + 53, 186, 32, 5, 5);
        }

        if (this.te.isFiring()) {
            drawTexture(matrices, this.x + 34, this.y + 53, 176, 32, 5, 5);
        }

        if (!this.te.hasFluid()) {
            drawTexture(matrices, this.x + 34, this.y + 53, 191, 32, 5, 5);
        }

        this.autoFireToggle.setMessage(new LiteralText("Timed: " + (this.te.autoFireEnabled ? Formatting.GREEN + "On" : Formatting.RED + "Off")));
    }

    private void drawPowerToolTip(MatrixStack stack, int mouseX, int mouseY, int startX, int startY, int sizeX, int sizeY, Text title, Text... description) {
        int k = (this.width - this.backgroundWidth) / 2;
        int l = (this.height - this.backgroundHeight) / 2;
        if (mouseX > startX && mouseX < startX + sizeX) {
            if (mouseY > startY && mouseY < startY + sizeY) {
                List<Text> list = new ArrayList<>();
                list.add(title);
                list.addAll(List.of(description));
                renderTooltip(stack, list, mouseX - k, mouseY - l);
            }
        }
    }

}
