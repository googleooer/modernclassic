package gooer.modernclassic.entity.vehicle;

import gooer.modernclassic.Modernclassic;
import gooer.modernclassic.entity.vehicle.dispenser.DispenserMinecartBehavior;
import gooer.modernclassic.entity.vehicle.dispenser.ItemDispenserMinecartBehavior;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.StorageMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Map;

public class DispenserMinecartEntity
        extends StorageMinecartEntity {

    private Direction direction = Direction.NORTH;
    public static final DirectionProperty FACING = FacingBlock.FACING;

    private static final Map<Item, DispenserMinecartBehavior> BEHAVIORS = Util.make(new Object2ObjectOpenHashMap(), map -> map.defaultReturnValue(new ItemDispenserMinecartBehavior()));


    public DispenserMinecartEntity(EntityType<? extends DispenserMinecartEntity> entityType, World world){
        super(entityType, world);
    }

    public DispenserMinecartEntity(World world, double x, double y, double z) {
        super(Modernclassic.DISPENSER_MINECART, x, y, z, world);
    }

    @Override
    public Item getItem(){
        return Modernclassic.DISPENSER_MINECART_ITEM;
    }

    @Override
    public ItemStack getPickBlockStack() {
        return new ItemStack(Modernclassic.DISPENSER_MINECART_ITEM);

    }


    @Override
    public int size() { return 9; }

    @Override
    public AbstractMinecartEntity.Type getMinecartType() {
        return Type.TNT;
    }

    @Override
    public void onActivatorRail(int x, int y, int z, boolean powered){
        if(powered && world instanceof ServerWorld serverWorld) dispense(serverWorld, this.getBlockPos());
    }

    public int chooseNonEmptySlot(Random random) {
        int i = -1;
        int j = 1;
        for (int k = 0; k < this.getInventory().size(); ++k) {
            if (this.getInventory().get(k).isEmpty() || random.nextInt(j++) != 0) continue;
            i = k;
        }
        return i;
    }

    public static Position getOutputLocation(Entity sourceEntity, BlockPos pos){
        Direction direction = sourceEntity.getMovementDirection().rotateCounterclockwise(Direction.Axis.Y);
        double d = pos.getX() + 0.7 * (double)direction.getOffsetX();
        double e = pos.getY() + 0.7 * (double)direction.getOffsetY();
        double f = pos.getZ() + 0.7 * (double)direction.getOffsetZ();

        BlockPos dispensePos = sourceEntity.getBlockPos().offset(sourceEntity.getMovementDirection().rotateCounterclockwise(Direction.Axis.Y));
        return new PositionImpl(dispensePos.getX(),dispensePos.getY(),dispensePos.getZ());
    }

    public int addToFirstFreeSlot(ItemStack stack) {
        for (int i = 0; i < this.getInventory().size(); ++i) {
            if (!this.getInventory().get(i).isEmpty()) continue;
            this.setStack(i, stack);
            return i;
        }
        return -1;
    }



    protected void dispense(ServerWorld world, BlockPos pos){
        BlockPointerImpl blockPointerImpl = new BlockPointerImpl(world, pos);

        int i = this.chooseNonEmptySlot(world.random);
        if (i < 0){
            world.syncWorldEvent(WorldEvents.DISPENSER_FAILS, pos, 0);
            world.emitGameEvent(null, GameEvent.DISPENSE_FAIL, pos);
            return;
        }
        ItemStack itemStack = this.getStack(i);
        DispenserMinecartBehavior dispenserMinecartBehavior = this.getBehaviorForItem(itemStack);
        if(dispenserMinecartBehavior != DispenserMinecartBehavior.NOOP){
            this.setStack(i, dispenserMinecartBehavior.dispense(this,itemStack));
        }
    }

    public static void registerBehavior(ItemConvertible provider, DispenserMinecartBehavior behavior) {
        BEHAVIORS.put(provider.asItem(), behavior);
    }

    protected DispenserMinecartBehavior getBehaviorForItem(ItemStack stack) {
        return BEHAVIORS.get(stack.getItem());
    }




    @Override
    public BlockState getDefaultContainedBlock() {
        //TODO: Get moving direction
        /*if(this.getVelocity().length() > 0.0f){
            Vec3d currentSpeed = this.getVelocity().withAxis(Direction.Axis.Y, 0.0f).normalize();

            switch((int)()){


            }
        }*/


        return (BlockState) Blocks.DISPENSER.getDefaultState().with(DispenserBlock.FACING, this.getMovementDirection());
    }

    @Override
    public ScreenHandler getScreenHandler(int syncId, PlayerInventory playerInventory){
        return new Generic3x3ContainerScreenHandler(syncId,playerInventory, this);
    }

}