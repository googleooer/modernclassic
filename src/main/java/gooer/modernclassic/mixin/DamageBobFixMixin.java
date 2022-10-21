package gooer.modernclassic.mixin;

import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class DamageBobFixMixin{

    //TODO: Fix this later.

    /*private static void doKnockback(Object entity, double f, double d, double e){
        if(entity instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity)entity;
            if(player instanceof ServerPlayerEntity && !player.world.isClient){
                PacketByteBuf knockback_packet = new PacketByteBuf(Unpooled.buffer());
                ((ServerPlayerEntity) entity).knockbackVelocity;

            }
        }
    }



    @Inject(at = @At("HEAD"),method = "takeKnockback(DDD)V")
    private void knockbackBobbing(double f, double d, double e, CallbackInfo ci)
    {
        KnockbackHandler.knockbackBobbing(this, f, d, e);
    }*/

}