package gooer.modernclassic.mixin.client;

import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(InGameOverlayRenderer.class)
public class WaterOverlayMixin {


    @ModifyArg(
            method = "renderUnderwaterOverlay",
    at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V"
    ),
            index=3
    )
    private static float waterOverlayTextureAlphaOverride(float red){
        return 0.5f;
    }



}