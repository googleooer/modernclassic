package gooer.modernclassic.mixin.client.gui.hud;

import gooer.modernclassic.Modernclassic;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    private static final String VERSION_TEXT = Modernclassic.MOD_NAME + " " + Modernclassic.CURR_VERSION; //FabricLoader.getInstance().getModContainer(Modernclassic.MOD_ID).get().getMetadata().getVersion().toString();


    @Shadow @Final private MinecraftClient client;

    @Shadow public abstract TextRenderer getTextRenderer();

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbar(FLnet/minecraft/client/util/math/MatrixStack;)V"))
    private void renderVersionText(MatrixStack matrices, float f, CallbackInfo ci){

        if(!this.client.options.hudHidden && !this.client.options.debugEnabled) {
            this.client.textRenderer.drawWithShadow(matrices, VERSION_TEXT, 2.0F, 2.0F, 0xFFFFFF);

            int i = 1;

            if(this.client.player.getInventory().main.stream().anyMatch(itemStack -> itemStack.isOf(Items.CLOCK))) {
                this.client.textRenderer.drawWithShadow(matrices, String.format("Day %d", this.client.world.getTimeOfDay() / 24000L), 2.0F, 2.0f + this.getTextRenderer().fontHeight * i, 0xFFFFFF);
                i++;
            }

            if(this.client.player.getInventory().main.stream().anyMatch(itemStack -> itemStack.isOf(Items.COMPASS))) {
                this.client.textRenderer.drawWithShadow(
                        matrices,
                        String.format("Facing %s", this.client.getCameraEntity().getHorizontalFacing()),
                        2.0F,
                        2.0f + this.getTextRenderer().fontHeight * i,
                        0xFFFFFF);
                i++;
            }

            if(this.client.player.getInventory().main.stream().anyMatch(itemStack -> itemStack.isOf(Items.MAP) || itemStack.isOf(Items.FILLED_MAP))) {
                this.client.textRenderer.drawWithShadow(
                        matrices,
                        String.format("X %d, Z %d", this.client.player.getBlockPos().getX(), this.client.player.getBlockPos().getZ()),
                        2.0F,
                        2.0f + this.getTextRenderer().fontHeight * i,
                        0xFFFFFF);
                i++;
            }



        }
    }
}