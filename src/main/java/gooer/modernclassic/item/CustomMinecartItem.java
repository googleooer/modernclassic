package gooer.modernclassic.item;/*
 * Decompiled with CFR 0.1.1 (FabricMC 57d88659).
 */

import gooer.modernclassic.entity.vehicle.CustomMinecartLogic;
import gooer.modernclassic.entity.vehicle.CustomMinecartTypeEnum;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

public class CustomMinecartItem
        extends Item {
    private static final DispenserBehavior DISPENSER_BEHAVIOR = new ItemDispenserBehavior(){
        private final ItemDispenserBehavior defaultBehavior = new ItemDispenserBehavior();

        @Override
        public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            double g;
            RailShape railShape;
            Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
            ServerWorld world = pointer.getWorld();
            double d = pointer.getX() + (double)direction.getOffsetX() * 1.125;
            double e = Math.floor(pointer.getY()) + (double)direction.getOffsetY();
            double f = pointer.getZ() + (double)direction.getOffsetZ() * 1.125;
            BlockPos blockPos = pointer.getPos().offset(direction);
            BlockState blockState = world.getBlockState(blockPos);
            RailShape railShape2 = railShape = blockState.getBlock() instanceof AbstractRailBlock ? blockState.get(((AbstractRailBlock)blockState.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
            if (blockState.isIn(BlockTags.RAILS)) {
                g = railShape.isAscending() ? 0.6 : 0.1;
            } else if (blockState.isAir() && world.getBlockState(blockPos.down()).isIn(BlockTags.RAILS)) {
                RailShape railShape22;
                BlockState blockState2 = world.getBlockState(blockPos.down());
                RailShape railShape3 = railShape22 = blockState2.getBlock() instanceof AbstractRailBlock ? blockState2.get(((AbstractRailBlock)blockState2.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
                g = direction == Direction.DOWN || !railShape22.isAscending() ? -0.9 : -0.4;
            } else {
                return this.defaultBehavior.dispense(pointer, stack);
            }
            AbstractMinecartEntity abstractMinecartEntity = CustomMinecartLogic.create(world, d, e + g, f, ((CustomMinecartItem)stack.getItem()).type);
            if (stack.hasCustomName()) {
                abstractMinecartEntity.setCustomName(stack.getName());
            }
            world.spawnEntity(abstractMinecartEntity);
            stack.decrement(1);
            return stack;
        }

        @Override
        protected void playSound(BlockPointer pointer) {
            pointer.getWorld().syncWorldEvent(WorldEvents.DISPENSER_DISPENSES, pointer.getPos(), 0);
        }
    };
    final CustomMinecartTypeEnum.Type type;

    public CustomMinecartItem(CustomMinecartTypeEnum.Type type, Item.Settings settings) {
        super(settings);
        this.type = type;
        DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
    }



    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos blockPos;
        World world = context.getWorld();
        BlockState blockState = world.getBlockState(blockPos = context.getBlockPos());
        if (!blockState.isIn(BlockTags.RAILS)) {
            return ActionResult.FAIL;
        }
        ItemStack itemStack = context.getStack();
        if (!world.isClient) {
            RailShape railShape = blockState.getBlock() instanceof AbstractRailBlock ? blockState.get(((AbstractRailBlock)blockState.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
            double d = 0.0;
            if (railShape.isAscending()) {
                d = 0.5;
            }
            AbstractMinecartEntity abstractMinecartEntity = CustomMinecartLogic.create(world, (double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.0625 + d, (double)blockPos.getZ() + 0.5, this.type);
            if (itemStack.hasCustomName()) {
                abstractMinecartEntity.setCustomName(itemStack.getName());
            }
            world.spawnEntity(abstractMinecartEntity);
            world.emitGameEvent(GameEvent.ENTITY_PLACE, blockPos, GameEvent.Emitter.of(context.getPlayer(), world.getBlockState(blockPos.down())));
        }
        itemStack.decrement(1);
        return ActionResult.success(world.isClient);
    }
}

