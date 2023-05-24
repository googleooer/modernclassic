package gooer.modernclassic.entity.vehicle.dispenser;

import gooer.modernclassic.entity.vehicle.DispenserMinecartEntity;
import gooer.modernclassic.entity.vehicle.dispenser.ItemDispenserMinecartBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public abstract class ProjectileDispenserMinecartBehavior extends ItemDispenserMinecartBehavior {

    protected abstract net.minecraft.entity.projectile.ProjectileEntity createProjectile(World world, Position position, ItemStack stack);

    public ItemStack dispenseSilently(Entity sourceEntity, ItemStack stack){
        World world = sourceEntity.getWorld();
        Position position = DispenserMinecartEntity.getOutputLocation(sourceEntity, sourceEntity.getBlockPos());
        Direction direction = sourceEntity.getMovementDirection().rotateCounterclockwise(Direction.Axis.Y);
        ProjectileEntity projectileEntity = this.createProjectile(world, position, stack);

        projectileEntity.setVelocity(
                (double) direction.getOffsetX(),
                (double) direction.getOffsetY() + 0.1f,
                (double) direction.getOffsetZ(),
                this.getForce(),
                this.getVariation());

        world.spawnEntity(projectileEntity);
        stack.decrement(1);
        return stack;
    }

    protected void playSound(Entity sourceEntity){
        sourceEntity.getWorld().syncWorldEvent(1002, sourceEntity.getBlockPos(),0);
    }

    protected float getVariation() {
        return 6.0F;
    }

    protected float getForce() {
        return 1.1F;
    }
}