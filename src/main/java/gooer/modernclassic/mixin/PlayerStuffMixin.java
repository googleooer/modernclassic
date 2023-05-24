package gooer.modernclassic.mixin;

import gooer.modernclassic.client.CameraPitchFixInterface;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import gooer.modernclassic.mixin.EntityBobbingStuffMixin;

@Mixin(PlayerEntity.class)
public abstract class PlayerStuffMixin extends LivingEntity implements CameraPitchFixInterface
{


    @Shadow public abstract float getMovementSpeed();

    @Shadow @Final private PlayerAbilities abilities;

    @Shadow protected HungerManager hungerManager;

    protected PlayerStuffMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    
    @Inject(method="tickMovement()V", at=@At(value="INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setMovementSpeed(F)V"))
    private void tickMovementInjection(CallbackInfo ci){
        double yVel = this.getVelocity().y;
        float targetPitchModifier = (float)(Math.atan(-yVel * 0.20000000298023224D) * 15.0D);
        float camPitch = this.getCameraPitch();

        boolean isGrounded = yVel < -0.07 && yVel > 0.08;
        if(isGrounded || this.onGround || this.getHealth() <= 0.0f) targetPitchModifier = 0.0f;

        this.setCameraPitch(camPitch + (targetPitchModifier - camPitch) * 0.8f);
    }

    /**
     * @author Max
     * @reason Let players eat if health below 20
     */
    @Overwrite
    public boolean canConsume(boolean ignoreHunger) {
        return this.abilities.invulnerable || ignoreHunger || this.hungerManager.isNotFull() || this.getHealth() < 20;
    }
}