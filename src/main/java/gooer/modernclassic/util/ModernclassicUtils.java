package gooer.modernclassic.util;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ModernclassicUtils {


public static class StateUtils{

    /*
    public <T extends Comparable<T>> State cycleBackwards(Property<T> property, State state) {
        return state.with(property, (Comparable) State.getNext(property.getValues(), state.get(property)));
    }
*/

    public static <T extends Comparable<T>> Object cycleBackwards(Property<T> property, State state) {
        return state.with(property, (Comparable) getPrev((Collection<T>) property.getValues(), (T)state.get(property)));
    }


    protected static <T> T getPrev(Collection<T> values, T value){
        Collections.reverse((List<?>) values);
        Iterator<T> iterator = values.iterator();
        T prevValue = null;
        while(iterator.hasNext()) {
            if(!iterator.next().equals(value)) {
                prevValue = iterator.next();
                continue;}
            if(iterator.hasNext()){
                return prevValue;
            }
            return prevValue;
        }
        return iterator.next();
    }


}




    public static Direction minecartDirectionToVelocity(AbstractMinecartEntity cart){

        Vec3d cartVelocity = cart.getVelocity();

        Vec3i normalized = new Vec3i(MathHelper.floor(cartVelocity.normalize().x),0,MathHelper.floor(cartVelocity.normalize().z));

        if(normalized.equals(new Vec3i(1, 0, 0)) || normalized.equals(new Vec3i(-1, 0, 0)) && cart.yawFlipped){
            return Direction.EAST;
        } else if(normalized.equals(new Vec3i(-1, 0, 0)) && !cart.yawFlipped){
            return Direction.WEST;
        } else if(normalized.equals(new Vec3i(0, 0, -1)) || normalized.equals(new Vec3i(0, 0, 1)) && cart.yawFlipped){
            return Direction.NORTH;
        } else if(normalized.equals(new Vec3i(0, 0, 1)) && !cart.yawFlipped){
            return Direction.SOUTH;
        }

        return Direction.NORTH;
    }







}