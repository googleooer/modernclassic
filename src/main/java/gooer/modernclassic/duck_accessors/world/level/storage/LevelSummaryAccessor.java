package gooer.modernclassic.duck_accessors.world.level.storage;

import com.mojang.serialization.Lifecycle;

public interface LevelSummaryAccessor {

    void setWorldStability(Lifecycle lifecycle);

    Lifecycle getWorldStability();


}