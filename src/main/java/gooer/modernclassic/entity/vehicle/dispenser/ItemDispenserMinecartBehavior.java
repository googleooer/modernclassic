package gooer.modernclassic.entity.vehicle.dispenser;

import gooer.modernclassic.entity.vehicle.DispenserMinecartEntity;
import gooer.modernclassic.entity.vehicle.dispenser.DispenserMinecartBehavior;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

public class ItemDispenserMinecartBehavior
implements DispenserMinecartBehavior {

    @Override
    public final ItemStack dispense(Entity sourceEntity, ItemStack itemStack){
        ItemStack itemStack2 = this.dispenseSilently(sourceEntity, itemStack);
        this.playSound(sourceEntity);
        this.spawnParticles(sourceEntity, sourceEntity.getHorizontalFacing());
        return itemStack2;
    }

    protected ItemStack dispenseSilently(Entity sourceEntity, ItemStack stack) {
        Direction direction = sourceEntity.getMovementDirection().rotateCounterclockwise(Direction.Axis.Y);
        Position position = DispenserMinecartEntity.getOutputLocation(sourceEntity, sourceEntity.getBlockPos());
        ItemStack itemStack = stack.split(1);
        ItemDispenserMinecartBehavior.spawnItem(sourceEntity.getWorld(), itemStack, 6, direction, position);
        return stack;
    }

    public static void spawnItem(World world, ItemStack stack, int speed, Direction side, Position pos) {
        double d = pos.getX();
        double e = pos.getY();
        double f = pos.getZ();
        e = side.getAxis() == Direction.Axis.Y ? (e -= 0.125) : (e -= 0.15625);
        ItemEntity itemEntity = new ItemEntity(world, d, e, f, stack);
        double g = world.random.nextDouble() * 0.1 + 0.2;
        itemEntity.setVelocity(world.random.nextTriangular((double)side.getOffsetX() * g, 0.0172275 * (double)speed), world.random.nextTriangular(0.2, 0.0172275 * (double)speed), world.random.nextTriangular((double)side.getOffsetZ() * g, 0.0172275 * (double)speed));
        world.spawnEntity(itemEntity);
    }


    protected void playSound(Entity sourceEntity) {
        sourceEntity.getWorld().syncWorldEvent(WorldEvents.DISPENSER_DISPENSES, sourceEntity.getBlockPos(), 0);
    }

    protected void spawnParticles(Entity sourceEntity, Direction side) {
        sourceEntity.getWorld().syncWorldEvent(WorldEvents.DISPENSER_ACTIVATED, sourceEntity.getBlockPos(), side.getId());
    }



}