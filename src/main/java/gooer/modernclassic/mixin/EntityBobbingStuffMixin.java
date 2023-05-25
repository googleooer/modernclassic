package gooer.modernclassic.mixin;

import gooer.modernclassic.duck_accessors.client.render.BobViewFixInterface;
import gooer.modernclassic.client.CameraPitchFixInterface;
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







    /*

    @Inject(method="<init>", at=@At("HEAD"))
    private static void defineGeneralSpeed(EntityType type, World world, CallbackInfo ci){
        float distanceWalkedModified;
        float prevDistanceWalkedModified;

        double d10 = this.pos.x;
        double d11 = this.pos.y;
        double d1 = this.pos.z;

        double d15 = this.pos.x - d10;
        double d16 = this.pos.y - d11;
        double d17 = this.pos.z - d1;


        distanceWalkedModified = (float((double)distanceWalkedModified + (double)MathHelper.sqrt()))

    }*/

/*

    @Inject(method="move",at=@At("HEAD"))
    void updateWalkStuff(MovementType movementType, Vec3d movement, CallbackInfo ci){
        double d10 = this.pos.x;
        double d11 = this.pos.y;
        double d1 = this.pos.z;

        if(movementType = MovementType.PLAYER && (this.isOnGround() || !this.isSneaking() || !((Entity)this instanceof PlayerEntity) || !this.hasVehicle())){
            double d15 = this.pos.x - d10;
            double d16 = this.pos.y - d11;
            double d17 = this.pos.z - d1;

            this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + (double)MathHelper.sqrt((float) (d15 * d15 + d17 * d17)) * 0.6D);


        }



    }

*/




    /**
     * @author Max
     * @reason Adds general prevSpeed. Required by BobViewFixMixin to replace prevHorizontalSpeed with an all-axis version so bobbing supports jumping once again like before 1.13.
     */
    @Inject(method="baseTick()V", at=@At(value="FIELD",ordinal = 0,target = "Lnet/minecraft/entity/LivingEntity;hurtTime:I"))
    void updatePrevSpeed(CallbackInfo ci) {

        this.setPrevCameraPitch(this.getCameraPitch());




    }





    /*

    @Overwrite
    public void baseTick() {
        this.world.getProfiler().push("entityBaseTick");
        this.blockStateAtPos = null;
        if (this.hasVehicle() && this.getVehicle().isRemoved()) {
            this.stopRiding();
        }
        if (this.ridingCooldown > 0) {
            --this.ridingCooldown;
        }
        this.prevHorizontalSpeed = this.horizontalSpeed;
        this.prevPitch = this.getPitch();
        this.prevYaw = this.getYaw();
        this.tickNetherPortal();
        if (this.shouldSpawnSprintingParticles()) {
            this.spawnSprintingParticles();
        }
        this.wasInPowderSnow = this.inPowderSnow;
        this.inPowderSnow = false;
        this.updateWaterState();
        this.updateSubmergedInWaterState();
        this.updateSwimming();
        if (this.world.isClient) {
            this.extinguish();
        } else if (this.fireTicks > 0) {
            if (this.isFireImmune()) {
                this.setFireTicks(this.fireTicks - 4);
                if (this.fireTicks < 0) {
                    this.extinguish();
                }
            } else {
                if (this.fireTicks % 20 == 0 && !this.isInLava()) {
                    this.damage(DamageSource.ON_FIRE, 1.0f);
                }
                this.setFireTicks(this.fireTicks - 1);
            }
            if (this.getFrozenTicks() > 0) {
                this.setFrozenTicks(0);
                this.world.syncWorldEvent(null, WorldEvents.FIRE_EXTINGUISHED, this.blockPos, 1);
            }
        }
        if (this.isInLava()) {
            this.setOnFireFromLava();
            this.fallDistance *= 0.5f;
        }
        this.attemptTickInVoid();
        if (!this.world.isClient) {
            this.setOnFire(this.fireTicks > 0);
        }
        this.firstUpdate = false;
        this.world.getProfiler().pop();
    }

    */





}