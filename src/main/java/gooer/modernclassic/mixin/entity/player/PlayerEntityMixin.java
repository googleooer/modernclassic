package gooer.modernclassic.mixin.entity.player;

import gooer.modernclassic.duck_accessors.entity.player.PlayerEntityAccessor;
import gooer.modernclassic.data.tutorial.TutorialGroup;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PlayerEntityAccessor {

    @Unique
    private List<TutorialGroup> queuedTutorials = new ArrayList<>();

    @Override
    public void setQueuedTutorials(List<TutorialGroup> queuedTutorials) {
        this.queuedTutorials = queuedTutorials;
        //Modernclassic.LOGGER.info(String.format("PlayerEntity: Set queued tutorials. New size: %d", this.queuedTutorials.size()));
    }

    @Override
    public List<TutorialGroup> getQueuedTutorials() {
        return queuedTutorials;
    }
}