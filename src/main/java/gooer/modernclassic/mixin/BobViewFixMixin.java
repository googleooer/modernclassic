package gooer.modernclassic.mixin;


import gooer.modernclassic.client.BobViewFixInterface;
import gooer.modernclassic.client.CameraPitchFixInterface;
import gooer.modernclassic.screen.FletchingTableScreenHandler;
import net.fabricmc.tinyremapper.extension.mixin.common.Logger;
import net.minecraft.block.BlockState;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.FletchingTableBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import gooer.modernclassic.mixin.EntityBobbingStuffMixin;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.server.Main;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


import java.util.logging.Level;

@Mixin(GameRenderer.class)
public class BobViewFixMixin{

    @Shadow @Final private MinecraftClient client;



    //





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





        //TODO: Implement distanceWalkedModified from 1.12.2 into EntityBobbingStuffMixin and use it here.
        //modified
        //float f = playerEntity.distanceTraveled - ((BobViewFixInterface)playerEntity).getPrevDistanceTraveled();

        //this is pretty much f1, but without verticalspeed
        //float g = -(playerEntity.distanceTraveled + f * ((BobViewFixInterface)playerEntity).getPrevDistanceTraveled());
        //float h = ((MathHelper.lerp(tickDelta, playerEntity.prevStrideDistance, playerEntity.strideDistance)));

        //float f2 = playerEntity.prevYaw + (playerEntity.getYaw() - playerEntity.prevYaw) * tickDelta;
        //float f3 = playerEntity.prevPitch + (playerEntity.getPitch() - playerEntity.prevPitch) * tickDelta;

        //matrices.translate(MathHelper.sin(g * (float)Math.PI) * f2 * 0.5f, -Math.abs(MathHelper.cos(g * (float)Math.PI) * f2), 0.0);

        //original rotation
        //matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.sin(g * (float)Math.PI) * h * 3.0f));
        //matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(Math.abs(MathHelper.cos(g * (float)Math.PI - 0.2f) * h) * 5.0f));

        //matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.sin(g * (float)Math.PI) * f2 * 3.0f));
        //matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(Math.abs(MathHelper.cos(g * (float)Math.PI - 0.2f) * f2) * 5.0f));
        //matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion( f3));


        //original

        //float f = playerEntity.speed - ((BobViewFixInterface)playerEntity).getPrevSpeed();
        //float g = -(playerEntity.speed + f * tickDelta);
        //float h = MathHelper.lerp(tickDelta, playerEntity.prevStrideDistance, playerEntity.strideDistance);
        //matrices.translate(MathHelper.sin(g * (float)Math.PI) * h * 0.5f, -Math.abs(MathHelper.cos(g * (float)Math.PI) * h), 0.0);
        //matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.sin(g * (float)Math.PI) * h * 3.0f));
        //matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(Math.abs(MathHelper.cos(g * (float)Math.PI - 0.2f) * h) * 5.0f));

        //double eyeH = playerEntity.getEyeY();
        //double d0 = playerEntity.prevX + (playerEntity.getPos().x - playerEntity.prevX) * (double)tickDelta;
        //double d1 = playerEntity.prevY + (playerEntity.getPos().y - playerEntity.prevY) * (double)tickDelta + eyeH;
        //double d2 = playerEntity.prevZ + (playerEntity.getPos().z - playerEntity.prevZ) * (double)tickDelta;
        //matrices.translate(0.0F, -eyeH, 0.0F);

         //d0 = playerEntity.prevX + (playerEntity.getPos().x - playerEntity.prevX) * (double)tickDelta;
         //d1 = playerEntity.prevY + (playerEntity.getPos().y - playerEntity.prevY) * (double)tickDelta + eyeH;
         //d2 = playerEntity.prevZ + (playerEntity.getPos().z - playerEntity.prevZ) * (double)tickDelta;



    }

}