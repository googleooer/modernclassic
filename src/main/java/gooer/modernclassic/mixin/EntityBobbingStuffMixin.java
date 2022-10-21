package gooer.modernclassic.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.entity.EntityLike;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityBobbingStuffMixin{




    //private static float prevSpeed;

    /*float getPrevSpeed(){
     return prevSpeed;
    }*/

    @Shadow public float speed;

    @Shadow public World world;


    @Shadow private Vec3d pos;

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

    }

    @Inject(method="move(Lnet/minecraft/entity/MovementType$MovementType;Lnet/minecraft/util/math/Position$Vec3d;)V")
    void updateWalkStuff(){

    }


    /**
     * @author Max
     * @reason Adds general prevSpeed. Required by BobViewFixMixin to replace prevHorizontalSpeed with an all-axis version so bobbing supports jumping once again like before 1.13.
     */
    /*@Inject(method="baseTick()V", at=@At(value="FIELD", ordinal = 1, target = "Lnet/minecraft/entity/Entity;prevHorizontalSpeed:F"))
    private void updatePrevSpeed(CallbackInfo ci) {

        prevSpeed = speed;


    }*/



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