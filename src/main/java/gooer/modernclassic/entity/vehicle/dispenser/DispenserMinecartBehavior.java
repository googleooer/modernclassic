package gooer.modernclassic.entity.vehicle.dispenser;

import com.mojang.logging.LogUtils;
import gooer.modernclassic.entity.vehicle.DispenserMinecartEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.dispenser.BoatDispenserBehavior;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Util;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import org.apache.logging.log4j.core.jmx.Server;
import org.slf4j.Logger;

import java.util.List;

public interface DispenserMinecartBehavior {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DispenserMinecartBehavior NOOP = (pointer, stack) -> stack;


    public ItemStack dispense(Entity sourceEntity, ItemStack var2);

    public static void registerDefaults() {
        DispenserMinecartEntity.registerBehavior(Items.ARROW, new ProjectileDispenserMinecartBehavior(){
            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack){
                ArrowEntity arrowEntity = new ArrowEntity(world,position.getX(),position.getY(),position.getZ());
                arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                return arrowEntity;
            }

        });

        DispenserMinecartEntity.registerBehavior(Items.TIPPED_ARROW, new ProjectileDispenserMinecartBehavior() {


            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                ArrowEntity arrowEntity = new ArrowEntity(world, position.getX(), position.getY(), position.getZ());
                arrowEntity.initFromStack(stack);
                arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                return arrowEntity;
            }
        });

        DispenserMinecartEntity.registerBehavior(Items.SPECTRAL_ARROW, new ProjectileDispenserMinecartBehavior() {
            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                SpectralArrowEntity persistentProjectileEntity = new SpectralArrowEntity(world, position.getX(), position.getY(), position.getZ());
                persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                return persistentProjectileEntity;
            }
        });

        DispenserMinecartEntity.registerBehavior(Items.EGG, new ProjectileDispenserMinecartBehavior() {
            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                return Util.make(new EggEntity(world, position.getX(), position.getY(), position.getZ()), entity -> entity.setItem(stack));
            }
        });

        DispenserMinecartEntity.registerBehavior(Items.SNOWBALL, new ProjectileDispenserMinecartBehavior() {
            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                return Util.make(new SnowballEntity(world, position.getX(), position.getY(), position.getZ()), entity -> entity.setItem(stack));
            }
        });

        DispenserMinecartEntity.registerBehavior(Items.EXPERIENCE_BOTTLE, new ProjectileDispenserMinecartBehavior() {
            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                return Util.make(new ExperienceBottleEntity(world, position.getX(), position.getY(), position.getZ()), entity -> entity.setItem(stack));
            }

            @Override
            protected float getVariation() {
                return super.getVariation() * 0.5f;
            }

            @Override
            protected float getForce() {
                return super.getForce() * 1.25f;
            }
        });

        DispenserMinecartEntity.registerBehavior(Items.SPLASH_POTION, new DispenserMinecartBehavior() {

            @Override
            public ItemStack dispense(Entity sourceEntity, ItemStack itemStack){
                return new ProjectileDispenserMinecartBehavior(){

                    @Override
                    protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                        return Util.make(new PotionEntity(world, position.getX(), position.getY(), position.getZ()), entity -> entity.setItem(stack));
                    }

                    @Override
                    protected float getVariation() {
                        return super.getVariation() * 0.5f;
                    }

                    @Override
                    protected float getForce() {
                        return super.getForce() * 1.25f;
                    }

                }.dispense(sourceEntity, itemStack);



            }
        });

        DispenserMinecartEntity.registerBehavior(Items.LINGERING_POTION, new DispenserMinecartBehavior() {
            @Override
            public ItemStack dispense(Entity sourceEntity, ItemStack itemStack){
                return new ProjectileDispenserMinecartBehavior(){

                    @Override
                    protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                        return Util.make(new PotionEntity(world, position.getX(), position.getY(), position.getZ()), entity -> entity.setItem(stack));
                    }

                    @Override
                    protected float getVariation() {
                        return super.getVariation() * 0.5f;
                    }

                    @Override
                    protected float getForce() {
                        return super.getForce() * 1.25f;
                    }

                }.dispense(sourceEntity, itemStack);



            }
        });

        ItemDispenserMinecartBehavior itemDispenserMinecartBehavior = new ItemDispenserMinecartBehavior(){

            @Override
            public ItemStack dispenseSilently(Entity sourceEntity, ItemStack stack){
                Direction direction = sourceEntity.getMovementDirection().rotateCounterclockwise(Direction.Axis.Y);
                EntityType<?> entityType = ((SpawnEggItem)stack.getItem()).getEntityType(stack.getNbt());
                try {
                    entityType.spawnFromItemStack((ServerWorld) sourceEntity.getWorld(), stack, null, sourceEntity.getBlockPos().offset(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
                }
                catch (Exception exception) {
                    LOGGER.error("Error while dispensing spawn egg from dispenser at {}", (Object)sourceEntity.getPos(), (Object)exception);
                    return ItemStack.EMPTY;
                }
                stack.decrement(1);
                sourceEntity.getWorld().emitGameEvent(null, GameEvent.ENTITY_PLACE, sourceEntity.getPos());
                return stack;
            }

        };
        for (SpawnEggItem spawnEggItem : SpawnEggItem.getAll()) {
            DispenserMinecartEntity.registerBehavior(spawnEggItem, itemDispenserMinecartBehavior);
        }

        DispenserMinecartEntity.registerBehavior(Items.ARMOR_STAND, new ItemDispenserMinecartBehavior(){

            @Override
            public ItemStack dispenseSilently(Entity sourceEntity, ItemStack stack) {
                Direction direction = sourceEntity.getMovementDirection().rotateCounterclockwise(Direction.Axis.Y);
                BlockPos blockPos = sourceEntity.getBlockPos().offset(direction);
                ServerWorld world = (ServerWorld) sourceEntity.getWorld();
                ArmorStandEntity armorStandEntity = new ArmorStandEntity(world, (double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5);
                EntityType.loadFromEntityNbt(world, null, armorStandEntity, stack.getNbt());
                armorStandEntity.setYaw(direction.asRotation());
                world.spawnEntity(armorStandEntity);
                stack.decrement(1);
                return stack;
            }

        });

        DispenserMinecartEntity.registerBehavior(Items.SADDLE, new FallibleItemDispenserMinecartBehavior() {

            @Override
            public ItemStack dispenseSilently(Entity sourceEntity, ItemStack stack){
                BlockPos blockPos = sourceEntity.getBlockPos().offset(sourceEntity.getMovementDirection().rotateCounterclockwise(Direction.Axis.Y));
                List<LivingEntity> list = sourceEntity.getWorld().getEntitiesByClass(LivingEntity.class, new Box(blockPos), entity -> {
                    if (entity instanceof Saddleable) {
                        Saddleable saddleable = (Saddleable)((Object)entity);
                        return !saddleable.isSaddled() && saddleable.canBeSaddled();
                    }
                    return false;
                });
                if (!list.isEmpty()) {
                    ((Saddleable)((Object)list.get(0))).saddle(SoundCategory.BLOCKS);
                    stack.decrement(1);
                    this.setSuccess(true);
                    return stack;
                }
                return super.dispenseSilently(sourceEntity, stack);
            }


        });

        FallibleItemDispenserMinecartBehavior itemDispenserMinecartBehavior2 = new FallibleItemDispenserMinecartBehavior() {

            @Override
            protected ItemStack dispenseSilently(Entity sourceEntity, ItemStack stack){
                BlockPos blockPos = sourceEntity.getBlockPos().offset(sourceEntity.getMovementDirection().rotateCounterclockwise(Direction.Axis.Y));
                List<AbstractHorseEntity> list = sourceEntity.getWorld().getEntitiesByClass(AbstractHorseEntity.class, new Box(blockPos), entity -> entity.isAlive() && entity.hasArmorSlot());
                for (AbstractHorseEntity abstractHorseEntity : list) {
                    if (!abstractHorseEntity.isHorseArmor(stack) || abstractHorseEntity.hasArmorInSlot() || !abstractHorseEntity.isTame()) continue;
                    abstractHorseEntity.getStackReference(401).set(stack.split(1));
                    this.setSuccess(true);
                    return stack;
                }
                return super.dispenseSilently(sourceEntity, stack);
            }
        };


        DispenserMinecartEntity.registerBehavior(Items.LEATHER_HORSE_ARMOR, itemDispenserMinecartBehavior2);
        DispenserMinecartEntity.registerBehavior(Items.IRON_HORSE_ARMOR, itemDispenserMinecartBehavior2);
        DispenserMinecartEntity.registerBehavior(Items.GOLDEN_HORSE_ARMOR, itemDispenserMinecartBehavior2);
        DispenserMinecartEntity.registerBehavior(Items.DIAMOND_HORSE_ARMOR, itemDispenserMinecartBehavior2);
        DispenserMinecartEntity.registerBehavior(Items.WHITE_CARPET, itemDispenserMinecartBehavior2);
        DispenserMinecartEntity.registerBehavior(Items.ORANGE_CARPET, itemDispenserMinecartBehavior2);
        DispenserMinecartEntity.registerBehavior(Items.CYAN_CARPET, itemDispenserMinecartBehavior2);
        DispenserMinecartEntity.registerBehavior(Items.BLUE_CARPET, itemDispenserMinecartBehavior2);
        DispenserMinecartEntity.registerBehavior(Items.BROWN_CARPET, itemDispenserMinecartBehavior2);
        DispenserMinecartEntity.registerBehavior(Items.BLACK_CARPET, itemDispenserMinecartBehavior2);
        DispenserMinecartEntity.registerBehavior(Items.GRAY_CARPET, itemDispenserMinecartBehavior2);
        DispenserMinecartEntity.registerBehavior(Items.GREEN_CARPET, itemDispenserMinecartBehavior2);
        DispenserMinecartEntity.registerBehavior(Items.LIGHT_BLUE_CARPET, itemDispenserMinecartBehavior2);
        DispenserMinecartEntity.registerBehavior(Items.LIGHT_GRAY_CARPET, itemDispenserMinecartBehavior2);
        DispenserMinecartEntity.registerBehavior(Items.LIME_CARPET, itemDispenserMinecartBehavior2);
        DispenserMinecartEntity.registerBehavior(Items.MAGENTA_CARPET, itemDispenserMinecartBehavior2);
        DispenserMinecartEntity.registerBehavior(Items.PINK_CARPET, itemDispenserMinecartBehavior2);
        DispenserMinecartEntity.registerBehavior(Items.PURPLE_CARPET, itemDispenserMinecartBehavior2);
        DispenserMinecartEntity.registerBehavior(Items.RED_CARPET, itemDispenserMinecartBehavior2);
        DispenserMinecartEntity.registerBehavior(Items.YELLOW_CARPET, itemDispenserMinecartBehavior2);
        DispenserMinecartEntity.registerBehavior(Items.CHAIN, new FallibleItemDispenserMinecartBehavior() {

            @Override
            public ItemStack dispenseSilently(Entity sourceEntity, ItemStack stack){
                BlockPos blockPos = sourceEntity.getBlockPos().offset(sourceEntity.getMovementDirection().rotateCounterclockwise(Direction.Axis.Y));
                List<AbstractDonkeyEntity> list = sourceEntity.getWorld().getEntitiesByClass(AbstractDonkeyEntity.class, new Box(blockPos), entity -> entity.isAlive() && !entity.hasChest());
                for (AbstractDonkeyEntity abstractDonkeyEntity : list) {
                    if (!abstractDonkeyEntity.isTame() || !abstractDonkeyEntity.getStackReference(499).set(stack)) continue;
                    stack.decrement(1);
                    this.setSuccess(true);
                    return stack;
                }
                return super.dispenseSilently(sourceEntity, stack);
            }

        });

        DispenserMinecartEntity.registerBehavior(Items.FIREWORK_ROCKET, new ItemDispenserMinecartBehavior(){

            @Override
            public ItemStack dispenseSilently(Entity sourceEntity, ItemStack stack){
                Direction direction = sourceEntity.getMovementDirection().rotateCounterclockwise(Direction.Axis.Y);
                FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity((World)sourceEntity.getWorld(), stack, sourceEntity.getX(), sourceEntity.getY(), sourceEntity.getX(), true);
                DispenserBehavior.setEntityPosition(new BlockPointerImpl((ServerWorld) sourceEntity.world, sourceEntity.getBlockPos()), fireworkRocketEntity, direction);
                fireworkRocketEntity.setVelocity(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ(), 0.5f, 1.0f);
                sourceEntity.getWorld().spawnEntity(fireworkRocketEntity);
                stack.decrement(1);
                return stack;
            }

            @Override
            protected void playSound(Entity sourceEntity){
                sourceEntity.getWorld().syncWorldEvent(WorldEvents.FIREWORK_ROCKET_SHOOTS, sourceEntity.getBlockPos(), 0);
            }

        });

        DispenserMinecartEntity.registerBehavior(Items.FIRE_CHARGE,new ItemDispenserMinecartBehavior(){

            @Override
            public ItemStack dispenseSilently(Entity sourceEntity, ItemStack stack){
                Direction direction = sourceEntity.getMovementDirection().rotateCounterclockwise(Direction.Axis.Y);
                Position position = DispenserMinecartEntity.getOutputLocation(sourceEntity, sourceEntity.getBlockPos());
                double d = position.getX() + (double)((float)direction.getOffsetX() * 0.3f);
                double e = position.getY() + (double)((float)direction.getOffsetY() * 0.3f);
                double f = position.getZ() + (double)((float)direction.getOffsetZ() * 0.3f);
                ServerWorld world = (ServerWorld)sourceEntity.getWorld();
                Random random = world.random;
                double g = random.nextTriangular(direction.getOffsetX(), 0.11485000000000001);
                double h = random.nextTriangular(direction.getOffsetY(), 0.11485000000000001);
                double i = random.nextTriangular(direction.getOffsetZ(), 0.11485000000000001);
                SmallFireballEntity smallFireballEntity = new SmallFireballEntity(world, d, e, f, g, h, i);
                world.spawnEntity(Util.make(smallFireballEntity, entity -> entity.setItem(stack)));
                stack.decrement(1);
                return stack;
            }

            @Override
            protected void playSound(Entity sourceEntity){
                sourceEntity.getWorld().syncWorldEvent(WorldEvents.BLAZE_SHOOTS, sourceEntity.getBlockPos(),0);
            }

        });


        DispenserMinecartEntity.registerBehavior(Items.OAK_BOAT, new BoatDispenserMinecartBehavior(BoatEntity.Type.OAK));
        DispenserMinecartEntity.registerBehavior(Items.SPRUCE_BOAT, new BoatDispenserMinecartBehavior(BoatEntity.Type.SPRUCE));
        DispenserMinecartEntity.registerBehavior(Items.BIRCH_BOAT, new BoatDispenserMinecartBehavior(BoatEntity.Type.BIRCH));
        DispenserMinecartEntity.registerBehavior(Items.JUNGLE_BOAT, new BoatDispenserMinecartBehavior(BoatEntity.Type.JUNGLE));
        DispenserMinecartEntity.registerBehavior(Items.DARK_OAK_BOAT, new BoatDispenserMinecartBehavior(BoatEntity.Type.DARK_OAK));
        DispenserMinecartEntity.registerBehavior(Items.ACACIA_BOAT, new BoatDispenserMinecartBehavior(BoatEntity.Type.ACACIA));
        DispenserMinecartEntity.registerBehavior(Items.MANGROVE_BOAT, new BoatDispenserMinecartBehavior(BoatEntity.Type.MANGROVE));
        DispenserMinecartEntity.registerBehavior(Items.OAK_CHEST_BOAT, new BoatDispenserMinecartBehavior(BoatEntity.Type.OAK, true));
        DispenserMinecartEntity.registerBehavior(Items.SPRUCE_CHEST_BOAT, new BoatDispenserMinecartBehavior(BoatEntity.Type.SPRUCE, true));
        DispenserMinecartEntity.registerBehavior(Items.BIRCH_CHEST_BOAT, new BoatDispenserMinecartBehavior(BoatEntity.Type.BIRCH, true));
        DispenserMinecartEntity.registerBehavior(Items.JUNGLE_CHEST_BOAT, new BoatDispenserMinecartBehavior(BoatEntity.Type.JUNGLE, true));
        DispenserMinecartEntity.registerBehavior(Items.DARK_OAK_CHEST_BOAT, new BoatDispenserMinecartBehavior(BoatEntity.Type.DARK_OAK, true));
        DispenserMinecartEntity.registerBehavior(Items.ACACIA_CHEST_BOAT, new BoatDispenserMinecartBehavior(BoatEntity.Type.ACACIA, true));
        DispenserMinecartEntity.registerBehavior(Items.MANGROVE_CHEST_BOAT, new BoatDispenserMinecartBehavior(BoatEntity.Type.MANGROVE, true));
        ItemDispenserMinecartBehavior dispenserMinecartBehavior = new ItemDispenserMinecartBehavior(){
            private final ItemDispenserMinecartBehavior fallbackBehavior = new ItemDispenserMinecartBehavior();

            @Override
            public ItemStack dispenseSilently(Entity sourceEntity, ItemStack stack){
                FluidModificationItem fluidModificationItem = (FluidModificationItem)((Object)stack.getItem());
                BlockPos blockPos = sourceEntity.getBlockPos().offset(sourceEntity.getMovementDirection().rotateCounterclockwise(Direction.Axis.Y));
                ServerWorld world = (ServerWorld)sourceEntity.getWorld();
                if (fluidModificationItem.placeFluid(null, world, blockPos, null)) {
                    fluidModificationItem.onEmptied(null, world, stack, blockPos);
                    return new ItemStack(Items.BUCKET);
                }
                return this.fallbackBehavior.dispense(sourceEntity, stack);
            }
        };
        DispenserMinecartEntity.registerBehavior(Items.LAVA_BUCKET, dispenserMinecartBehavior);
        DispenserMinecartEntity.registerBehavior(Items.WATER_BUCKET, dispenserMinecartBehavior);
        DispenserMinecartEntity.registerBehavior(Items.POWDER_SNOW_BUCKET, dispenserMinecartBehavior);
        DispenserMinecartEntity.registerBehavior(Items.SALMON_BUCKET, dispenserMinecartBehavior);
        DispenserMinecartEntity.registerBehavior(Items.COD_BUCKET, dispenserMinecartBehavior);
        DispenserMinecartEntity.registerBehavior(Items.PUFFERFISH_BUCKET, dispenserMinecartBehavior);
        DispenserMinecartEntity.registerBehavior(Items.TROPICAL_FISH_BUCKET, dispenserMinecartBehavior);
        DispenserMinecartEntity.registerBehavior(Items.AXOLOTL_BUCKET, dispenserMinecartBehavior);
        DispenserMinecartEntity.registerBehavior(Items.TADPOLE_BUCKET, dispenserMinecartBehavior);
        DispenserMinecartEntity.registerBehavior(Items.BUCKET, new ItemDispenserMinecartBehavior(){
            private final ItemDispenserMinecartBehavior fallbackBehavior = new ItemDispenserMinecartBehavior();

            @Override
            public ItemStack dispenseSilently(Entity sourceEntity, ItemStack stack){
                ItemStack itemStack;
                BlockPos blockPos;
                ServerWorld worldAccess = (ServerWorld)sourceEntity.getWorld();
                BlockState blockState = worldAccess.getBlockState(blockPos = sourceEntity.getBlockPos().offset(sourceEntity.getMovementDirection().rotateCounterclockwise(Direction.Axis.Y)));
                Block block = blockState.getBlock();
                if (block instanceof FluidDrainable) {
                    itemStack = ((FluidDrainable)((Object)block)).tryDrainFluid(worldAccess, blockPos, blockState);
                    if (itemStack.isEmpty()) {
                        return super.dispenseSilently(sourceEntity, stack);
                    }
                } else {
                    return super.dispenseSilently(sourceEntity, stack);
                }
                worldAccess.emitGameEvent(null, GameEvent.FLUID_PICKUP, blockPos);
                Item item = itemStack.getItem();
                stack.decrement(1);
                if (stack.isEmpty()) {
                    return new ItemStack(item);
                }
                if (((DispenserMinecartEntity)sourceEntity).addToFirstFreeSlot(new ItemStack(item)) < 0) {
                    this.fallbackBehavior.dispense(sourceEntity, new ItemStack(item));
                }
                return stack;
            }
        });

        //DispenserMinecartEntity.registerBehavior(Items/.);






    }

}