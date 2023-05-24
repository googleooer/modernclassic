package gooer.modernclassic.entity.vehicle.dispenser;

import gooer.modernclassic.entity.vehicle.dispenser.ItemDispenserMinecartBehavior;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldEvents;
import org.apache.logging.log4j.core.jmx.Server;

public class BoatDispenserMinecartBehavior
extends ItemDispenserMinecartBehavior {

    private final ItemDispenserMinecartBehavior itemDispenser = new ItemDispenserMinecartBehavior();
    private final BoatEntity.Type boatType;
    private final boolean chest;

    public BoatDispenserMinecartBehavior(BoatEntity.Type type){
        this(type, false);
    }

    public BoatDispenserMinecartBehavior(BoatEntity.Type boatType, boolean chest){
        this.boatType = boatType;
        this.chest = chest;
    }

    @Override
    public ItemStack dispenseSilently(Entity sourceEntity, ItemStack stack) {
        double g;
        Direction direction = sourceEntity.getMovementDirection();
        ServerWorld world = (ServerWorld)sourceEntity.getWorld();
        double d = sourceEntity.getX() + (double)((float)direction.getOffsetX() * 1.125f);
        double e = sourceEntity.getY() + (double)((float)direction.getOffsetY() * 1.125f);
        double f = sourceEntity.getZ() + (double)((float)direction.getOffsetZ() * 1.125f);
        BlockPos blockPos = sourceEntity.getBlockPos().offset(direction);
        if (world.getFluidState(blockPos).isIn(FluidTags.WATER)) {
            g = 1.0;
        } else if (world.getBlockState(blockPos).isAir() && world.getFluidState(blockPos.down()).isIn(FluidTags.WATER)) {
            g = 0.0;
        } else {
            return this.itemDispenser.dispense(sourceEntity, stack);
        }
        BoatEntity boatEntity = this.chest ? new ChestBoatEntity(world, d, e + g, f) : new BoatEntity(world, d, e + g, f);
        boatEntity.setBoatType(this.boatType);
        boatEntity.setYaw(direction.asRotation());
        world.spawnEntity(boatEntity);
        stack.decrement(1);
        return stack;
    }

    @Override
    protected void playSound(Entity sourceEntity){
        sourceEntity.getWorld().syncWorldEvent(WorldEvents.DISPENSER_DISPENSES, sourceEntity.getBlockPos(),0);
    }



}