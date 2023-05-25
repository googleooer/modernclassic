package gooer.modernclassic.client.gui.hud;

import gooer.modernclassic.data.tutorial.TutorialGroup;
import gooer.modernclassic.duck_accessors.entity.player.CustomPlayerEntityAccess;
import gooer.modernclassic.data.tutorial.TutorialStep;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.List;

public class TutorialHudOverlay implements HudRenderCallback{

    @Override
    public void onHudRender(MatrixStack matrices, float tickDelta) {

        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;
        if (player != null) {
            List<TutorialGroup> tutorials = ((CustomPlayerEntityAccess) player).getQueuedTutorials();
            if (!tutorials.isEmpty() && tutorials.get(0) != null) {
                if(tutorials.get(0).getSteps().size() < 1) return;
                try {
                    TutorialGroup currentTutorial = tutorials.get(0);
                    TutorialStep currentTutorialStep = currentTutorial.getCurrentStep();
                    drawTutorialOverlay(matrices, currentTutorial, currentTutorialStep);
                } catch (NullPointerException e) {
                    throw new RuntimeException("currentTutorial is nullpointer...... somehow!");
                }
            }
        }
    }

















        /*
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        PlayerEntity playerEntity = minecraftClient.player;
        if(playerEntity == null) return;

        if(!((CustomPlayerEntityAccess)playerEntity).getQueuedTutorials().isEmpty()) {

            int screenWidth = MinecraftClient.getInstance().getWindow().getWidth();
            int screenHeight = MinecraftClient.getInstance().getWindow().getHeight();
            DrawableHelper.fill(matrices, 2, 2, MathHelper.floor(screenWidth / 2) - 50, MathHelper.floor(screenHeight / 4.5), 1262506368);
        }

         */






    private static Text parseFormattedText(String text) {
        return Text.Serializer.fromJson('"' + text.replace("\\n", "\\\\n").replace("\"", "\\\"") + '"');
    }

    private static void drawTutorialOverlay(MatrixStack matrices, TutorialGroup currentTutorial, TutorialStep currentStep) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        int maxWidth = screenWidth / 2;

        List<OrderedText> wrappedTitle = textRenderer.wrapLines(parseFormattedText(currentStep.getTutorialTitle() /*+ "DEBUG_ID: " + currentTutorial.getTutorialId()*/), maxWidth);
        List<OrderedText> wrappedText = textRenderer.wrapLines(parseFormattedText(currentStep.getTutorialText() /*+ " DEBUG_TIMELEFT:" + Integer.toString(currentStep.getDuration())*/), maxWidth);

        int boxWidth = Math.max(wrappedTitle.stream().mapToInt(textRenderer::getWidth).max().orElse(0), wrappedText.stream().mapToInt(textRenderer::getWidth).max().orElse(0)) + 8;
        int boxHeight = (wrappedTitle.size() + wrappedText.size()) * textRenderer.fontHeight + 8;

        int boxX = screenWidth - boxWidth - 4;
        int boxY = 4;

        // Draw background box
        DrawableHelper.fill(matrices, boxX, boxY, boxX + boxWidth, boxY + boxHeight, 1262506368);

        // Draw title and text
        int titleY = boxY + 4;
        for (OrderedText titleLine : wrappedTitle) {
            textRenderer.draw(matrices, titleLine, boxX + 4, titleY, 0xFFFFFF);
            titleY += textRenderer.fontHeight;
        }

        int textY = titleY;
        for (OrderedText textLine : wrappedText) {
            textRenderer.draw(matrices, textLine, boxX + 4, textY, 0xFFFFFF);
            textY += textRenderer.fontHeight;
        }
    }


}