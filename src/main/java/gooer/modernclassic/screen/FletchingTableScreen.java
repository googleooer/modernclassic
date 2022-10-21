package gooer.modernclassic.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import gooer.modernclassic.Modernclassic;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class FletchingTableScreen
        extends ForgingScreen<FletchingTableScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(Modernclassic.MOD_ID,"textures/gui/container/fletching_table.png");

    public FletchingTableScreen(FletchingTableScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title, TEXTURE);
        this.titleX = 8;
        this.titleY = 6;
        this.backgroundHeight = 166;
        this.backgroundWidth = 176;
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        RenderSystem.disableBlend();
        super.drawForeground(matrices, mouseX, mouseY);
    }
}