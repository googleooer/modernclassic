package gooer.modernclassic.client;

import gooer.modernclassic.Modernclassic;
import gooer.modernclassic.screen.FletchingTableScreen;
import gooer.modernclassic.screen.WaypointScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

@Environment(EnvType.CLIENT)
public class ModernclassicClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(Modernclassic.WAYPOINT_SCREEN_HANDLER, WaypointScreen::new);
        HandledScreens.register(Modernclassic.FLETCHING_SCREEN_HANDLER, FletchingTableScreen::new);




    }
}
