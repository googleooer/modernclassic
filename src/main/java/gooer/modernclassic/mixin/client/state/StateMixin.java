package gooer.modernclassic.mixin.client.state;

import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Mixin(State.class)
public class StateMixin {

    /*
    private static <T extends Comparable<T>> Object cycleBackwards(Property<T> property, State state) {
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

     */
}