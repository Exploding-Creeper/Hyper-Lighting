package me.hypherionmc.hyperlighting.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.common.blockentities.SwitchBoardBlockEntity;
import me.hypherionmc.hyperlighting.common.handlers.screen.SwitchBoardScreenHandler;
import me.hypherionmc.hyperlighting.utils.SwitchBoardHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class SwitchBoardScreen extends HandledScreen<SwitchBoardScreenHandler> {

    private static final Identifier TEXTURE = new Identifier(ModConstants.MOD_ID, "textures/gui/switchboard_gui.png");
    private final Inventory player;
    private final SwitchBoardBlockEntity te;
    private TexturedButtonWidget buttonImage;
    private final List<SwitchBoardHelper> slots = new ArrayList<>();

    public SwitchBoardScreen(SwitchBoardScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.player = inventory;

        // TODO FIX THIS... IT'S SO FUCKING HACKY!
        this.te = (SwitchBoardBlockEntity) MinecraftClient.getInstance().world.getBlockEntity(handler.getPos());

        slots.add(new SwitchBoardHelper(new Vector2f(17, 14 + 22), new Vector2f(24, 32), new Vector2f(29, 32), new Vector2f(43, 14)));
        slots.add(new SwitchBoardHelper(new Vector2f(66, 14 + 22), new Vector2f(73, 32), new Vector2f(78, 32), new Vector2f(92, 14)));
        slots.add(new SwitchBoardHelper(new Vector2f(115, 14 + 22), new Vector2f(122, 32), new Vector2f(127, 32), new Vector2f(141, 14)));
        slots.add(new SwitchBoardHelper(new Vector2f(17, 44 + 22), new Vector2f(24, 62), new Vector2f(29, 62), new Vector2f(43, 44)));
        slots.add(new SwitchBoardHelper(new Vector2f(66, 44 + 22), new Vector2f(73, 62), new Vector2f(78, 62), new Vector2f(92, 44)));
        slots.add(new SwitchBoardHelper(new Vector2f(115, 44 + 22), new Vector2f(122, 62), new Vector2f(127, 62), new Vector2f(141, 44)));
    }

    @Override
    protected void init() {
        super.init();
        //titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
        titleY = titleY - 2;
        int id = 0;
        for (SwitchBoardHelper itm : this.slots) {
            int finalId = id;
            this.buttonImage = new TexturedButtonWidget(this.x + (int) itm.getButtonPos().getX(), this.y + (int) itm.getButtonPos().getY(), 16, 16, 176, 0, 0, TEXTURE, (button) -> this.actionPerformed(button, finalId));
            addSelectableChild(this.buttonImage);
            id++;
        }
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexture(matrices, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        for (int j = 0; j < 6; j++) {
            if (this.handler.slots.get(j).hasStack() && this.te.isLinked(j)) {

                int i = this.te.getPowerLevel(j);
                SwitchBoardHelper helper = slots.get(j);
                drawTexture(matrices, this.x + (int) helper.getBarPos().getX(), this.y + (int) helper.getBarPos().getY() - i + 1, 176, 39 - i, 4, i + 1);

                if (this.te.getCharging(j)) {
                    drawTexture(matrices, this.x + (int) helper.getChargePos().getX(), this.y + (int) helper.getChargePos().getY(), 189, 17, 4, 4);
                }

                if (this.te.getState(j)) {
                    drawTexture(matrices, this.x + (int) helper.getStatepos().getX(), this.y + (int) helper.getStatepos().getY(), 181, 17, 4, 4);
                } else {
                    drawTexture(matrices, this.x + (int) helper.getStatepos().getX(), this.y + (int) helper.getStatepos().getY(), 185, 17, 4, 4);

                }
            }
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        super.drawForeground(matrices, mouseX, mouseY);
        for (int i = 0; i < slots.size(); ++i) {
            if (this.handler.slots.get(i).hasStack() && this.te.isLinked(i)) {
                SwitchBoardHelper helper = slots.get(i);
                this.drawPowerToolTip(matrices, mouseX, mouseY, this.x + (int) helper.getBarPos().getX(), this.y + (int) helper.getBarPos().getY() - 22, 4, 23, "Battery Level", Formatting.YELLOW + "" + Math.round(this.te.getPowerLevelPer(i)) + "%");
                this.drawPowerToolTip(matrices, mouseX, mouseY, this.x + (int) helper.getChargePos().getX(), this.y + (int) helper.getChargePos().getY(), 4, 4, "Charging State", (this.te.getCharging(i) ? Formatting.GREEN + "Charging" : Formatting.RED + "Not Charging"));
                this.drawPowerToolTip(matrices, mouseX, mouseY, this.x + (int) helper.getStatepos().getX(), this.y + (int) helper.getStatepos().getY(), 4, 4, "Power State", (this.te.getState(i) ? Formatting.GREEN + "On" : Formatting.RED + "Off"));
            }
        }
    }

    private void drawPowerToolTip(MatrixStack stack, int mouseX, int mouseY, int startX, int startY, int sizeX, int sizeY, String title, String description) {
        int k = (this.width - this.backgroundWidth) / 2; //X asis on GUI
        int l = (this.height - this.backgroundHeight) / 2; //Y asis on GUI
        if (mouseX > startX && mouseX < startX + sizeX) //Basically checking if mouse is in the correct area
        {
            if (mouseY > startY && mouseY < startY + sizeY) {
                List<Text> list = new ArrayList<>();
                list.add(new TranslatableText(title));
                list.add(new TranslatableText(description));
                renderTooltip(stack, list, mouseX - k, mouseY - l);
            }
        }
    }

    private void actionPerformed(ButtonWidget button, int finalId) {
        if (this.handler.slots.get(finalId).hasStack()) {
            this.te.toggleState(finalId);
        }
    }
}
