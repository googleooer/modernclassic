package gooer.modernclassic.mixin.entity.projectile;

import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.data.DataTracker;

import java.util.OptionalInt;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin
        extends ProjectileEntity
        implements FlyingItemEntity
{

    public FireworkRocketEntityMixin(EntityType<? extends FireworkRocketEntity> entityType, World world) {
        super((EntityType<? extends ProjectileEntity>)entityType, world);
    }


    @Shadow private @Nullable LivingEntity shooter;

    /*
    public FireworkRocketEntityMixin(EntityType<? extends FireworkRocketEntity> entityType, World world) {
        super((EntityType<? extends ProjectileEntity>)entityType, world);
    }
     */

    @Shadow public abstract ItemStack getStack();

    @Shadow @Final private static TrackedData<OptionalInt> SHOOTER_ENTITY_ID;

    @Shadow protected abstract boolean wasShotByEntity();




    /*
    public FireworkRocketEntityMixin(World world, ItemStack stack, LivingEntity shooter) {
        this(world, shooter, shooter.getX(), shooter.getY(), shooter.getZ(), stack);
        this.dataTracker.set(SHOOTER_ENTITY_ID, OptionalInt.of(shooter.getId()));
        this.shooter = shooter;
    }

    */


    /**
     * @author Max
     * @reason Add elytra durability change
     */
    @Inject(
            method = "tick",
            at = @At(
                    target = "Lnet/minecraft/entity/LivingEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
                    value = "INVOKE"))
    private void applyElytraBoostDamage(CallbackInfo ci){
        if (this.shooter instanceof PlayerEntity) {
            ItemStack playerChestItem = this.shooter.getEquippedStack(EquipmentSlot.CHEST);
            if( playerChestItem.getItem().equals(Items.ELYTRA) ) {
                int damageAmt = 2;
                playerChestItem.setDamage(playerChestItem.getDamage()+damageAmt);

            }

        }

    }


}