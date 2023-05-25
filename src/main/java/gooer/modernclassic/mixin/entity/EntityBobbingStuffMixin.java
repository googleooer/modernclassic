package gooer.modernclassic.mixin.entity;

import gooer.modernclassic.duck_accessors.client.render.BobViewFixInterface;
import gooer.modernclassic.duck_accessors.entity.player.CameraPitchFixInterface;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.entity.EntityLike;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class EntityBobbingStuffMixin implements CameraPitchFixInterface,BobViewFixInterface, EntityLike{

    @Override
    public float getPrevSpeed() {
        return prevSpeed;
    }

    @Override
    public float getPrevDistanceWalkedModified(){
        return prevDistanceWalkedModified;
    }

    @Override
    public float getDistanceWalkedModified(){
        return distanceWalkedModified;
    }



    @Unique private float cameraPitch = 0.0f;
    @Unique public float prevCameraPitch = 0.0f;


    @Override
    public void setCameraPitch(float newCameraPitch) {
        this.cameraPitch = newCameraPitch;
    }

    @Override
    public void setPrevCameraPitch(float newPrevCameraPitch) {
        this.prevCameraPitch = newPrevCameraPitch;
    }

    @Override
    public float getCameraPitch() {
        return cameraPitch;
    }

    @Override
    public float getPrevCameraPitch() {
        return prevCameraPitch;
    }


    float prevDistanceWalkedModified;
    float distanceWalkedModified;

    float prevSpeed;


    /**
     * @author Max
     * @reason Adds general prevSpeed. Required by BobViewFixMixin to replace prevHorizontalSpeed with an all-axis version so bobbing supports jumping once again like before 1.13.
     */
    @Inject(method="baseTick()V", at=@At(value="FIELD",ordinal = 0,target = "Lnet/minecraft/entity/LivingEntity;hurtTime:I"))
    void updatePrevSpeed(CallbackInfo ci) {
        this.setPrevCameraPitch(this.getCameraPitch());
    }
}