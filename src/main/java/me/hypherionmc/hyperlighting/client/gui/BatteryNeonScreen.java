package me.hypherionmc.hyperlighting.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import me.hypherionmc.hyperlighting.ModConstants;
import me.hypherionmc.hyperlighting.common.blockentities.BatteryNeonBlockEntity;
import me.hypherionmc.hyperlighting.common.handlers.screen.BatteryNeonScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class BatteryNeonScreen extends HandledScreen<BatteryNeonScreenHandler> {

    private static final Identifier TEXTURE = new Identifier(ModConstants.MOD_ID + ":textures/gui/neon_light.png");
    private final Inventory player;
    private final BatteryNeonBlockEntity te;

    public BatteryNeonScreen(BatteryNeonScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.player = inventory;

        // TODO FIX THIS... IT'S SO FUCKING HACKY!
        this.te = (BatteryNeonBlockEntity) MinecraftClient.getInstance().world.getBlockEntity(handler.getPos());
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        super.drawForeground(matrices, mouseX, mouseY);
        this.drawPowerToolTip(matrices, mouseX, mouseY, x + this.handler.slots.get(0).x, y + this.handler.slots.get(0).y, 16, 16, Formatting.YELLOW + "Power Slot", "Place a wireless battery", "module in this slot", "linked to a solar panel", "to charge the light");
        this.drawPowerToolTip(matrices, mouseX, mouseY, y + this.handler.slots.get(1).x, y + this.handler.slots.get(1).y, 16, 16, Formatting.YELLOW + "Dye Slot", "Place dye here to", "change the color of the", "light");
        this.drawPowerToolTip(matrices, mouseX, mouseY, this.x + 47, this.y + 20, 110, 16, Formatting.YELLOW + "Power Level", Formatting.BLUE + "" + (int) (((float) this.te.getPowerLevel() / this.te.getMaxPowerLevel()) * 100) + "%", (te.isCharging() ? Formatting.GREEN + "Charging" : Formatting.RED + "Not Charging"));
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexture(matrices, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        drawTexture(matrices, this.x + 47, this.y + 20, 0, 198, (int) (((float) this.te.getPowerLevel() / this.te.getMaxPowerLevel()) * 110), 16);

        if (this.te.isCharging()) {
            drawTexture(matrices, this.x + 26, this.y + 38, 185, 17, 4, 4);
        }
    }

    private void drawPowerToolTip(MatrixStack stack, int mouseX, int mouseY, int startX, int startY, int sizeX, int sizeY, String title, String... description) {
        int k = (this.width - this.backgroundWidth) / 2; //X asis on GUI
        int l = (this.height - this.backgroundHeight) / 2; //Y asis on GUI
        if (mouseX > startX && mouseX < startX + sizeX) //Basically checking if mouse is in the correct area
        {
            if (mouseY > startY && mouseY < startY + sizeY) {
                List<Text> list = new ArrayList<>();
                list.add(new TranslatableText(title));
                for (String desc : description) {
                    list.add(new TranslatableText(desc));
                }
                renderTooltip(stack, list, mouseX - k, mouseY - l);
            }
        }
    }
}
