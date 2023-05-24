package gooer.modernclassic.mixin.server.integrated;

import com.mojang.serialization.Lifecycle;
import gooer.modernclassic.Modernclassic;
import gooer.modernclassic.client.gui.screen.IntegratedServerLoaderWarningScreen;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.server.integrated.IntegratedServerLoader;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(IntegratedServerLoader.class)
public class IntegratedServerLoaderMixin {

    /**
     * @author Max
     * @reason Remove experimental warning
     */
    @Overwrite
    public static void tryLoad(MinecraftClient client, CreateWorldScreen parent, Lifecycle lifecycle, Runnable loader) {
        BooleanConsumer booleanConsumer = confirmed -> {
            if (confirmed) {
                loader.run();
            } else {
                client.setScreen(parent);
            }
        };
        if (lifecycle == Lifecycle.stable()) {
            loader.run();
        } else if (lifecycle == Lifecycle.experimental() && !Modernclassic.serverState.ignoreExperimentalWarning) {
            //client.setScreen(new ConfirmScreen(booleanConsumer, Text.translatable("selectWorld.import_worldgen_settings.experimental.title"), Text.translatable("selectWorld.import_worldgen_settings.experimental.question")));
            client.setScreen(new IntegratedServerLoaderWarningScreen(booleanConsumer, Text.translatable("selectWorld.import_worldgen_settings.experimental.title"), Text.translatable("selectWorld.import_worldgen_settings.experimental.question"), Modernclassic.serverState));
        } else {
            //client.setScreen(new ConfirmScreen(booleanConsumer, Text.translatable("selectWorld.import_worldgen_settings.deprecated.title"), Text.translatable("selectWorld.import_worldgen_settings.deprecated.question")));
            client.setScreen(new IntegratedServerLoaderWarningScreen(booleanConsumer, Text.translatable("selectWorld.import_worldgen_settings.deprecated.title"), Text.translatable("selectWorld.import_worldgen_settings.deprecated.question"), Modernclassic.serverState));
        }
    }


}