package gooer.modernclassic.mixin.world.level.storage;

import com.mojang.serialization.Lifecycle;
import gooer.modernclassic.duck_accessors.world.level.storage.LevelSummaryAccessor;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LevelSummary.class)
public class LevelSummaryMixin /*implements LevelSummaryAccessor*/ {
/*
    @Unique private Lifecycle worldStability = Lifecycle.stable();

    @Override
    public void setWorldStability(Lifecycle lifecycle) {
        this.worldStability = lifecycle;
    }

    @Override
    public Lifecycle getWorldStability() {
        return this.worldStability;
    }*/
}