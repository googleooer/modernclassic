package gooer.modernclassic.mixin.client.render;


import gooer.modernclassic.duck_accessors.entity.player.CameraPitchFixInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class BobViewFixMixin{

    @Shadow @Final private MinecraftClient client;

    /**
     * @author Max
     * @reason Rolls back view bobbing behavior to how it was before 1.13.
     */


    @Inject(method="bobView", at = @At(value = "HEAD"), cancellable = true)
    private void bobView(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if(!(this.client.getCameraEntity() instanceof PlayerEntity playerEntity)) {
            return;
        }


        CameraPitchFixInterface playerWPitch = (CameraPitchFixInterface)playerEntity;

        float f = playerEntity.horizontalSpeed - playerEntity.prevHorizontalSpeed;
        float f1 = -(playerEntity.horizontalSpeed + f * tickDelta);
        float f2 = MathHelper.lerp(tickDelta, playerEntity.prevStrideDistance, playerEntity.strideDistance);
        float f3 = MathHelper.lerp(tickDelta, playerWPitch.getPrevCameraPitch(), playerWPitch.getCameraPitch());

        matrices.translate(Math.sin(f1 * (float) Math.PI) * f2 * 0.5F, -Math.abs(Math.cos(f1 * (float) Math.PI) * f2), 0.0F);
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion((float)(Math.sin(f1 * (float) Math.PI) * f2 * 3.0F)));
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion((float)(Math.abs(Math.cos(f1 * (float) Math.PI - 0.2F) * f2) * 5.0F)));
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(f3));

        ci.cancel();
    }
}