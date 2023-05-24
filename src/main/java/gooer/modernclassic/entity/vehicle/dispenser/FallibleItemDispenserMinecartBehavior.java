package gooer.modernclassic.entity.vehicle.dispenser;

import gooer.modernclassic.entity.vehicle.dispenser.ItemDispenserMinecartBehavior;
import net.minecraft.entity.Entity;

public abstract class FallibleItemDispenserMinecartBehavior
extends ItemDispenserMinecartBehavior {

    private boolean success;

    public FallibleItemDispenserMinecartBehavior(){

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success){

    }

    protected void playSound(Entity sourceEntity){

    }

}