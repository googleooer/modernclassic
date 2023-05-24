package gooer.modernclassic.client;

import gooer.modernclassic.Modernclassic;
import gooer.modernclassic.client.gui.hud.TutorialHudOverlay;
import gooer.modernclassic.client.render.entity.DispenserMinecartEntityModel;
import gooer.modernclassic.screen.FletchingTableScreen;
import gooer.modernclassic.screen.WaypointScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.MinecartEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;

@Environment(EnvType.CLIENT)
public class ModernclassicClient implements ClientModInitializer {

    public static final EntityModelLayer DISPENSER_MINECART_LAYER = new EntityModelLayer(new Identifier("modernclassic", "dispenser_minecart"), "main");

    public static Matrix4f blueModelView = new Matrix4f();
    public static Matrix4f blueProjection = new Matrix4f();

    @Override
    public void onInitializeClient() {
        HandledScreens.register(Modernclassic.WAYPOINT_SCREEN_HANDLER, WaypointScreen::new);
        HandledScreens.register(Modernclassic.FLETCHING_SCREEN_HANDLER, FletchingTableScreen::new);

        BlockRenderLayerMap.INSTANCE.putBlock(Modernclassic.FRAMED_GLASS_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(Modernclassic.POWERED_COPPER_RAIL, RenderLayer.getCutout());

        EntityRendererRegistry.register(Modernclassic.DISPENSER_MINECART, (context) -> {
            return new MinecartEntityRenderer(context, DISPENSER_MINECART_LAYER);
        });

        EntityModelLayerRegistry.registerModelLayer(DISPENSER_MINECART_LAYER, DispenserMinecartEntityModel::getTexturedModelData);


        //Create HudRenderCallback so we can render tutorial text & stuff
        HudRenderCallback.EVENT.register(new TutorialHudOverlay());






    }
}
