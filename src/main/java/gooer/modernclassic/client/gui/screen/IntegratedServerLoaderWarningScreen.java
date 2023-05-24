package gooer.modernclassic.client.gui.screen;

import gooer.modernclassic.screen.BetaRewindScreenTexts;
import gooer.modernclassic.world.BetaRewindServerState;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class IntegratedServerLoaderWarningScreen extends ConfirmScreen {

    BetaRewindServerState serverState;
    protected static Text ignoreTranslated = BetaRewindScreenTexts.IGNORE;

    public IntegratedServerLoaderWarningScreen(BooleanConsumer callback, Text title, Text message, BetaRewindServerState serverState) {
        super(callback, title, message);
        this.serverState = serverState;
    }

    @Override
    protected void addButtons(int y) {
        this.addButton(new ButtonWidget(this.width / 2 - 155, y, 150, 20, this.yesTranslated, button -> this.callback.accept(true)));
        this.addButton(new ButtonWidget(this.width / 2 - 155 + 160, y, 150, 20, this.noTranslated, button -> this.callback.accept(false)));
        this.addButton(new ButtonWidget(this.width / 2 - 155 + 160 * 2, y, 150, 20, this.ignoreTranslated, button -> {
            this.serverState.ignoreExperimentalWarning = true;
            this.serverState.markDirty();
        }));
    }
}