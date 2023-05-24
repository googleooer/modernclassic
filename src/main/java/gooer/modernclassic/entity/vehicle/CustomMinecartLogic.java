package gooer.modernclassic.entity.vehicle;

import net.minecraft.entity.vehicle.*;
import net.minecraft.world.World;

public class CustomMinecartLogic {
    //Store stuff in here to make life easier without needing mixins
    public static AbstractMinecartEntity create(World world, double x, double y, double z, CustomMinecartTypeEnum.Type type) {

        /*
        if (type == CustomMinecartTypeEnum.Type.CHEST) {
            return new ChestMinecartEntity(world, x, y, z);
        }
        if (type == CustomMinecartTypeEnum.Type.FURNACE) {
            return new FurnaceMinecartEntity(world, x, y, z);
        }
        if (type == CustomMinecartTypeEnum.Type.TNT) {
            return new TntMinecartEntity(world, x, y, z);
        }
        if (type == CustomMinecartTypeEnum.Type.SPAWNER) {
            return new SpawnerMinecartEntity(world, x, y, z);
        }
        if (type == CustomMinecartTypeEnum.Type.HOPPER) {
            return new HopperMinecartEntity(world, x, y, z);
        }
        if (type == CustomMinecartTypeEnum.Type.COMMAND_BLOCK) {
            return new CommandBlockMinecartEntity(world, x, y, z);
        }
        return new MinecartEntity(world, x, y, z);


         */

        switch (type) {
            case CHEST -> {
                return new ChestMinecartEntity(world, x, y, z);
            }
            case FURNACE -> {
                return new FurnaceMinecartEntity(world, x, y, z);
            }
            case TNT -> {
                return new TntMinecartEntity(world, x, y, z);
            }
            case SPAWNER -> {
                return new SpawnerMinecartEntity(world, x, y, z);
            }
            case HOPPER -> {
                return new HopperMinecartEntity(world, x, y, z);
            }
            case COMMAND_BLOCK -> {
                return new CommandBlockMinecartEntity(world, x, y, z);
            }
            case DISPENSER, DROPPER -> {
                return new DispenserMinecartEntity(world, x, y, z);
            }
            default -> {
                return new MinecartEntity(world, x, y, z);
            }
        }



    }
}